package com.unab.dunab.controller;

import com.unab.dunab.model.Usuario;
import com.unab.dunab.persistence.UsuarioDAO;

import java.util.UUID;

public class Autenticacion {

    private UsuarioDAO usuarioDAO;

    public Autenticacion() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public boolean iniciarSesion(String usuario, String contrasena) {
        if (usuario == null || contrasena == null || usuario.isEmpty() || contrasena.isEmpty()) {
            return false;
        }
        Usuario u = usuarioDAO.buscar(usuario);
        return u != null && u.getContrasena().equals(contrasena);
    }

    public boolean registrarUsuario(String usuario, String contrasena) {
        if (usuario == null || contrasena == null || usuario.isEmpty() || contrasena.isEmpty()) {
            return false;
        }
        
        String id = UUID.randomUUID().toString();
        Usuario nuevoUsuario = new Usuario(id, usuario, contrasena, "", "", "", 0.0);
        
        return usuarioDAO.guardar(nuevoUsuario);
    }
}