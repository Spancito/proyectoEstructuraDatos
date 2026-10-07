package com.unab.dunab;

import com.unab.dunab.controller.Autenticacion;
import com.unab.dunab.view.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception excepcion) {
            // Se ignora si falla
        }

        SwingUtilities.invokeLater(() -> {
            Autenticacion controladorAutenticacion = new Autenticacion();
            LoginFrame marcoLogin = new LoginFrame(controladorAutenticacion);
            marcoLogin.setVisible(true);
        });
    }
}
