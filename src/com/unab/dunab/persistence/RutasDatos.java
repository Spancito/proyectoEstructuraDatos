package com.unab.dunab.persistence;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

public final class RutasDatos {

    private static final String RUTA_RELATIVA_DATOS = "src/com/unab/dunab/data";
    private static final String[] MARCADORES_RAIZ = {
        ".git", "pom.xml", "build.gradle", "build.gradle.kts", "settings.gradle", "settings.gradle.kts"
    };

    private static volatile Path directorioDatos;

    private RutasDatos() {
    }

    public static Path obtenerDirectorioDatos() {
        Path actual = directorioDatos;
        if (actual == null) {
            synchronized (RutasDatos.class) {
                actual = directorioDatos;
                if (actual == null) {
                    actual = resolverDirectorioDatos();
                    directorioDatos = actual;
                }
            }
        }
        return actual;
    }

    public static File obtenerArchivo(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo de datos no puede estar vacío.");
        }

        Path directorio = obtenerDirectorioDatos();
        Path archivo = directorio.resolve(nombreArchivo).normalize();
        if (!archivo.startsWith(directorio)) {
            throw new IllegalArgumentException("El archivo de datos debe ubicarse dentro de " + directorio);
        }
        return archivo.toFile();
    }

    private static Path resolverDirectorioDatos() {
        for (Path inicio : obtenerPuntosDeInicio()) {
            Path encontrado = buscarDirectorioDatos(inicio);
            if (encontrado != null) {
                return encontrado;
            }
        }

        throw new IllegalStateException(
            "No se encontró el directorio oficial de datos '" + RUTA_RELATIVA_DATOS + "'. " +
            "Ejecute el proyecto desde su raíz o desde una subcarpeta del mismo."
        );
    }

    private static Set<Path> obtenerPuntosDeInicio() {
        Set<Path> puntos = new LinkedHashSet<>();

        String userDir = System.getProperty("user.dir");
        if (userDir != null && !userDir.trim().isEmpty()) {
            agregarSiDirectorio(puntos, Paths.get(userDir).toAbsolutePath().normalize());
        }

        try {
            URI ubicacion = RutasDatos.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            Path codeSource = Paths.get(ubicacion).toAbsolutePath().normalize();
            agregarSiDirectorio(puntos, Files.isDirectory(codeSource) ? codeSource : codeSource.getParent());
        } catch (Exception ignored) {
        }

        return puntos;
    }

    private static void agregarSiDirectorio(Set<Path> puntos, Path ruta) {
        if (ruta != null && Files.isDirectory(ruta)) {
            puntos.add(ruta);
        }
    }

    private static Path buscarDirectorioDatos(Path inicio) {
        Path actual = inicio;
        while (actual != null) {
            Path oficial = actual.resolve(RUTA_RELATIVA_DATOS).normalize();
            if (Files.isDirectory(oficial)) {
                return oficial;
            }

            if (esRaizProyecto(actual)) {
                Path candidato = actual.resolve(RUTA_RELATIVA_DATOS).normalize();
                if (Files.isDirectory(candidato)) {
                    return candidato;
                }
            }

            actual = actual.getParent();
        }
        return null;
    }

    private static boolean esRaizProyecto(Path directorio) {
        for (String marcador : MARCADORES_RAIZ) {
            if (Files.exists(directorio.resolve(marcador))) {
                return true;
            }
        }
        return false;
    }
}
