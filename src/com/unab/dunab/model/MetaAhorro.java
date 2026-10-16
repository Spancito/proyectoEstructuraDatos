package com.unab.dunab.model;

import java.io.Serializable;
import java.util.UUID;

public class MetaAhorro implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String usuarioId;
    private String nombreMeta;
    private double valorObjetivo;
    private double valorActual;

    public MetaAhorro() {
        this.id = UUID.randomUUID().toString();
    }

    public MetaAhorro(String usuarioId, String nombreMeta, double valorObjetivo, double valorActual) {
        this(UUID.randomUUID().toString(), usuarioId, nombreMeta, valorObjetivo, valorActual);
    }

    public MetaAhorro(String id, String usuarioId, String nombreMeta, double valorObjetivo, double valorActual) {
        this.id = (id == null || id.trim().isEmpty()) ? UUID.randomUUID().toString() : id;
        this.usuarioId = usuarioId;
        this.nombreMeta = nombreMeta;
        this.valorObjetivo = valorObjetivo;
        this.valorActual = valorActual;
    }

    public double getProgresoPorcentaje() {
        if (this.valorObjetivo <= 0) return 0.0;
        return Math.min(100.0, (this.valorActual / this.valorObjetivo) * 100.0);
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

    public String getNombreMeta() {
        return nombreMeta;
    }

    public void setNombreMeta(String nombreMeta) {
        this.nombreMeta = nombreMeta;
    }

    public double getValorObjetivo() {
        return valorObjetivo;
    }

    public void setValorObjetivo(double valorObjetivo) {
        this.valorObjetivo = valorObjetivo;
    }

    public double getValorActual() {
        return valorActual;
    }

    public void setValorActual(double valorActual) {
        this.valorActual = valorActual;
    }

    @Override
    public String toString() {
        return "MetaAhorro{" +
                "id='" + id + '\'' +
                ", usuarioId='" + usuarioId + '\'' +
                ", nombreMeta='" + nombreMeta + '\'' +
                ", valorObjetivo=" + valorObjetivo +
                ", valorActual=" + valorActual +
                '}';
    }
}
