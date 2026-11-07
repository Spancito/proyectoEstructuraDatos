package com.unab.dunab.view;

import com.unab.dunab.controller.Autenticacion;
import com.unab.dunab.view.theme.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private Autenticacion controladorAutenticacion;

    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JButton botonAlternarTema;
    private JPanel panelPrincipal;
    private JPanel panelIzquierdo;
    private JPanel panelDerecho;
    private JPanel formulario;
    private JLabel titulo;
    private JLabel texto;
    private JLabel etiquetaUsuario;
    private JLabel etiquetaContrasena;
    private JLabel preguntaRegistro;
    private JButton botonIngresar;
    private JButton botonRegistro;

    public LoginFrame(Autenticacion controlador) {
        this.controladorAutenticacion = controlador;

        setTitle("DUNAB - Iniciar sesión");
        setSize(950, 600);
        setMinimumSize(new Dimension(800, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        crearInterfaz();
    }

    private void crearInterfaz() {
        panelPrincipal = new JPanel(new GridLayout(1, 2));
        panelPrincipal.setBackground(Tema.getColorDeFondo());

        panelIzquierdo = new JPanel(new GridBagLayout());
        panelIzquierdo.setOpaque(true);
        panelIzquierdo.setBackground(new Color(48, 32, 82));

        JPanel panelContenidoIzquierda = new JPanel();
        panelContenidoIzquierda.setLayout(new BoxLayout(panelContenidoIzquierda, BoxLayout.Y_AXIS));
        panelContenidoIzquierda.setOpaque(false);

        JLabel tituloLogo = new JLabel("DUNAB");
        tituloLogo.setFont(new Font("Arial", Font.BOLD, 46));
        tituloLogo.setForeground(Color.WHITE);
        tituloLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Tu espacio universitario");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descripcion = new JLabel(
            "<html><div style='text-align:center;'>"
            + "Organiza tu experiencia universitaria<br>"
            + "y administra tu información desde<br>"
            + "un solo lugar."
            + "</div></html>"
        );
        descripcion.setFont(new Font("Arial", Font.PLAIN, 15));
        descripcion.setForeground(new Color(230, 225, 240));
        descripcion.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelContenidoIzquierda.add(tituloLogo);
        panelContenidoIzquierda.add(Box.createVerticalStrut(15));
        panelContenidoIzquierda.add(subtitulo);
        panelContenidoIzquierda.add(Box.createVerticalStrut(30));
        panelContenidoIzquierda.add(descripcion);

        GridBagConstraints gbcIzq = new GridBagConstraints();
        gbcIzq.gridx = 0;
        gbcIzq.gridy = 0;
        gbcIzq.anchor = GridBagConstraints.CENTER;
        panelIzquierdo.add(panelContenidoIzquierda, gbcIzq);

        panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setOpaque(true);
        panelDerecho.setBackground(Tema.getColorDeFondo());

        formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBackground(Tema.obtenerColorPanel());
        formulario.setBorder(new EmptyBorder(15, 35, 15, 35));
        formulario.setPreferredSize(new Dimension(420, 470));

        JPanel contenedorBotonTema = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        contenedorBotonTema.setOpaque(false);
        contenedorBotonTema.setAlignmentX(Component.CENTER_ALIGNMENT);

        botonAlternarTema = new JButton(Tema.ModoOscuro ? "☀️ Modo Claro" : "🌙 Modo Oscuro");
        botonAlternarTema.setFont(new Font("Arial", Font.BOLD, 12));
        botonAlternarTema.setForeground(Tema.MORADO);
        botonAlternarTema.setBackground(Tema.ModoOscuro ? new Color(60, 60, 60) : new Color(240, 235, 250));
        botonAlternarTema.setFocusPainted(false);
        botonAlternarTema.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.MORADO, 1, true),
            new EmptyBorder(4, 8, 4, 8)
        ));
        botonAlternarTema.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonAlternarTema.setPreferredSize(new Dimension(130, 28));
        botonAlternarTema.addActionListener(e -> alternarTema());

        contenedorBotonTema.add(botonAlternarTema);

        titulo = new JLabel("¡Bienvenido!");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(Tema.MORADO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        texto = new JLabel("Inicia sesión en tu cuenta");
        texto.setFont(new Font("Arial", Font.PLAIN, 12));
        texto.setForeground(Color.GRAY);
        texto.setAlignmentX(Component.CENTER_ALIGNMENT);

        etiquetaUsuario = new JLabel("Usuario");
        etiquetaUsuario.setFont(new Font("Arial", Font.BOLD, 12));
        etiquetaUsuario.setForeground(Tema.obtenerColorTexto());
        etiquetaUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);

        campoUsuario = new JTextField();
        campoUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        campoUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        campoUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        campoUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.GRIS),
            new EmptyBorder(4, 8, 4, 8)
        ));

        etiquetaContrasena = new JLabel("Contraseña");
        etiquetaContrasena.setFont(new Font("Arial", Font.BOLD, 12));
        etiquetaContrasena.setForeground(Tema.obtenerColorTexto());
        etiquetaContrasena.setAlignmentX(Component.LEFT_ALIGNMENT);

        campoContrasena = new JPasswordField();
        campoContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        campoContrasena.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        campoContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);
        campoContrasena.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.GRIS),
            new EmptyBorder(4, 8, 4, 8)
        ));

        botonIngresar = new JButton("Iniciar sesión");
        botonIngresar.setFont(new Font("Arial", Font.BOLD, 14));
        botonIngresar.setBackground(Tema.NARANJA);
        botonIngresar.setForeground(Color.WHITE);
        botonIngresar.setFocusPainted(false);
        botonIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        botonIngresar.setPreferredSize(new Dimension(0, 38));
        botonIngresar.addActionListener(e -> iniciarSesion());

        preguntaRegistro = new JLabel("¿Todavía no tienes una cuenta?");
        preguntaRegistro.setFont(new Font("Arial", Font.PLAIN, 11));
        preguntaRegistro.setForeground(Color.GRAY);
        preguntaRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);

        botonRegistro = new JButton("Crear una cuenta");
        botonRegistro.setFont(new Font("Arial", Font.BOLD, 12));
        botonRegistro.setForeground(Tema.MORADO);
        botonRegistro.setBackground(Tema.obtenerColorPanel());
        botonRegistro.setBorderPainted(false);
        botonRegistro.setFocusPainted(false);
        botonRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonRegistro.addActionListener(e -> abrirRegistro());

        formulario.add(contenedorBotonTema);
        formulario.add(Box.createVerticalStrut(6));
        formulario.add(titulo);
        formulario.add(Box.createVerticalStrut(2));
        formulario.add(texto);
        formulario.add(Box.createVerticalStrut(10));

        formulario.add(etiquetaUsuario);
        formulario.add(Box.createVerticalStrut(2));
        formulario.add(campoUsuario);
        formulario.add(Box.createVerticalStrut(8));

        formulario.add(etiquetaContrasena);
        formulario.add(Box.createVerticalStrut(2));
        formulario.add(campoContrasena);
        formulario.add(Box.createVerticalStrut(12));

        formulario.add(botonIngresar);
        formulario.add(Box.createVerticalStrut(8));

        formulario.add(preguntaRegistro);
        formulario.add(Box.createVerticalStrut(2));
        formulario.add(botonRegistro);

        GridBagConstraints gbcDer = new GridBagConstraints();
        gbcDer.gridx = 0;
        gbcDer.gridy = 0;
        gbcDer.anchor = GridBagConstraints.CENTER;
        panelDerecho.add(formulario, gbcDer);

        panelPrincipal.add(panelIzquierdo);
        panelPrincipal.add(panelDerecho);

        add(panelPrincipal, BorderLayout.CENTER);

        getRootPane().setDefaultButton(botonIngresar);
    }

    private void alternarTema() {
        Tema.ModoOscuro = !Tema.ModoOscuro;
        actualizarEstilosTema();
        actualizarColores();
    }

    private void actualizarEstilosTema() {
        botonAlternarTema.setText(Tema.ModoOscuro ? "☀️ Modo Claro" : "🌙 Modo Oscuro");
        botonAlternarTema.setBackground(Tema.ModoOscuro ? new Color(60, 60, 60) : new Color(240, 235, 250));
        botonAlternarTema.setForeground(Tema.ModoOscuro ? Color.WHITE : Tema.MORADO);
    }

    private void actualizarColores() {
        panelPrincipal.setBackground(Tema.getColorDeFondo());
        panelDerecho.setBackground(Tema.getColorDeFondo());
        formulario.setBackground(Tema.obtenerColorPanel());
        
        etiquetaUsuario.setForeground(Tema.obtenerColorTexto());
        etiquetaContrasena.setForeground(Tema.obtenerColorTexto());
        
        botonRegistro.setBackground(Tema.obtenerColorPanel());
        
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void iniciarSesion() {
        String usuarioTexto = campoUsuario.getText().trim();
        String contrasenaTexto = new String(campoContrasena.getPassword());

        if (usuarioTexto.isEmpty() || contrasenaTexto.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor, completa todos los campos.",
                "Campos vacíos",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (controladorAutenticacion.iniciarSesion(usuarioTexto, contrasenaTexto)) {
            JOptionPane.showMessageDialog(
                this,
                "¡Bienvenido a DUNAB!",
                "Inicio de sesión exitoso",
                JOptionPane.INFORMATION_MESSAGE
            );

            try {
                DashboardFrame dashboard = new DashboardFrame(usuarioTexto);
                dashboard.setVisible(true);
                this.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    this,
                    "Error al abrir el Dashboard:\n" + ex.getMessage(),
                    "Error crítico",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } else {
            JOptionPane.showMessageDialog(
                this,
                "El usuario o la contraseña son incorrectos.\n(Asegúrate de registrarte primero si no tienes cuenta).",
                "Error de inicio de sesión",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void abrirRegistro() {
        RegisterFrame ventanaRegistro = new RegisterFrame(this, controladorAutenticacion);
        ventanaRegistro.setVisible(true);
    }
}