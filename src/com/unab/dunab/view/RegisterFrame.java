package com.unab.dunab.view;

import com.unab.dunab.controller.Autenticacion;
import com.unab.dunab.view.theme.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private Autenticacion controladorAutenticacion;
    private JFrame ventanaLogin;

    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JPasswordField campoConfirmarContrasena;

    public RegisterFrame(JFrame loginParent, Autenticacion controlador) {
        this.ventanaLogin = loginParent;
        this.controladorAutenticacion = controlador;

        setTitle("DUNAB - Crear nueva cuenta");
        setSize(450, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(loginParent);
        setLayout(new BorderLayout());

        crearInterfaz();
    }

    private void crearInterfaz() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(Tema.obtenerColorPanel());
        panelPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel titulo = new JLabel("Registro DUNAB");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(Tema.MORADO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Crea tu cuenta institucional");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitulo.setForeground(Color.GRAY);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel etiquetaUsuario = new JLabel("Nuevo Usuario");
        etiquetaUsuario.setFont(new Font("Arial", Font.BOLD, 13));
        etiquetaUsuario.setForeground(Tema.obtenerColorTexto());
        etiquetaUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);

        campoUsuario = new JTextField();
        campoUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        campoUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campoUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        campoUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.GRIS),
            new EmptyBorder(6, 10, 6, 10)
        ));

        JLabel etiquetaContrasena = new JLabel("Contraseña");
        etiquetaContrasena.setFont(new Font("Arial", Font.BOLD, 13));
        etiquetaContrasena.setForeground(Tema.obtenerColorTexto());
        etiquetaContrasena.setAlignmentX(Component.LEFT_ALIGNMENT);

        campoContrasena = new JPasswordField();
        campoContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        campoContrasena.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campoContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);
        campoContrasena.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.GRIS),
            new EmptyBorder(6, 10, 6, 10)
        ));

        JLabel etiquetaConfirmar = new JLabel("Confirmar Contraseña");
        etiquetaConfirmar.setFont(new Font("Arial", Font.BOLD, 13));
        etiquetaConfirmar.setForeground(Tema.obtenerColorTexto());
        etiquetaConfirmar.setAlignmentX(Component.LEFT_ALIGNMENT);

        campoConfirmarContrasena = new JPasswordField();
        campoConfirmarContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        campoConfirmarContrasena.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campoConfirmarContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);
        campoConfirmarContrasena.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.GRIS),
            new EmptyBorder(6, 10, 6, 10)
        ));

        JButton botonRegistrar = new JButton("Registrarse");
        botonRegistrar.setFont(new Font("Arial", Font.BOLD, 15));
        botonRegistrar.setBackground(Tema.NARANJA);
        botonRegistrar.setForeground(Color.WHITE);
        botonRegistrar.setFocusPainted(false);
        botonRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonRegistrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        botonRegistrar.addActionListener(e -> registrarUsuario());

        JButton botonVolver = new JButton("Volver al login");
        botonVolver.setFont(new Font("Arial", Font.PLAIN, 13));
        botonVolver.setForeground(Tema.MORADO);
        botonVolver.setBackground(Tema.obtenerColorPanel());
        botonVolver.setBorderPainted(false);
        botonVolver.setFocusPainted(false);
        botonVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        botonVolver.addActionListener(e -> dispose());

        panelPrincipal.add(titulo);
        panelPrincipal.add(Box.createVerticalStrut(5));
        panelPrincipal.add(subtitulo);
        panelPrincipal.add(Box.createVerticalStrut(25));

        panelPrincipal.add(etiquetaUsuario);
        panelPrincipal.add(Box.createVerticalStrut(5));
        panelPrincipal.add(campoUsuario);
        panelPrincipal.add(Box.createVerticalStrut(15));

        panelPrincipal.add(etiquetaContrasena);
        panelPrincipal.add(Box.createVerticalStrut(5));
        panelPrincipal.add(campoContrasena);
        panelPrincipal.add(Box.createVerticalStrut(15));

        panelPrincipal.add(etiquetaConfirmar);
        panelPrincipal.add(Box.createVerticalStrut(5));
        panelPrincipal.add(campoConfirmarContrasena);
        panelPrincipal.add(Box.createVerticalStrut(25));

        panelPrincipal.add(botonRegistrar);
        panelPrincipal.add(Box.createVerticalStrut(10));
        panelPrincipal.add(botonVolver);

        add(panelPrincipal, BorderLayout.CENTER);
    }

    private void registrarUsuario() {
        String usuario = campoUsuario.getText().trim();
        String pass1 = new String(campoContrasena.getPassword());
        String pass2 = new String(campoConfirmarContrasena.getPassword());

        if (usuario.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!pass1.equals(pass2)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean registrado = controladorAutenticacion.registrarUsuario(usuario, pass1);

            if (registrado) {
                JOptionPane.showMessageDialog(this, "Cuenta creada con éxito. Ahora puedes iniciar sesión.", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "El usuario ya existe o no se pudo registrar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error en el controlador:\n" + ex.getMessage(), "Excepción", JOptionPane.ERROR_MESSAGE);
        }
    }
}