package com.unab.dunab.controller;

import com.unab.dunab.model.Usuario;
import com.unab.dunab.persistence.UsuarioDAO;

public class Autenticacion {
    private UsuarioDAO usuarioDAO;

    public Autenticacion() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public boolean registrar(String nombreUsuario, String contrasena) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty()) {
            return false;
        }
        Usuario nuevoUsuario = new Usuario(nombreUsuario, contrasena);
        return usuarioDAO.guardar(nuevoUsuario);
    }

    public boolean iniciarSesion(String nombreUsuario, String contrasena) {
        Usuario usuario = usuarioDAO.buscar(nombreUsuario);
        if (usuario != null) {
            return usuario.getContrasena().equals(contrasena);
        }
        return false;
    }
}
