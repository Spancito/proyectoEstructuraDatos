package com.unab.dunab.view.theme;

import javax.swing.*;
import java.awt.*;

public class Tema {
    public static boolean ModoOscuro = false;

    // Colores UNAB
    public static final Color UNAB_NARANJA = new Color(233, 164, 39);
    public static final Color UNAB_MORADO = new Color(134, 0, 221);

    public static Color getColorDeFondo() {
        return ModoOscuro ? new Color(30, 30, 30) : new Color(245, 245, 250);
    }
    
    public static Color obtenerColorPanel() {
        return ModoOscuro ? new Color(45, 45, 45) : Color.WHITE;
    }

    public static Color obtenerColorTexto() {
        return ModoOscuro ? new Color(230, 230, 230) : new Color(50, 50, 50);
    }
    
    public static Color obtenerColorCabecera() {
        return ModoOscuro ? UNAB_MORADO : UNAB_NARANJA;
    }
    
    public static Color obtenerColorTextoCabecera() {
        return Color.WHITE; 
    }
    
    public static Color getColorDeBoton() {
        return ModoOscuro ? UNAB_MORADO : UNAB_NARANJA;
    }

    public static Color getColorDeBotonRegistro() {
        return ModoOscuro ? UNAB_NARANJA : UNAB_MORADO;
    }

    public static Color obtenerColorBotonSecundario() {
        return ModoOscuro ? new Color(70, 70, 70) : new Color(230, 230, 230);
    }

    public static void aplicarEstiloBoton(JButton boton, Color colorFondo, Color colorTexto) {
        if (boton == null) return;
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setBorderPainted(false);
        boton.setBackground(colorFondo);
        boton.setForeground(colorTexto);
    }
}
