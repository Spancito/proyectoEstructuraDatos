package com.unab.dunab.persistence;

import com.unab.dunab.model.Materia;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MateriaDAO {
    private File archivoDatos;
    private Map<String, Materia> baseDatosMaterias;

    public MateriaDAO() {
        this.baseDatosMaterias = new LinkedHashMap<>();
        this.archivoDatos = resolverArchivoDatos("materias.csv");
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
            System.err.println("Error al inicializar almacenamiento de materias: " + e.getMessage());
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
                Materia materia = deserializarMateria(linea);
                if (materia != null && materia.getId() != null) {
                    baseDatosMaterias.put(materia.getId(), materia);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo de materias: " + e.getMessage());
        }
    }

    private synchronized void sincronizarArchivo() {
        try (BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivoDatos), StandardCharsets.UTF_8))) {
            for (Materia m : baseDatosMaterias.values()) {
                escritor.write(serializarMateria(m));
                escritor.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar materias en archivo: " + e.getMessage());
        }
    }

    private String serializarMateria(Materia m) {
        return escapar(m.getId()) + "," +
                escapar(m.getUsuarioId()) + "," +
                escapar(m.getNombre()) + "," +
                m.getCreditos() + "," +
                m.getValorPorCredito() + "," +
                m.getSemestreIndex();
    }

    private Materia deserializarMateria(String linea) {
        String[] partes = linea.split(",", -1);
        if (partes.length < 6) {
            return null;
        }

        String id = desescapar(partes[0]);
        String usuarioId = desescapar(partes[1]);
        String nombre = desescapar(partes[2]);
        int creditos = 0;
        double valorPorCredito = 0.0;
        int semestreIndex = 1;

        try {
            creditos = Integer.parseInt(partes[3]);
            valorPorCredito = Double.parseDouble(partes[4]);
            semestreIndex = Integer.parseInt(partes[5]);
        } catch (NumberFormatException ignored) {}

        return new Materia(id, usuarioId, nombre, creditos, valorPorCredito, semestreIndex);
    }

    private String escapar(String valor) {
        if (valor == null) return "";
        return valor.replace("\\", "\\\\").replace(",", "\\,").replace("\n", "\\n");
    }

    private String desescapar(String valor) {
        if (valor == null) return "";
        return valor.replace("\\n", "\n").replace("\\,", ",").replace("\\\\", "\\");
    }

    public synchronized boolean guardar(Materia materia) {
        if (materia == null || materia.getId() == null) {
            return false;
        }
        baseDatosMaterias.put(materia.getId(), materia);
        sincronizarArchivo();
        return true;
    }

    public synchronized boolean actualizar(Materia materia) {
        if (materia == null || materia.getId() == null || !baseDatosMaterias.containsKey(materia.getId())) {
            return false;
        }
        baseDatosMaterias.put(materia.getId(), materia);
        sincronizarArchivo();
        return true;
    }

    public synchronized boolean eliminar(String id) {
        if (id == null || !baseDatosMaterias.containsKey(id)) {
            return false;
        }
        baseDatosMaterias.remove(id);
        sincronizarArchivo();
        return true;
    }

    public synchronized Materia buscarPorId(String id) {
        if (id == null) return null;
        return baseDatosMaterias.get(id);
    }

    public synchronized List<Materia> listarPorUsuario(String usuarioId) {
        List<Materia> lista = new ArrayList<>();
        if (usuarioId == null) return lista;
        for (Materia m : baseDatosMaterias.values()) {
            if (usuarioId.equals(m.getUsuarioId())) {
                lista.add(m);
            }
        }
        return lista;
    }

    public synchronized List<Materia> listarPorUsuarioYSemestre(String usuarioId, int semestreIndex) {
        List<Materia> lista = new ArrayList<>();
        if (usuarioId == null) return lista;
        for (Materia m : baseDatosMaterias.values()) {
            if (usuarioId.equals(m.getUsuarioId()) && m.getSemestreIndex() == semestreIndex) {
                lista.add(m);
            }
        }
        return lista;
    }

    public synchronized List<Materia> obtenerTodas() {
        return new ArrayList<>(baseDatosMaterias.values());
    }
}
