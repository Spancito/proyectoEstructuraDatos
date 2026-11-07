package com.unab.dunab.persistence;

import com.unab.dunab.model.Usuario;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class UsuarioDAO {
    private File archivoDatos;
    private Map<String, Usuario> baseDatosUsuarios;

    public UsuarioDAO() {
        this.baseDatosUsuarios = new LinkedHashMap<>();
        this.archivoDatos = resolverArchivoDatos("usuarios.csv");
        asegurarDirectorioYArchivo();
        cargarDesdeArchivo();
    }

    private File resolverArchivoDatos(String nombreArchivo) {
        File f1 = new File("data", nombreArchivo);
        if (f1.exists()) {
            return f1;
        }
        File f2 = new File("../../../data", nombreArchivo);
        if (f2.exists()) {
            return f2;
        }
        File f3 = new File("../../data", nombreArchivo);
        if (f3.exists()) {
            return f3;
        }
        File dirActual = new File(".").getAbsoluteFile();
        while (dirActual != null) {
            File carpetaData = new File(dirActual, "data");
            if (carpetaData.exists() && carpetaData.isDirectory()) {
                File candidato = new File(carpetaData, nombreArchivo);
                if (candidato.exists()) {
                    return candidato;
                }
            }
            dirActual = dirActual.getParentFile();
        }
        return f1;
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

    private synchronized void sincronizarArchivo() {
        try (BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivoDatos), StandardCharsets.UTF_8))) {
            for (Usuario u : baseDatosUsuarios.values()) {
                escritor.write(serializarUsuario(u));
                escritor.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios en archivo: " + e.getMessage());
        }
    }

    private String serializarUsuario(Usuario u) {
        return escapar(u.getId()) + "," +
                escapar(u.getUsuario()) + "," +
                escapar(u.getContrasena()) + "," +
                escapar(u.getNombre()) + "," +
                escapar(u.getCorreo()) + "," +
                escapar(u.getCarrera()) + "," +
                u.getPresupuestoMensual();
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

        return new Usuario(id, usuario, contrasena, nombre, correo, carrera, presupuesto);
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
        sincronizarArchivo();
        return true;
    }

    public synchronized boolean actualizar(Usuario usuario) {
        if (usuario == null || usuario.getUsuario() == null || !baseDatosUsuarios.containsKey(usuario.getUsuario())) {
            return false;
        }
        baseDatosUsuarios.put(usuario.getUsuario(), usuario);
        sincronizarArchivo();
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
