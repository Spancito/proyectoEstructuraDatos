package com.unab.dunab.view.theme;

import java.awt.Color;

public class Tema {
    public static boolean ModoOscuro = false;

    // Colores institucionales base
    public static final Color MORADO = new Color(48, 32, 82);
    public static final Color NARANJA = new Color(255, 140, 0);
    public static final Color GRIS = new Color(200, 200, 200);
    public static final Color TEXTO_OSCURO = new Color(40, 40, 40);
    public static final Color TEXTO_CLARO = new Color(230, 230, 230);

    public static Color getColorDeFondo() {
        return ModoOscuro ? new Color(25, 20, 35) : new Color(245, 245, 250);
    }

    public static Color obtenerColorPanel() {
        return ModoOscuro ? new Color(38, 30, 55) : Color.WHITE;
    }

    public static Color obtenerColorTexto() {
        return ModoOscuro ? TEXTO_CLARO : TEXTO_OSCURO;
    }
}