package com.unab.dunab.view;

import com.unab.dunab.controller.Autenticacion;
import com.unab.dunab.view.theme.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    private Autenticacion controladorAutenticacion;
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    
    private JPanel panelPrincipal, panelFormulario, panelCentral, panelBotones, panelAlternadorTema;
    private JLabel etiquetaUsuario, etiquetaContrasena, etiquetaIniciarSesion;
    private JButton botonIniciarSesion, botonRegistrar, botonAlternarTema;

    public LoginFrame(Autenticacion controlador) {
        this.controladorAutenticacion = controlador;

        setTitle("DUNAB - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        panelPrincipal = new JPanel(new BorderLayout());

        panelCentral = new JPanel(new GridBagLayout());
        
        panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setPreferredSize(new Dimension(420, 480));

        etiquetaIniciarSesion = new JLabel("Iniciar Sesión");
        etiquetaIniciarSesion.setFont(new Font("Segoe UI", Font.BOLD, 24));
        etiquetaIniciarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFormulario.add(etiquetaIniciarSesion);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 40)));

        etiquetaUsuario = new JLabel("Nombre de Usuario");
        etiquetaUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        etiquetaUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFormulario.add(etiquetaUsuario);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 5)));

        campoUsuario = new JTextField();
        campoUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campoUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panelFormulario.add(campoUsuario);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 25)));

        etiquetaContrasena = new JLabel("Contraseña");
        etiquetaContrasena.setFont(new Font("Segoe UI", Font.BOLD, 14));
        etiquetaContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFormulario.add(etiquetaContrasena);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 5)));

        campoContrasena = new JPasswordField();
        campoContrasena.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campoContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panelFormulario.add(campoContrasena);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 40)));

        panelBotones = new JPanel(new GridLayout(2, 1, 0, 15));
        panelBotones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        botonIniciarSesion = new JButton("Iniciar Sesión");
        botonIniciarSesion.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botonIniciarSesion.setFocusPainted(false);
        botonIniciarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonIniciarSesion.addActionListener(e -> iniciarSesion());
        panelBotones.add(botonIniciarSesion);

        botonRegistrar = new JButton("Crear Cuenta Nueva");
        botonRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        botonRegistrar.setFocusPainted(false);
        botonRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonRegistrar.addActionListener(e -> abrirRegistro());
        panelBotones.add(botonRegistrar);

        panelFormulario.add(panelBotones);
        
        panelCentral.add(panelFormulario);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        botonAlternarTema = new JButton("Modo Oscuro");
        botonAlternarTema.setFont(new Font("Segoe UI", Font.BOLD, 13));
        botonAlternarTema.setFocusPainted(false);
        botonAlternarTema.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonAlternarTema.addActionListener(e -> alternarTema());
        
        panelAlternadorTema = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 15));
        panelAlternadorTema.setBorder(new EmptyBorder(10, 10, 30, 30));
        panelAlternadorTema.setOpaque(false);
        panelAlternadorTema.add(botonAlternarTema);
        
        panelPrincipal.add(panelAlternadorTema, BorderLayout.SOUTH);

        add(panelPrincipal);
        
        actualizarColores();
    }
    
    private void alternarTema() {
        Tema.ModoOscuro = !Tema.ModoOscuro;
        botonAlternarTema.setText(Tema.ModoOscuro ? "Modo Claro" : "Modo Oscuro");
        actualizarColores();
    }

    private void actualizarColores() {
        panelPrincipal.setBackground(Tema.getColorDeFondo());
        panelCentral.setBackground(Tema.getColorDeFondo());
        
        panelFormulario.setBackground(Tema.obtenerColorPanel());
        panelBotones.setBackground(Tema.obtenerColorPanel());
        
        etiquetaUsuario.setForeground(Tema.obtenerColorTexto());
        etiquetaContrasena.setForeground(Tema.obtenerColorTexto());
        etiquetaIniciarSesion.setForeground(Tema.obtenerColorTexto()); 
        
        campoUsuario.setBackground(Tema.getColorDeFondo());
        campoUsuario.setForeground(Tema.obtenerColorTexto());
        campoUsuario.setCaretColor(Tema.obtenerColorTexto());
        campoUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.ModoOscuro ? new Color(80, 80, 80) : new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        campoContrasena.setBackground(Tema.getColorDeFondo());
        campoContrasena.setForeground(Tema.obtenerColorTexto());
        campoContrasena.setCaretColor(Tema.obtenerColorTexto());
        campoContrasena.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.ModoOscuro ? new Color(80, 80, 80) : new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        botonIniciarSesion.setBackground(Tema.getColorDeBoton());
        botonIniciarSesion.setForeground(Color.WHITE);
        
        botonRegistrar.setBackground(Tema.obtenerColorBotonSecundario());
        botonRegistrar.setForeground(Tema.obtenerColorTexto());
        
        botonAlternarTema.setBackground(Tema.obtenerColorBotonSecundario());
        botonAlternarTema.setForeground(Tema.obtenerColorTexto());
        
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Tema.ModoOscuro ? new Color(60, 60, 60) : new Color(220, 220, 220), 1, true),
            new EmptyBorder(50, 60, 50, 60)
        ));
    }

    private void iniciarSesion() {
        String usuarioTexto = campoUsuario.getText();
        String contrasenaTexto = new String(campoContrasena.getPassword());

        if (controladorAutenticacion.iniciarSesion(usuarioTexto, contrasenaTexto)) {
            JOptionPane.showMessageDialog(this, 
                "¡Bienvenido de nuevo, " + usuarioTexto + "!", 
                "Acceso Concedido", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Usuario o contraseña incorrectos.", 
                "Error de Acceso", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirRegistro() {
        RegisterFrame ventanaRegistro = new RegisterFrame(this, controladorAutenticacion);
        ventanaRegistro.setVisible(true);
    }
}
