package com.unab.dunab;

import com.unab.dunab.controller.Autenticacion;
import com.unab.dunab.view.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            Autenticacion controlador = new Autenticacion();
            LoginFrame loginFrame = new LoginFrame(controlador);
            loginFrame.setVisible(true);
        });
    }
}
