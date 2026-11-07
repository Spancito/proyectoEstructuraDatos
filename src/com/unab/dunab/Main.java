package com.unab.dunab;

import com.unab.dunab.controller.Autenticacion;
import com.unab.dunab.view.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Asegura que la interfaz gráfica se ejecute en el hilo seguro de Swing
        SwingUtilities.invokeLater(() -> {
            // Inicializa tu controlador de autenticación
            Autenticacion controlador = new Autenticacion();
            
            // Crea y muestra la ventana de inicio de sesión como la pantalla principal
            LoginFrame loginFrame = new LoginFrame(controlador);
            loginFrame.setVisible(true);
        });
    }
}