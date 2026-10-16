package com.unab.dunab.persistence;

import com.unab.dunab.model.Usuario;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class UsuarioDAO {
    private static final String RUTA_ARCHIVO = "data/usuarios.csv";
    private Map<String, Usuario> baseDatosUsuarios;

    public UsuarioDAO() {
        this.baseDatosUsuarios = new LinkedHashMap<>();
        asegurarDirectorioYArchivo();
        cargarDesdeArchivo();
    }

    private void asegurarDirectorioYArchivo() {
        try {
            File carpeta = new File("data");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            File archivo = new File(RUTA_ARCHIVO);
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error al inicializar almacenamiento de usuarios: " + e.getMessage());
        }
    }

    private synchronized void cargarDesdeArchivo() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists() || archivo.length() == 0) {
            return;
        }

        try (BufferedReader lector = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
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
        File archivo = new File(RUTA_ARCHIVO);
        try (BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
            for (Usuario u : baseDatosUsuarios.values()) {
                escritor.write(serializarUsuario(u));
                escritor.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios en archivo: " + e.getMessage());
        }
    }

    private String serializarUsuario(Usuario u) {
        // Formato CSV
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
            return false; // Ya existe
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

    public synchronized Usuario buscar(String nombreUsuario) {
        if (nombreUsuario == null) return null;
        return baseDatosUsuarios.get(nombreUsuario);
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
