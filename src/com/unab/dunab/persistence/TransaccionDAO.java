package com.unab.dunab.persistence;

import com.unab.dunab.model.Transaccion;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TransaccionDAO {
    private File archivoDatos;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private Map<String, Transaccion> baseDatosTransacciones;

    public TransaccionDAO() {
        this.baseDatosTransacciones = new LinkedHashMap<>();
        this.archivoDatos = RutasDatos.obtenerArchivo("transacciones.csv");
        asegurarDirectorioYArchivo();
        cargarDesdeArchivo();
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

    private synchronized boolean sincronizarArchivo() {
        File carpeta = archivoDatos.getAbsoluteFile().getParentFile();
        File temporal = null;
        try {
            if (carpeta != null && !carpeta.exists()) {
                Files.createDirectories(carpeta.toPath());
            }
            temporal = File.createTempFile(archivoDatos.getName() + ".", ".tmp", carpeta);
            try (BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temporal), StandardCharsets.UTF_8))) {
                for (Transaccion t : baseDatosTransacciones.values()) {
                    escritor.write(serializarTransaccion(t));
                    escritor.newLine();
                }
                escritor.flush();
            }
            try {
                Files.move(temporal.toPath(), archivoDatos.toPath(),
                        StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporal.toPath(), archivoDatos.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            temporal = null;
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar transacciones en archivo: " + e.getMessage());
            return false;
        } finally {
            if (temporal != null && temporal.exists()) {
                temporal.delete();
            }
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
        Transaccion anterior = baseDatosTransacciones.put(transaccion.getId(), transaccion);
        if (!sincronizarArchivo()) {
            restaurar(transaccion.getId(), anterior);
            return false;
        }
        return true;
    }

    public synchronized boolean guardarTodas(List<Transaccion> transacciones) {
        if (transacciones == null || transacciones.isEmpty()) {
            return false;
        }

        Map<String, Transaccion> anteriores = new LinkedHashMap<>();
        for (Transaccion transaccion : transacciones) {
            if (transaccion == null || transaccion.getId() == null) {
                for (Map.Entry<String, Transaccion> entry : anteriores.entrySet()) {
                    restaurar(entry.getKey(), entry.getValue());
                }
                return false;
            }
            anteriores.put(transaccion.getId(), baseDatosTransacciones.put(transaccion.getId(), transaccion));
        }

        if (!sincronizarArchivo()) {
            for (Map.Entry<String, Transaccion> entry : anteriores.entrySet()) {
                restaurar(entry.getKey(), entry.getValue());
            }
            return false;
        }
        return true;
    }

    public synchronized boolean actualizar(Transaccion transaccion) {
        if (transaccion == null || transaccion.getId() == null || !baseDatosTransacciones.containsKey(transaccion.getId())) {
            return false;
        }
        Transaccion anterior = baseDatosTransacciones.put(transaccion.getId(), transaccion);
        if (!sincronizarArchivo()) {
            restaurar(transaccion.getId(), anterior);
            return false;
        }
        return true;
    }

    public synchronized boolean eliminar(String id) {
        if (id == null || !baseDatosTransacciones.containsKey(id)) {
            return false;
        }
        Transaccion anterior = baseDatosTransacciones.remove(id);
        if (!sincronizarArchivo()) {
            if (anterior != null) {
                baseDatosTransacciones.put(id, anterior);
            }
            return false;
        }
        return true;
    }

    private void restaurar(String id, Transaccion anterior) {
        if (anterior == null) {
            baseDatosTransacciones.remove(id);
        } else {
            baseDatosTransacciones.put(id, anterior);
        }
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
