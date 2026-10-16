package com.unab.dunab.persistence;

import com.unab.dunab.model.Materia;
import com.unab.dunab.model.Transaccion;
import com.unab.dunab.model.Usuario;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportService {

    private UsuarioDAO usuarioDAO;
    private TransaccionDAO transaccionDAO;
    private MateriaDAO materiaDAO;

    public ExportService() {
        this.usuarioDAO = new UsuarioDAO();
        this.transaccionDAO = new TransaccionDAO();
        this.materiaDAO = new MateriaDAO();
    }

    public ExportService(UsuarioDAO usuarioDAO, TransaccionDAO transaccionDAO, MateriaDAO materiaDAO) {
        this.usuarioDAO = usuarioDAO;
        this.transaccionDAO = transaccionDAO;
        this.materiaDAO = materiaDAO;
    }

    //Exporta a Excel
    public boolean exportarAExcel(String usuarioId, String rutaArchivo) {
        if (usuarioId == null || rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);
        if (usuario == null) {
            usuario = usuarioDAO.buscar(usuarioId);
        }

        List<Transaccion> transacciones = transaccionDAO.listarPorUsuario(usuario != null ? usuario.getId() : usuarioId);
        List<Materia> materias = materiaDAO.listarPorUsuario(usuario != null ? usuario.getId() : usuarioId);

        File archivo = new File(rutaArchivo);
        File directorioPadre = archivo.getParentFile();
        if (directorioPadre != null && !directorioPadre.exists()) {
            directorioPadre.mkdirs();
        }

        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
            escritor.write('\ufeff');

            // Encabezado reporte
            escritor.write("REPORTE FINANCIERO DUNAB - EXPORTACIÓN GENERAL");
            escritor.newLine();
            escritor.write("Generado: " + java.time.LocalDateTime.now().format(formatoFecha));
            escritor.newLine();
            escritor.newLine();

            // Datos de Usuario
            escritor.write("=== INFORMACIÓN DEL ESTUDIANTE ===");
            escritor.newLine();
            escritor.write("ID,Usuario,Nombre,Correo,Carrera,Presupuesto Mensual");
            escritor.newLine();
            if (usuario != null) {
                escritor.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%.2f",
                        escaparCsv(usuario.getId()),
                        escaparCsv(usuario.getUsuario()),
                        escaparCsv(usuario.getNombre()),
                        escaparCsv(usuario.getCorreo()),
                        escaparCsv(usuario.getCarrera()),
                        usuario.getPresupuestoMensual()));
            } else {
                escritor.write(String.format("\"%s\",\"N/A\",\"N/A\",\"N/A\",\"N/A\",0.00", escaparCsv(usuarioId)));
            }
            escritor.newLine();
            escritor.newLine();

            // Materias
            escritor.write("=== MATERIAS INSCRITAS ===");
            escritor.newLine();
            escritor.write("ID Materia,Nombre Materia,Créditos,Valor Crédito,Semestre,Costo Total");
            escritor.newLine();
            double costoTotalAcademico = 0.0;
            for (Materia m : materias) {
                double costo = m.getCostoTotal();
                costoTotalAcademico += costo;
                escritor.write(String.format("\"%s\",\"%s\",%d,%.2f,%d,%.2f",
                        escaparCsv(m.getId()),
                        escaparCsv(m.getNombre()),
                        m.getCreditos(),
                        m.getValorPorCredito(),
                        m.getSemestreIndex(),
                        costo));
                escritor.newLine();
            }
            escritor.write(String.format(",,,,TOTAL MATERIAS,%.2f", costoTotalAcademico));
            escritor.newLine();
            escritor.newLine();

            // Transacciones
            escritor.write("=== HISTORIAL DE TRANSACCIONES ===");
            escritor.newLine();
            escritor.write("ID Transacción,Fecha y Hora,Tipo,Categoría,Descripción,Semestre,Monto");
            escritor.newLine();
            double totalIngresos = 0.0;
            double totalGastos = 0.0;

            for (Transaccion t : transacciones) {
                if ("INGRESO".equalsIgnoreCase(t.getTipo())) {
                    totalIngresos += t.getMonto();
                } else if ("GASTO".equalsIgnoreCase(t.getTipo())) {
                    totalGastos += t.getMonto();
                }

                String fechaStr = (t.getFechaHora() != null) ? t.getFechaHora().format(formatoFecha) : "";
                escritor.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%d,%.2f",
                        escaparCsv(t.getId()),
                        escaparCsv(fechaStr),
                        escaparCsv(t.getTipo()),
                        escaparCsv(t.getCategoria()),
                        escaparCsv(t.getDescripcion()),
                        t.getSemestreIndex(),
                        t.getMonto()));
                escritor.newLine();
            }

            escritor.newLine();
            escritor.write("=== BALANCE GENERAL ===");
            escritor.newLine();
            escritor.write(String.format("Total Ingresos,%.2f", totalIngresos));
            escritor.newLine();
            escritor.write(String.format("Total Gastos,%.2f", totalGastos));
            escritor.newLine();
            escritor.write(String.format("Saldo Neto,%.2f", (totalIngresos - totalGastos)));
            escritor.newLine();

            escritor.flush();
            return true;
        } catch (IOException e) {
            System.err.println("Error al exportar a Excel/CSV: " + e.getMessage());
            return false;
        }
    }

    // Exporta a PDF
    public boolean exportarAPDF(String usuarioId, String rutaArchivo) {
        if (usuarioId == null || rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);
        if (usuario == null) {
            usuario = usuarioDAO.buscar(usuarioId);
        }

        List<Transaccion> transacciones = transaccionDAO.listarPorUsuario(usuario != null ? usuario.getId() : usuarioId);
        List<Materia> materias = materiaDAO.listarPorUsuario(usuario != null ? usuario.getId() : usuarioId);

        File archivo = new File(rutaArchivo);
        File directorioPadre = archivo.getParentFile();
        if (directorioPadre != null && !directorioPadre.exists()) {
            directorioPadre.mkdirs();
        }

        StringBuilder streamContenido = new StringBuilder();
        streamContenido.append("BT\n");
        streamContenido.append("/F1 16 Tf\n");
        streamContenido.append("50 780 Td\n");
        streamContenido.append("(GESTION FINANCIERA DUNAB - INFORME OFICIAL) Tj\n");
        streamContenido.append("0 -24 Td\n");

        streamContenido.append("/F1 10 Tf\n");
        String fechaActual = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        streamContenido.append(String.format("(Fecha de Emision: %s) Tj\n", fechaActual));
        streamContenido.append("0 -18 Td\n");

        String nombreEstudiante = (usuario != null && usuario.getNombre() != null && !usuario.getNombre().isEmpty()) ? usuario.getNombre() : "Estudiante UNAB";
        String correoEstudiante = (usuario != null && usuario.getCorreo() != null) ? usuario.getCorreo() : "N/A";
        String carreraEstudiante = (usuario != null && usuario.getCarrera() != null) ? usuario.getCarrera() : "N/A";
        double presupuesto = (usuario != null) ? usuario.getPresupuestoMensual() : 0.0;

        streamContenido.append(String.format("(Estudiante: %s | Correo: %s) Tj\n", sanitizarPdf(nombreEstudiante), sanitizarPdf(correoEstudiante)));
        streamContenido.append("0 -14 Td\n");
        streamContenido.append(String.format("(Carrera: %s | Presupuesto Mensual: $%.2f) Tj\n", sanitizarPdf(carreraEstudiante), presupuesto));
        streamContenido.append("0 -24 Td\n");

        // Resumen
        streamContenido.append("/F1 12 Tf\n");
        streamContenido.append("(1. RESUMEN DE MATERIAS ACADEMICAS) Tj\n");
        streamContenido.append("0 -16 Td\n");
        streamContenido.append("/F1 9 Tf\n");

        double totalMaterias = 0.0;
        int limiteMaterias = Math.min(materias.size(), 10);
        for (int i = 0; i < limiteMaterias; i++) {
            Materia m = materias.get(i);
            totalMaterias += m.getCostoTotal();
            String lineaMateria = String.format("- %s | Creditos: %d | Val/Cred: $%.2f | Semestre: %d | Costo: $%.2f",
                    sanitizarPdf(m.getNombre()), m.getCreditos(), m.getValorPorCredito(), m.getSemestreIndex(), m.getCostoTotal());
            streamContenido.append(String.format("(%s) Tj\n", lineaMateria));
            streamContenido.append("0 -13 Td\n");
        }
        if (materias.isEmpty()) {
            streamContenido.append("(No se registran materias inscritas actualmente.) Tj\n");
            streamContenido.append("0 -13 Td\n");
        }
        streamContenido.append(String.format("(Costo Total Proyectado Materias: $%.2f) Tj\n", totalMaterias));
        streamContenido.append("0 -24 Td\n");

        // Transacciones
        streamContenido.append("/F1 12 Tf\n");
        streamContenido.append("(2. ULTIMOS MOVIMIENTOS Y TRANSACCIONES) Tj\n");
        streamContenido.append("0 -16 Td\n");
        streamContenido.append("/F1 9 Tf\n");

        double totalIng = 0;
        double totalGas = 0;
        int limiteTransacciones = Math.min(transacciones.size(), 15);
        for (int i = 0; i < limiteTransacciones; i++) {
            Transaccion t = transacciones.get(i);
            if ("INGRESO".equalsIgnoreCase(t.getTipo())) totalIng += t.getMonto();
            if ("GASTO".equalsIgnoreCase(t.getTipo())) totalGas += t.getMonto();

            String fechaStr = (t.getFechaHora() != null) ? t.getFechaHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
            String lineaTransaccion = String.format("[%s] %s - %s ($%.2f) | Cat: %s",
                    t.getTipo(), fechaStr, sanitizarPdf(t.getDescripcion()), t.getMonto(), sanitizarPdf(t.getCategoria()));
            streamContenido.append(String.format("(%s) Tj\n", lineaTransaccion));
            streamContenido.append("0 -13 Td\n");
        }
        if (transacciones.isEmpty()) {
            streamContenido.append("(No hay movimientos financieros registrados.) Tj\n");
            streamContenido.append("0 -13 Td\n");
        }

        streamContenido.append("0 -15 Td\n");
        streamContenido.append("/F1 11 Tf\n");
        streamContenido.append(String.format("(BALANCE: Ingresos: $%.2f | Gastos: $%.2f | Saldo Neto: $%.2f) Tj\n",
                totalIng, totalGas, (totalIng - totalGas)));
        streamContenido.append("ET\n");

        return escribirArchivoPdf(archivo, streamContenido.toString());
    }

    private boolean escribirArchivoPdf(File archivo, String contenidoStream) {
        try (FileOutputStream out = new FileOutputStream(archivo)) {
            byte[] streamBytes = contenidoStream.getBytes(StandardCharsets.US_ASCII);

            List<Long> offsets = new java.util.ArrayList<>();
            long bytesEscritos = 0;

            String encabezado = "%PDF-1.4\n";
            byte[] hBytes = encabezado.getBytes(StandardCharsets.US_ASCII);
            out.write(hBytes);
            bytesEscritos += hBytes.length;

            // Catalogo
            offsets.add(bytesEscritos);
            String obj1 = "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n";
            byte[] b1 = obj1.getBytes(StandardCharsets.US_ASCII);
            out.write(b1);
            bytesEscritos += b1.length;

            // Paginas
            offsets.add(bytesEscritos);
            String obj2 = "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n";
            byte[] b2 = obj2.getBytes(StandardCharsets.US_ASCII);
            out.write(b2);
            bytesEscritos += b2.length;

            // Pagina
            offsets.add(bytesEscritos);
            String obj3 = "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>\nendobj\n";
            byte[] b3 = obj3.getBytes(StandardCharsets.US_ASCII);
            out.write(b3);
            bytesEscritos += b3.length;

            // Contenidos
            offsets.add(bytesEscritos);
            String obj4Inicio = "4 0 obj\n<< /Length " + streamBytes.length + " >>\nstream\n";
            byte[] b4Inicio = obj4Inicio.getBytes(StandardCharsets.US_ASCII);
            out.write(b4Inicio);
            out.write(streamBytes);
            String obj4Fin = "\nendstream\nendobj\n";
            byte[] b4Fin = obj4Fin.getBytes(StandardCharsets.US_ASCII);
            out.write(b4Fin);
            bytesEscritos += b4Inicio.length + streamBytes.length + b4Fin.length;

            // Letra
            offsets.add(bytesEscritos);
            String obj5 = "5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n";
            byte[] b5 = obj5.getBytes(StandardCharsets.US_ASCII);
            out.write(b5);
            bytesEscritos += b5.length;

            // Tabla
            long xrefOffset = bytesEscritos;
            StringBuilder xref = new StringBuilder();
            xref.append("xref\n");
            xref.append("0 6\n");
            xref.append("0000000000 65535 f \n");
            for (Long offset : offsets) {
                xref.append(String.format("%010d 00000 n \n", offset));
            }
            xref.append("trailer\n");
            xref.append("<< /Size 6 /Root 1 0 R >>\n");
            xref.append("startxref\n");
            xref.append(xrefOffset).append("\n");
            xref.append("%%EOF\n");

            out.write(xref.toString().getBytes(StandardCharsets.US_ASCII));
            out.flush();
            return true;
        } catch (IOException e) {
            System.err.println("Error al escribir estructura PDF: " + e.getMessage());
            return false;
        }
    }

    private String escaparCsv(String texto) {
        if (texto == null) return "";
        return texto.replace("\"", "\"\"");
    }

    private String sanitizarPdf(String texto) {
        if (texto == null) return "";
        // solo texto para el pdf
        return texto.replace("\\", "\\\\")
                    .replace("(", "\\(")
                    .replace(")", "\\)")
                    .replaceAll("[^\\x20-\\x7E]", " ");
    }
}
