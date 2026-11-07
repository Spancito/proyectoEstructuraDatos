package com.unab.dunab.model;

import java.io.Serializable;
import java.util.UUID;

public class Materia implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String usuarioId;
    private String nombre;
    private int creditos;
    private double valorPorCredito;
    private int semestreIndex;

    public Materia() {
        this.id = UUID.randomUUID().toString();
    }

    public Materia(String usuarioId, String nombre, int creditos, double valorPorCredito, int semestreIndex) {
        this(UUID.randomUUID().toString(), usuarioId, nombre, creditos, valorPorCredito, semestreIndex);
    }

    public Materia(String id, String usuarioId, String nombre, int creditos, double valorPorCredito, int semestreIndex) {
        this.id = (id == null || id.trim().isEmpty()) ? UUID.randomUUID().toString() : id;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.creditos = creditos;
        this.valorPorCredito = valorPorCredito;
        this.semestreIndex = semestreIndex;
    }

    public double getCostoTotal() {
        return this.creditos * this.valorPorCredito;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public double getValorPorCredito() {
        return valorPorCredito;
    }

    public void setValorPorCredito(double valorPorCredito) {
        this.valorPorCredito = valorPorCredito;
    }

    public int getSemestreIndex() {
        return semestreIndex;
    }

    public void setSemestreIndex(int semestreIndex) {
        this.semestreIndex = semestreIndex;
    }

    @Override
    public String toString() {
        return "Materia{" +
                "id='" + id + '\'' +
                ", usuarioId='" + usuarioId + '\'' +
                ", nombre='" + nombre + '\'' +
                ", creditos=" + creditos +
                ", valorPorCredito=" + valorPorCredito +
                ", semestreIndex=" + semestreIndex +
                ", costoTotal=" + getCostoTotal() +
                '}';
    }
}
