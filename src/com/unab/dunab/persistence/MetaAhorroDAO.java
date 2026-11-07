package com.unab.dunab.persistence;

import com.unab.dunab.model.MetaAhorro;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MetaAhorroDAO {
    private File archivoDatos;
    private Map<String, MetaAhorro> baseDatosMetas;

    public MetaAhorroDAO() {
        this.baseDatosMetas = new LinkedHashMap<>();
        this.archivoDatos = resolverArchivoDatos("metas.csv");
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
            System.err.println("Error al inicializar almacenamiento de metas: " + e.getMessage());
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
                MetaAhorro meta = deserializarMeta(linea);
                if (meta != null && meta.getId() != null) {
                    baseDatosMetas.put(meta.getId(), meta);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo de metas: " + e.getMessage());
        }
    }

    private synchronized void sincronizarArchivo() {
        try (BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivoDatos), StandardCharsets.UTF_8))) {
            for (MetaAhorro m : baseDatosMetas.values()) {
                escritor.write(serializarMeta(m));
                escritor.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar metas en archivo: " + e.getMessage());
        }
    }

    private String serializarMeta(MetaAhorro m) {
        return escapar(m.getId()) + "," +
                escapar(m.getUsuarioId()) + "," +
                escapar(m.getNombreMeta()) + "," +
                m.getValorObjetivo() + "," +
                m.getValorActual();
    }

    private MetaAhorro deserializarMeta(String linea) {
        String[] partes = linea.split(",", -1);
        if (partes.length < 5) {
            return null;
        }

        String id = desescapar(partes[0]);
        String usuarioId = desescapar(partes[1]);
        String nombreMeta = desescapar(partes[2]);
        double valorObjetivo = 0.0;
        double valorActual = 0.0;

        try {
            valorObjetivo = Double.parseDouble(partes[3]);
            valorActual = Double.parseDouble(partes[4]);
        } catch (NumberFormatException ignored) {}

        return new MetaAhorro(id, usuarioId, nombreMeta, valorObjetivo, valorActual);
    }

    private String escapar(String valor) {
        if (valor == null) return "";
        return valor.replace("\\", "\\\\").replace(",", "\\,").replace("\n", "\\n");
    }

    private String desescapar(String valor) {
        if (valor == null) return "";
        return valor.replace("\\n", "\n").replace("\\,", ",").replace("\\\\", "\\");
    }

    public synchronized boolean guardar(MetaAhorro meta) {
        if (meta == null || meta.getId() == null) {
            return false;
        }
        baseDatosMetas.put(meta.getId(), meta);
        sincronizarArchivo();
        return true;
    }

    public synchronized boolean actualizar(MetaAhorro meta) {
        if (meta == null || meta.getId() == null || !baseDatosMetas.containsKey(meta.getId())) {
            return false;
        }
        baseDatosMetas.put(meta.getId(), meta);
        sincronizarArchivo();
        return true;
    }

    public synchronized boolean eliminar(String id) {
        if (id == null || !baseDatosMetas.containsKey(id)) {
            return false;
        }
        baseDatosMetas.remove(id);
        sincronizarArchivo();
        return true;
    }

    public synchronized MetaAhorro buscarPorId(String id) {
        if (id == null) return null;
        return baseDatosMetas.get(id);
    }

    public synchronized List<MetaAhorro> listarPorUsuario(String usuarioId) {
        List<MetaAhorro> lista = new ArrayList<>();
        if (usuarioId == null) return lista;
        for (MetaAhorro m : baseDatosMetas.values()) {
            if (usuarioId.equals(m.getUsuarioId())) {
                lista.add(m);
            }
        }
        return lista;
    }

    public synchronized List<MetaAhorro> obtenerTodas() {
        return new ArrayList<>(baseDatosMetas.values());
    }
}
