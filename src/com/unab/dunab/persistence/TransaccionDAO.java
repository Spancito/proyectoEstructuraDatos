package com.unab.dunab.persistence;

import com.unab.dunab.model.Transaccion;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TransaccionDAO {
    private File archivoDatos;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private Map<String, Transaccion> baseDatosTransacciones;

    public TransaccionDAO() {
        this.baseDatosTransacciones = new LinkedHashMap<>();
        this.archivoDatos = resolverArchivoDatos("transacciones.csv");
        asegurarDirectorioYArchivo();
        cargarDesdeArchivo();
    }

    private File resolverArchivoDatos(String nombreArchivo) {
        File f1 = new File("data", nombreArchivo);
        if (f1.exists()) {
            return f1;
        }
        File f2 = new File("../../../data", nombreArchivo);
        if (f2.exists()) {
            return f2;
        }
        File f3 = new File("../../data", nombreArchivo);
        if (f3.exists()) {
            return f3;
        }
        File dirActual = new File(".").getAbsoluteFile();
        while (dirActual != null) {
            File carpetaData = new File(dirActual, "data");
            if (carpetaData.exists() && carpetaData.isDirectory()) {
                File candidato = new File(carpetaData, nombreArchivo);
                if (candidato.exists()) {
                    return candidato;
                }
            }
            dirActual = dirActual.getParentFile();
        }
        return f1;
    }

    private void asegurarDirectorioYArchivo() {
        try {
            File carpeta = archivoDatos.getParentFile();
            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }
            if (!archivoDatos.exists()) {
                archivoDatos.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error al inicializar almacenamiento de transacciones: " + e.getMessage());
        }
    }

    private synchronized void cargarDesdeArchivo() {
        if (!archivoDatos.exists() || archivoDatos.length() == 0) {
            return;
        }

        try (BufferedReader lector = new BufferedReader(new InputStreamReader(new FileInputStream(archivoDatos), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }
                Transaccion t = deserializarTransaccion(linea);
                if (t != null && t.getId() != null) {
                    baseDatosTransacciones.put(t.getId(), t);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo de transacciones: " + e.getMessage());
        }
    }

    private synchronized void sincronizarArchivo() {
        try (BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivoDatos), StandardCharsets.UTF_8))) {
            for (Transaccion t : baseDatosTransacciones.values()) {
                escritor.write(serializarTransaccion(t));
                escritor.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar transacciones en archivo: " + e.getMessage());
        }
    }

    private String serializarTransaccion(Transaccion t) {
        String fechaStr = (t.getFechaHora() != null) ? t.getFechaHora().format(FORMATO_FECHA) : LocalDateTime.now().format(FORMATO_FECHA);
        return escapar(t.getId()) + "," +
                escapar(t.getUsuarioId()) + "," +
                t.getMonto() + "," +
                escapar(t.getTipo()) + "," +
                escapar(t.getCategoria()) + "," +
                escapar(t.getDescripcion()) + "," +
                escapar(fechaStr) + "," +
                t.getSemestreIndex();
    }

    private Transaccion deserializarTransaccion(String linea) {
        String[] partes = linea.split(",", -1);
        if (partes.length < 8) {
            return null;
        }

        String id = desescapar(partes[0]);
        String usuarioId = desescapar(partes[1]);
        double monto = 0.0;
        try {
            monto = Double.parseDouble(partes[2]);
        } catch (NumberFormatException ignored) {}

        String tipo = desescapar(partes[3]);
        String categoria = desescapar(partes[4]);
        String descripcion = desescapar(partes[5]);
        LocalDateTime fechaHora = LocalDateTime.now();
        try {
            fechaHora = LocalDateTime.parse(desescapar(partes[6]), FORMATO_FECHA);
        } catch (Exception ignored) {}

        int semestreIndex = 1;
        try {
            semestreIndex = Integer.parseInt(partes[7]);
        } catch (NumberFormatException ignored) {}

        return new Transaccion(id, usuarioId, monto, tipo, categoria, descripcion, fechaHora, semestreIndex);
    }

    private String escapar(String valor) {
        if (valor == null) return "";
        return valor.replace("\\", "\\\\").replace(",", "\\,").replace("\n", "\\n");
    }

    private String desescapar(String valor) {
        if (valor == null) return "";
        return valor.replace("\\n", "\n").replace("\\,", ",").replace("\\\\", "\\");
    }

    public synchronized boolean guardar(Transaccion transaccion) {
        if (transaccion == null || transaccion.getId() == null) {
            return false;
        }
        baseDatosTransacciones.put(transaccion.getId(), transaccion);
        sincronizarArchivo();
        return true;
    }

    public synchronized boolean actualizar(Transaccion transaccion) {
        if (transaccion == null || transaccion.getId() == null || !baseDatosTransacciones.containsKey(transaccion.getId())) {
            return false;
        }
        baseDatosTransacciones.put(transaccion.getId(), transaccion);
        sincronizarArchivo();
        return true;
    }

    public synchronized boolean eliminar(String id) {
        if (id == null || !baseDatosTransacciones.containsKey(id)) {
            return false;
        }
        baseDatosTransacciones.remove(id);
        sincronizarArchivo();
        return true;
    }

    public synchronized Transaccion buscarPorId(String id) {
        if (id == null) return null;
        return baseDatosTransacciones.get(id);
    }

    public synchronized List<Transaccion> listarPorUsuario(String usuarioId) {
        List<Transaccion> resultado = new ArrayList<>();
        if (usuarioId == null) return resultado;
        for (Transaccion t : baseDatosTransacciones.values()) {
            if (usuarioId.equals(t.getUsuarioId())) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public synchronized List<Transaccion> obtenerUltimos20Movimientos(String usuarioId) {
        List<Transaccion> movimientos = listarPorUsuario(usuarioId);
        
        movimientos.sort((t1, t2) -> {
            if (t1.getFechaHora() == null && t2.getFechaHora() == null) return 0;
            if (t1.getFechaHora() == null) return 1;
            if (t2.getFechaHora() == null) return -1;
            return t2.getFechaHora().compareTo(t1.getFechaHora());
        });

        if (movimientos.size() > 20) {
            return new ArrayList<>(movimientos.subList(0, 20));
        }
        return movimientos;
    }

    public synchronized List<Transaccion> obtenerTodas() {
        return new ArrayList<>(baseDatosTransacciones.values());
    }
}
