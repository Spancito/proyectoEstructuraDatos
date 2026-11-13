package com.unab.dunab.persistence;

import com.unab.dunab.model.Usuario;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class UsuarioDAO {
    private File archivoDatos;
    private Map<String, Usuario> baseDatosUsuarios;

    public UsuarioDAO() {
        this.baseDatosUsuarios = new LinkedHashMap<>();
        this.archivoDatos = RutasDatos.obtenerArchivo("usuarios.csv");
        asegurarDirectorioYArchivo();
        cargarDesdeArchivo();
    }

    private void asegurarDirectorioYArchivo() {
        try {
            File carpeta = archivoDatos.getParentFile();
            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }
            if (!archivoDatos.exists()) {
                archivoDatos.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error al inicializar almacenamiento de usuarios: " + e.getMessage());
        }
    }

    private synchronized void cargarDesdeArchivo() {
        if (!archivoDatos.exists() || archivoDatos.length() == 0) {
            return;
        }

        try (BufferedReader lector = new BufferedReader(new InputStreamReader(new FileInputStream(archivoDatos), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }
                Usuario usuario = deserializarUsuario(linea);
                if (usuario != null && usuario.getUsuario() != null) {
                    baseDatosUsuarios.put(usuario.getUsuario(), usuario);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo de usuarios: " + e.getMessage());
        }
    }

    private synchronized boolean sincronizarArchivo() {
        File carpeta = archivoDatos.getAbsoluteFile().getParentFile();
        File temporal = null;
        try {
            if (carpeta != null && !carpeta.exists()) {
                Files.createDirectories(carpeta.toPath());
            }
            temporal = File.createTempFile(archivoDatos.getName() + ".", ".tmp", carpeta);
            try (BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temporal), StandardCharsets.UTF_8))) {
                for (Usuario u : baseDatosUsuarios.values()) {
                    escritor.write(serializarUsuario(u));
                    escritor.newLine();
                }
                escritor.flush();
            }
            try {
                Files.move(temporal.toPath(), archivoDatos.toPath(),
                        StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporal.toPath(), archivoDatos.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            temporal = null;
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios en archivo: " + e.getMessage());
            return false;
        } finally {
            if (temporal != null && temporal.exists()) {
                temporal.delete();
            }
        }
    }

    private String serializarUsuario(Usuario u) {
        return escapar(u.getId()) + "," +
                escapar(u.getUsuario()) + "," +
                escapar(u.getContrasena()) + "," +
                escapar(u.getNombre()) + "," +
                escapar(u.getCorreo()) + "," +
                escapar(u.getCarrera()) + "," +
                u.getPresupuestoMensual() + "," +
                u.getPuntosDunab();
    }

    private Usuario deserializarUsuario(String linea) {
        String[] partes = linea.split(",", -1);
        if (partes.length < 2) {
            return null;
        }

        String id = desescapar(partes[0]);
        String usuario = desescapar(partes[1]);
        String contrasena = partes.length > 2 ? desescapar(partes[2]) : "";
        String nombre = partes.length > 3 ? desescapar(partes[3]) : "";
        String correo = partes.length > 4 ? desescapar(partes[4]) : "";
        String carrera = partes.length > 5 ? desescapar(partes[5]) : "";
        double presupuesto = 0.0;
        if (partes.length > 6) {
            try {
                presupuesto = Double.parseDouble(partes[6]);
            } catch (NumberFormatException ignored) {}
        }
        double puntosDunab = Usuario.PUNTOS_INICIALES;
        if (partes.length > 7) {
            try {
                puntosDunab = Double.parseDouble(partes[7]);
            } catch (NumberFormatException ignored) {}
        }

        return new Usuario(id, usuario, contrasena, nombre, correo, carrera, presupuesto, puntosDunab);
    }

    private String escapar(String valor) {
        if (valor == null) return "";
        return valor.replace("\\", "\\\\").replace(",", "\\,").replace("\n", "\\n");
    }

    private String desescapar(String valor) {
        if (valor == null) return "";
        return valor.replace("\\n", "\n").replace("\\,", ",").replace("\\\\", "\\");
    }

    public synchronized boolean guardar(Usuario usuario) {
        if (usuario == null || usuario.getUsuario() == null) {
            return false;
        }
        if (baseDatosUsuarios.containsKey(usuario.getUsuario())) {
            return false;
        }
        baseDatosUsuarios.put(usuario.getUsuario(), usuario);
        if (!sincronizarArchivo()) {
            baseDatosUsuarios.remove(usuario.getUsuario());
            return false;
        }
        return true;
    }

    public synchronized boolean actualizar(Usuario usuario) {
        if (usuario == null || usuario.getUsuario() == null || !baseDatosUsuarios.containsKey(usuario.getUsuario())) {
            return false;
        }
        Usuario anterior = baseDatosUsuarios.put(usuario.getUsuario(), usuario);
        if (!sincronizarArchivo()) {
            baseDatosUsuarios.put(usuario.getUsuario(), anterior);
            return false;
        }
        return true;
    }

    public synchronized boolean transferirPuntos(String idOrigen, String idDestino, double puntos) {
        if (idOrigen == null || idDestino == null || idOrigen.equals(idDestino)
                || Double.isNaN(puntos) || Double.isInfinite(puntos) || puntos <= 0) {
            return false;
        }

        Usuario origen = buscarPorId(idOrigen);
        Usuario destino = buscarPorId(idDestino);
        if (origen == null || destino == null || origen == destino) {
            return false;
        }
        if (origen.getPuntosDunab() < puntos) {
            return false;
        }

        double saldoOrigen = origen.getPuntosDunab();
        double saldoDestino = destino.getPuntosDunab();
        origen.setPuntosDunab(saldoOrigen - puntos);
        destino.setPuntosDunab(saldoDestino + puntos);

        if (!sincronizarArchivo()) {
            origen.setPuntosDunab(saldoOrigen);
            destino.setPuntosDunab(saldoDestino);
            return false;
        }
        return true;
    }

    public synchronized Usuario buscar(String identificador) {
        if (identificador == null) return null;
        String idLimpio = identificador.trim();
        Usuario directo = baseDatosUsuarios.get(idLimpio);
        if (directo != null) {
            return directo;
        }
        for (Usuario u : baseDatosUsuarios.values()) {
            if (u.getUsuario() != null && u.getUsuario().equalsIgnoreCase(idLimpio)) {
                return u;
            }
            if (u.getCorreo() != null && u.getCorreo().equalsIgnoreCase(idLimpio)) {
                return u;
            }
        }
        return null;
    }

    public synchronized Usuario buscarPorId(String id) {
        if (id == null) return null;
        for (Usuario u : baseDatosUsuarios.values()) {
            if (id.equals(u.getId())) {
                return u;
            }
        }
        return null;
    }

    public synchronized List<Usuario> obtenerTodos() {
        return new ArrayList<>(baseDatosUsuarios.values());
    }
}
