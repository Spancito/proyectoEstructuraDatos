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
import java.time.YearMonth;
import java.util.Arrays;
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
        if (usuario == null || presupuestoMensual < 0) return false;

        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setCarrera(carrera);
        usuario.setPresupuestoMensual(presupuestoMensual);
        return usuarioDAO.actualizar(usuario);
    }

    public ResultadoTransferencia transferirPuntos(String remitenteIdentificador, String destinatarioIdentificador, double puntos, String concepto) {
        if (remitenteIdentificador == null || remitenteIdentificador.trim().isEmpty()) {
            return new ResultadoTransferencia(false, "No se pudo identificar al usuario que realiza la transferencia.", 0.0);
        }
        if (destinatarioIdentificador == null || destinatarioIdentificador.trim().isEmpty()) {
            return new ResultadoTransferencia(false, "Ingresa el usuario, correo o ID del destinatario.", 0.0);
        }
        if (Double.isNaN(puntos) || Double.isInfinite(puntos) || puntos <= 0) {
            return new ResultadoTransferencia(false, "La cantidad de puntos debe ser un número mayor que cero.", 0.0);
        }

        Usuario remitente = obtenerUsuario(remitenteIdentificador);
        if (remitente == null) {
            return new ResultadoTransferencia(false, "El usuario remitente no existe.", 0.0);
        }

        Usuario destinatario = obtenerUsuario(destinatarioIdentificador.trim());
        if (destinatario == null) {
            return new ResultadoTransferencia(false, "El destinatario no existe. Verifica el usuario, correo o ID.", remitente.getPuntosDunab());
        }
        if (remitente.getId().equals(destinatario.getId())) {
            return new ResultadoTransferencia(false, "No puedes transferir puntos a tu propia cuenta.", remitente.getPuntosDunab());
        }
        if (remitente.getPuntosDunab() < puntos) {
            return new ResultadoTransferencia(false,
                    "Saldo insuficiente. Disponible: " + String.format("%,.2f", remitente.getPuntosDunab()) + " puntos DUNAB.",
                    remitente.getPuntosDunab());
        }

        boolean puntosTransferidos = usuarioDAO.transferirPuntos(remitente.getId(), destinatario.getId(), puntos);
        if (!puntosTransferidos) {
            return new ResultadoTransferencia(false, "No se pudo guardar la transferencia en el archivo de usuarios.", remitente.getPuntosDunab());
        }

        String conceptoLimpio = (concepto == null || concepto.trim().isEmpty()) ? "Sin concepto" : concepto.trim();
        String nombreRemitente = (remitente.getNombre() == null || remitente.getNombre().trim().isEmpty())
                ? remitente.getUsuario() : remitente.getNombre();
        String nombreDestinatario = (destinatario.getNombre() == null || destinatario.getNombre().trim().isEmpty())
                ? destinatario.getUsuario() : destinatario.getNombre();
        LocalDateTime fechaHora = LocalDateTime.now();

        Transaccion envio = new Transaccion(remitente.getId(), puntos, "TRANSFERENCIA_ENVIADA", "Puntos DUNAB",
                "Envío a " + nombreDestinatario + " (" + destinatario.getUsuario() + "). Concepto: " + conceptoLimpio,
                fechaHora, 0);
        Transaccion recepcion = new Transaccion(destinatario.getId(), puntos, "TRANSFERENCIA_RECIBIDA", "Puntos DUNAB",
                "Recepción de " + nombreRemitente + " (" + remitente.getUsuario() + "). Concepto: " + conceptoLimpio,
                fechaHora, 0);

        boolean historialRegistrado = transaccionDAO.guardarTodas(Arrays.asList(envio, recepcion));
        if (!historialRegistrado) {
            usuarioDAO.transferirPuntos(destinatario.getId(), remitente.getId(), puntos);
            return new ResultadoTransferencia(false,
                    "No se pudo registrar el movimiento en el historial. La transferencia fue revertida.",
                    remitente.getPuntosDunab());
        }

        Usuario remitenteActualizado = usuarioDAO.buscarPorId(remitente.getId());
        double saldoActualizado = (remitenteActualizado != null) ? remitenteActualizado.getPuntosDunab() : remitente.getPuntosDunab();
        String mensaje = "Transferencia exitosa: " + String.format("%,.2f", puntos) + " puntos DUNAB enviados a " + nombreDestinatario + ".";
        mensaje += advertenciaPresupuesto(remitenteActualizado);
        return new ResultadoTransferencia(true, mensaje, saldoActualizado);
    }

    private String advertenciaPresupuesto(Usuario usuario) {
        if (usuario == null || usuario.getPresupuestoMensual() <= 0) {
            return "";
        }
        double gastosMesActual = calcularGastosMesActual(usuario.getId());
        if (gastosMesActual > usuario.getPresupuestoMensual()) {
            return String.format(
                    " Advertencia: tu presupuesto mensual ya está excedido (gastado: $ %,.2f de $ %,.2f).",
                    gastosMesActual, usuario.getPresupuestoMensual());
        }
        return "";
    }

    public boolean registrarTransaccion(String usuarioId, double monto, String tipo, String categoria, String descripcion, int semestreIndex) {
        return registrarTransaccionConValidacion(usuarioId, monto, tipo, categoria, descripcion, semestreIndex).isExitoso();
    }

    public ResultadoTransaccion registrarTransaccionConValidacion(String usuarioId, double monto, String tipo, String categoria, String descripcion, int semestreIndex) {
        if (usuarioId == null || tipo == null || monto <= 0) {
            return new ResultadoTransaccion(false, "Datos del movimiento inválidos.", 0.0, 0.0);
        }

        Usuario u = obtenerUsuario(usuarioId);
        String idReal = (u != null) ? u.getId() : usuarioId;
        double presupuesto = (u != null) ? u.getPresupuestoMensual() : 0.0;
        double gastosMesActual = calcularGastosMesActual(idReal);

        if ("GASTO".equalsIgnoreCase(tipo) && presupuesto > 0) {
            double nuevoTotalGastos = gastosMesActual + monto;
            if (nuevoTotalGastos > presupuesto) {
                double disponible = presupuesto - gastosMesActual;
                String mensaje = String.format(
                        "El gasto de $ %,.2f supera tu presupuesto mensual. Presupuesto: $ %,.2f. Gastado este mes: $ %,.2f. Disponible: $ %,.2f.",
                        monto, presupuesto, gastosMesActual, disponible);
                return new ResultadoTransaccion(false, mensaje, gastosMesActual, presupuesto);
            }
        }

        Transaccion t = new Transaccion(idReal, monto, tipo, categoria, descripcion, LocalDateTime.now(), semestreIndex);
        if (!transaccionDAO.guardar(t)) {
            return new ResultadoTransaccion(false, "No se pudo guardar el movimiento en el historial.", gastosMesActual, presupuesto);
        }

        double gastosActualizados = "GASTO".equalsIgnoreCase(tipo) ? gastosMesActual + monto : gastosMesActual;
        return new ResultadoTransaccion(true, "Movimiento registrado exitosamente.", gastosActualizados, presupuesto);
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

    public double calcularGastosMesActual(String usuarioId) {
        YearMonth mesActual = YearMonth.now();
        double total = 0.0;
        for (Transaccion t : obtenerTransaccionesUsuario(usuarioId)) {
            if ("GASTO".equalsIgnoreCase(t.getTipo()) && t.getFechaHora() != null
                    && YearMonth.from(t.getFechaHora()).equals(mesActual)) {
                total += t.getMonto();
            }
        }
        return total;
    }

    public double calcularPresupuestoDisponible(String usuarioId) {
        Usuario u = obtenerUsuario(usuarioId);
        if (u == null) return 0.0;
        return u.getPresupuestoMensual() - calcularGastosMesActual(usuarioId);
    }

    public double calcularPromedioMensualGastos(String usuarioId) {
        return calcularPromedioMensualGastos(usuarioId, 6);
    }

    public double calcularPromedioMensualGastos(String usuarioId, int meses) {
        if (meses <= 0) {
            meses = 6;
        }
        return calcularTotalGastos(usuarioId) / meses;
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

    public static class ResultadoTransaccion {
        private final boolean exitoso;
        private final String mensaje;
        private final double gastosMesActual;
        private final double presupuestoMensual;

        public ResultadoTransaccion(boolean exitoso, String mensaje, double gastosMesActual, double presupuestoMensual) {
            this.exitoso = exitoso;
            this.mensaje = mensaje;
            this.gastosMesActual = gastosMesActual;
            this.presupuestoMensual = presupuestoMensual;
        }

        public boolean isExitoso() {
            return exitoso;
        }

        public String getMensaje() {
            return mensaje;
        }

        public double getGastosMesActual() {
            return gastosMesActual;
        }

        public double getPresupuestoMensual() {
            return presupuestoMensual;
        }

        public double getDisponible() {
            return presupuestoMensual - gastosMesActual;
        }
    }

    public static class ResultadoTransferencia {
        private final boolean exitoso;
        private final String mensaje;
        private final double saldoRemitente;

        public ResultadoTransferencia(boolean exitoso, String mensaje, double saldoRemitente) {
            this.exitoso = exitoso;
            this.mensaje = mensaje;
            this.saldoRemitente = saldoRemitente;
        }

        public boolean isExitoso() {
            return exitoso;
        }

        public String getMensaje() {
            return mensaje;
        }

        public double getSaldoRemitente() {
            return saldoRemitente;
        }
    }
}
