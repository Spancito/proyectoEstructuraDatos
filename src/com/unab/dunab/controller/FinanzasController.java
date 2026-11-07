package com.unab.dunab.controller;

import com.unab.dunab.model.Materia;
import com.unab.dunab.model.MetaAhorro;
import com.unab.dunab.model.Transaccion;
import com.unab.dunab.model.Usuario;
import com.unab.dunab.persistence.ExportService;
import com.unab.dunab.persistence.MateriaDAO;
import com.unab.dunab.persistence.MetaAhorroDAO;
import com.unab.dunab.persistence.TransaccionDAO;
import com.unab.dunab.persistence.UsuarioDAO;

import java.time.LocalDateTime;
import java.util.List;

public class FinanzasController {

    private UsuarioDAO usuarioDAO;
    private TransaccionDAO transaccionDAO;
    private MateriaDAO materiaDAO;
    private MetaAhorroDAO metaAhorroDAO;
    private ExportService exportService;

    public FinanzasController() {
        this.usuarioDAO = new UsuarioDAO();
        this.transaccionDAO = new TransaccionDAO();
        this.materiaDAO = new MateriaDAO();
        this.metaAhorroDAO = new MetaAhorroDAO();
        this.exportService = new ExportService(this.usuarioDAO, this.transaccionDAO, this.materiaDAO, this.metaAhorroDAO);
    }

    public Usuario obtenerUsuario(String identificador) {
        if (identificador == null) return null;
        Usuario u = usuarioDAO.buscar(identificador);
        if (u == null) {
            u = usuarioDAO.buscarPorId(identificador);
        }
        return u;
    }

    public boolean actualizarPerfilUsuario(String usuarioId, String nombre, String correo, String carrera, double presupuestoMensual) {
        Usuario usuario = obtenerUsuario(usuarioId);
        if (usuario == null) return false;

        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setCarrera(carrera);
        usuario.setPresupuestoMensual(presupuestoMensual);
        return usuarioDAO.actualizar(usuario);
    }

    public boolean registrarTransaccion(String usuarioId, double monto, String tipo, String categoria, String descripcion, int semestreIndex) {
        if (usuarioId == null || tipo == null || monto <= 0) {
            return false;
        }
        Usuario u = obtenerUsuario(usuarioId);
        String idReal = (u != null) ? u.getId() : usuarioId;

        Transaccion t = new Transaccion(idReal, monto, tipo, categoria, descripcion, LocalDateTime.now(), semestreIndex);
        return transaccionDAO.guardar(t);
    }

    public boolean eliminarTransaccion(String transaccionId) {
        return transaccionDAO.eliminar(transaccionId);
    }

    public List<Transaccion> obtenerTransaccionesUsuario(String usuarioId) {
        Usuario u = obtenerUsuario(usuarioId);
        String idReal = (u != null) ? u.getId() : usuarioId;
        return transaccionDAO.listarPorUsuario(idReal);
    }

    public List<Transaccion> obtenerUltimasTransacciones(String usuarioId) {
        Usuario u = obtenerUsuario(usuarioId);
        String idReal = (u != null) ? u.getId() : usuarioId;
        return transaccionDAO.obtenerUltimos20Movimientos(idReal);
    }

    public double calcularTotalIngresos(String usuarioId) {
        double total = 0.0;
        for (Transaccion t : obtenerTransaccionesUsuario(usuarioId)) {
            if ("INGRESO".equalsIgnoreCase(t.getTipo())) {
                total += t.getMonto();
            }
        }
        return total;
    }

    public double calcularTotalGastos(String usuarioId) {
        double total = 0.0;
        for (Transaccion t : obtenerTransaccionesUsuario(usuarioId)) {
            if ("GASTO".equalsIgnoreCase(t.getTipo())) {
                total += t.getMonto();
            }
        }
        return total;
    }

    public double calcularSaldoNeto(String usuarioId) {
        return calcularTotalIngresos(usuarioId) - calcularTotalGastos(usuarioId);
    }

    public double calcularPromedioMensualGastos(String usuarioId) {
        double gastos = calcularTotalGastos(usuarioId);
        return gastos / 6.0;
    }

    public boolean agregarMateria(String usuarioId, String nombre, int creditos, double valorPorCredito, int semestreIndex) {
        if (usuarioId == null || nombre == null || nombre.trim().isEmpty() || creditos <= 0 || valorPorCredito < 0) {
            return false;
        }
        Usuario u = obtenerUsuario(usuarioId);
        String idReal = (u != null) ? u.getId() : usuarioId;

        Materia m = new Materia(idReal, nombre.trim(), creditos, valorPorCredito, semestreIndex);
        return materiaDAO.guardar(m);
    }

    public boolean eliminarMateria(String materiaId) {
        return materiaDAO.eliminar(materiaId);
    }

    public List<Materia> obtenerMateriasUsuario(String usuarioId) {
        Usuario u = obtenerUsuario(usuarioId);
        String idReal = (u != null) ? u.getId() : usuarioId;
        return materiaDAO.listarPorUsuario(idReal);
    }

    public int calcularTotalCreditos(String usuarioId) {
        int total = 0;
        for (Materia m : obtenerMateriasUsuario(usuarioId)) {
            total += m.getCreditos();
        }
        return total;
    }

    public double calcularCostoTotalAcademico(String usuarioId) {
        double total = 0.0;
        for (Materia m : obtenerMateriasUsuario(usuarioId)) {
            total += m.getCostoTotal();
        }
        return total;
    }

    public boolean agregarMetaAhorro(String usuarioId, String nombreMeta, double valorObjetivo, double valorActual) {
        if (usuarioId == null || nombreMeta == null || nombreMeta.trim().isEmpty() || valorObjetivo <= 0) {
            return false;
        }
        Usuario u = obtenerUsuario(usuarioId);
        String idReal = (u != null) ? u.getId() : usuarioId;

        MetaAhorro meta = new MetaAhorro(idReal, nombreMeta.trim(), valorObjetivo, valorActual);
        return metaAhorroDAO.guardar(meta);
    }

    public boolean actualizarAvanceMeta(String metaId, double nuevoValorActual) {
        MetaAhorro meta = metaAhorroDAO.buscarPorId(metaId);
        if (meta == null) return false;
        meta.setValorActual(nuevoValorActual);
        return metaAhorroDAO.actualizar(meta);
    }

    public boolean eliminarMetaAhorro(String metaId) {
        return metaAhorroDAO.eliminar(metaId);
    }

    public List<MetaAhorro> obtenerMetasUsuario(String usuarioId) {
        Usuario u = obtenerUsuario(usuarioId);
        String idReal = (u != null) ? u.getId() : usuarioId;
        return metaAhorroDAO.listarPorUsuario(idReal);
    }

    public boolean exportarReporteExcel(String usuarioId, String rutaDestino) {
        Usuario u = obtenerUsuario(usuarioId);
        String idReal = (u != null) ? u.getId() : usuarioId;
        return exportService.exportarAExcel(idReal, rutaDestino);
    }

    public boolean exportarReportePDF(String usuarioId, String rutaDestino) {
        Usuario u = obtenerUsuario(usuarioId);
        String idReal = (u != null) ? u.getId() : usuarioId;
        return exportService.exportarAPDF(idReal, rutaDestino);
    }
}
