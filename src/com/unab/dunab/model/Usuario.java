package com.unab.dunab.model;

import java.io.Serializable;
import java.util.UUID;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String usuario;
    private String contrasena;
    private String nombre;
    private String correo;
    private String carrera;
    private double presupuestoMensual;

    public Usuario() {
        this.id = UUID.randomUUID().toString();
    }

    public Usuario(String usuario, String contrasena) {
        this.id = UUID.randomUUID().toString();
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.nombre = "";
        this.correo = "";
        this.carrera = "";
        this.presupuestoMensual = 0.0;
    }

    public Usuario(String id, String usuario, String contrasena, String nombre, String correo, String carrera, double presupuestoMensual) {
        this.id = (id == null || id.trim().isEmpty()) ? UUID.randomUUID().toString() : id;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.correo = correo;
        this.carrera = carrera;
        this.presupuestoMensual = presupuestoMensual;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public double getPresupuestoMensual() {
        return presupuestoMensual;
    }

    public void setPresupuestoMensual(double presupuestoMensual) {
        this.presupuestoMensual = presupuestoMensual;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", usuario='" + usuario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", carrera='" + carrera + '\'' +
                ", presupuestoMensual=" + presupuestoMensual +
                '}';
    }
}
