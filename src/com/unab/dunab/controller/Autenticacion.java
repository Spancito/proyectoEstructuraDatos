package com.unab.dunab.controller;

import com.unab.dunab.model.Usuario;
import com.unab.dunab.persistence.UsuarioDAO;

import java.util.UUID;

public class Autenticacion {

    private UsuarioDAO usuarioDAO;
    private Usuario usuarioActual;

    public Autenticacion() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Autenticacion(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public boolean iniciarSesion(String nombreUsuario, String contrasena) {
        if (nombreUsuario == null || contrasena == null || nombreUsuario.trim().isEmpty() || contrasena.trim().isEmpty()) {
            return false;
        }
        Usuario u = usuarioDAO.buscar(nombreUsuario.trim());
        if (u != null && contrasena.equals(u.getContrasena())) {
            this.usuarioActual = u;
            return true;
        }
        return false;
    }

    public boolean registrarUsuario(String nombreUsuario, String contrasena) {
        if (nombreUsuario == null || contrasena == null || nombreUsuario.trim().isEmpty() || contrasena.trim().isEmpty()) {
            return false;
        }

        String id = UUID.randomUUID().toString();
        Usuario nuevoUsuario = new Usuario(id, nombreUsuario.trim(), contrasena, nombreUsuario.trim(), "", "", 0.0);

        return usuarioDAO.guardar(nuevoUsuario);
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public Usuario obtenerUsuarioPorNombre(String nombreUsuario) {
        if (nombreUsuario == null) return null;
        return usuarioDAO.buscar(nombreUsuario);
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }
}
