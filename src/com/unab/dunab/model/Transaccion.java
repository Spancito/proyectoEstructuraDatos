package com.unab.dunab.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaccion implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String usuarioId;
    private double monto;
    private String tipo;
    private String categoria;
    private String descripcion;
    private LocalDateTime fechaHora;
    private int semestreIndex;

    public Transaccion() {
        this.id = UUID.randomUUID().toString();
        this.fechaHora = LocalDateTime.now();
    }

    public Transaccion(String usuarioId, double monto, String tipo, String categoria, String descripcion, LocalDateTime fechaHora, int semestreIndex) {
        this(UUID.randomUUID().toString(), usuarioId, monto, tipo, categoria, descripcion, fechaHora, semestreIndex);
    }

    public Transaccion(String id, String usuarioId, double monto, String tipo, String categoria, String descripcion, LocalDateTime fechaHora, int semestreIndex) {
        this.id = (id == null || id.trim().isEmpty()) ? UUID.randomUUID().toString() : id;
        this.usuarioId = usuarioId;
        this.monto = monto;
        this.tipo = tipo;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.fechaHora = (fechaHora == null) ? LocalDateTime.now() : fechaHora;
        this.semestreIndex = semestreIndex;
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

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getSemestreIndex() {
        return semestreIndex;
    }

    public void setSemestreIndex(int semestreIndex) {
        this.semestreIndex = semestreIndex;
    }

    @Override
    public String toString() {
        return "Transaccion{" +
                "id='" + id + '\'' +
                ", usuarioId='" + usuarioId + '\'' +
                ", monto=" + monto +
                ", tipo='" + tipo + '\'' +
                ", categoria='" + categoria + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaHora=" + fechaHora +
                ", semestreIndex=" + semestreIndex +
                '}';
    }
}
