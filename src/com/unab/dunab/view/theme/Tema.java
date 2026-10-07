package com.unab.dunab.view.theme;

import javax.swing.*;
import java.awt.*;

public class Tema {
    public static boolean ModoOscuro = false;

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
        return ModoOscuro ? new Color(20, 20, 20) : new Color(33, 150, 243);
    }
    
    public static Color obtenerColorTextoCabecera() {
        return Color.WHITE; 
    }
    
    public static Color getColorDeBoton() {
        return ModoOscuro ? new Color(0, 122, 204) : new Color(33, 150, 243);
    }

    public static Color obtenerColorBotonSecundario() {
        return ModoOscuro ? new Color(70, 70, 70) : new Color(230, 230, 230);
    }
}
