package com.unab.dunab.persistence;

import com.unab.dunab.model.Usuario;
import java.util.HashMap;

public class UsuarioDAO {
    private HashMap<String, Usuario> baseDatosUsuarios;

    public UsuarioDAO() {
        baseDatosUsuarios = new HashMap<>();
    }

    public boolean guardar(Usuario usuario) {
        if (baseDatosUsuarios.containsKey(usuario.getUsuario())) {
            return false; // Ya existe
        }
        baseDatosUsuarios.put(usuario.getUsuario(), usuario);
        return true;
    }

    public Usuario buscar(String nombreUsuario) {
        return baseDatosUsuarios.get(nombreUsuario);
    }
}
