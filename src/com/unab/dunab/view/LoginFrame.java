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
        setSize(960, 620);
        setMinimumSize(new Dimension(860, 560));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        crearInterfaz();
        actualizarColores();
    }

    private void crearInterfaz() {
        panelPrincipal = new JPanel(new GridLayout(1, 2));

        panelIzquierdo = new JPanel(new GridBagLayout());
        panelIzquierdo.setOpaque(true);
        panelIzquierdo.setBackground(Tema.MORADO_UNAB);

        JPanel panelContenidoIzquierda = new JPanel();
        panelContenidoIzquierda.setLayout(new BoxLayout(panelContenidoIzquierda, BoxLayout.Y_AXIS));
        panelContenidoIzquierda.setOpaque(false);

        JLabel etiquetaBadge = new JLabel("UNIVERSIDAD AUTÓNOMA DE BUCARAMANGA");
        etiquetaBadge.setFont(new Font("SansSerif", Font.BOLD, 10));
        etiquetaBadge.setForeground(Tema.NARANJA_UNAB);
        etiquetaBadge.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tituloLogo = new JLabel("DUNAB");
        tituloLogo.setFont(new Font("SansSerif", Font.BOLD, 52));
        tituloLogo.setForeground(Color.WHITE);
        tituloLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Gestión Financiera Universitaria");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitulo.setForeground(new Color(230, 220, 240));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descripcion = new JLabel(
            "<html><div style='text-align:center;'>"
            + "Organiza tu presupuesto, controla<br>"
            + "tus materias y alcanza tus metas de<br>"
            + "ahorro desde un solo lugar."
            + "</div></html>"
        );
        descripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descripcion.setForeground(new Color(210, 200, 225));
        descripcion.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelContenidoIzquierda.add(etiquetaBadge);
        panelContenidoIzquierda.add(Box.createVerticalStrut(10));
        panelContenidoIzquierda.add(tituloLogo);
        panelContenidoIzquierda.add(Box.createVerticalStrut(8));
        panelContenidoIzquierda.add(subtitulo);
        panelContenidoIzquierda.add(Box.createVerticalStrut(25));
        panelContenidoIzquierda.add(descripcion);

        GridBagConstraints gbcIzq = new GridBagConstraints();
        gbcIzq.gridx = 0;
        gbcIzq.gridy = 0;
        gbcIzq.anchor = GridBagConstraints.CENTER;
        panelIzquierdo.add(panelContenidoIzquierda, gbcIzq);

        panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setOpaque(true);

        formulario = new JPanel(new GridBagLayout());
        formulario.setOpaque(true);
        formulario.setPreferredSize(new Dimension(380, 500));

        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.gridx = 0;
        gbcForm.gridy = 0;
        gbcForm.weightx = 1.0;
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.anchor = GridBagConstraints.CENTER;
        gbcForm.insets = new Insets(0, 0, 4, 0);

        titulo = new JLabel("¡Bienvenido!", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        formulario.add(titulo, gbcForm);

        gbcForm.gridy++;
        gbcForm.insets = new Insets(0, 0, 20, 0);
        texto = new JLabel("Ingresa tus credenciales para continuar", SwingConstants.CENTER);
        texto.setFont(new Font("SansSerif", Font.PLAIN, 13));
        formulario.add(texto, gbcForm);

        gbcForm.gridy++;
        gbcForm.insets = new Insets(0, 0, 4, 0);
        etiquetaUsuario = new JLabel("Usuario");
        etiquetaUsuario.setFont(new Font("SansSerif", Font.BOLD, 12));
        formulario.add(etiquetaUsuario, gbcForm);

        gbcForm.gridy++;
        gbcForm.insets = new Insets(0, 0, 14, 0);
        campoUsuario = new JTextField();
        campoUsuario.setPreferredSize(new Dimension(310, 38));
        formulario.add(campoUsuario, gbcForm);

        gbcForm.gridy++;
        gbcForm.insets = new Insets(0, 0, 4, 0);
        etiquetaContrasena = new JLabel("Contraseña");
        etiquetaContrasena.setFont(new Font("SansSerif", Font.BOLD, 12));
        formulario.add(etiquetaContrasena, gbcForm);

        gbcForm.gridy++;
        gbcForm.insets = new Insets(0, 0, 22, 0);
        campoContrasena = new JPasswordField();
        campoContrasena.setPreferredSize(new Dimension(310, 38));
        formulario.add(campoContrasena, gbcForm);

        gbcForm.gridy++;
        gbcForm.insets = new Insets(0, 0, 14, 0);
        botonIngresar = new JButton("Iniciar sesión");
        botonIngresar.setPreferredSize(new Dimension(310, 42));
        botonIngresar.addActionListener(e -> iniciarSesion());
        formulario.add(botonIngresar, gbcForm);

        gbcForm.gridy++;
        gbcForm.insets = new Insets(0, 0, 4, 0);
        preguntaRegistro = new JLabel("¿Todavía no tienes una cuenta?", SwingConstants.CENTER);
        preguntaRegistro.setFont(new Font("SansSerif", Font.PLAIN, 12));
        formulario.add(preguntaRegistro, gbcForm);

        gbcForm.gridy++;
        gbcForm.insets = new Insets(0, 0, 24, 0);
        botonRegistro = new JButton("Crear una cuenta nueva");
        botonRegistro.setFont(new Font("SansSerif", Font.BOLD, 12));
        botonRegistro.setBorderPainted(false);
        botonRegistro.setFocusPainted(false);
        botonRegistro.setContentAreaFilled(false);
        botonRegistro.setOpaque(false);
        botonRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonRegistro.addActionListener(e -> abrirRegistro());
        formulario.add(botonRegistro, gbcForm);

        gbcForm.gridy++;
        gbcForm.insets = new Insets(0, 0, 0, 0);
        gbcForm.fill = GridBagConstraints.NONE;
        botonAlternarTema = new JButton();
        botonAlternarTema.setFont(new Font("SansSerif", Font.BOLD, 12));
        botonAlternarTema.setFocusPainted(false);
        botonAlternarTema.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonAlternarTema.setPreferredSize(new Dimension(150, 32));
        botonAlternarTema.addActionListener(e -> alternarTema());
        formulario.add(botonAlternarTema, gbcForm);

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
        actualizarColores();
    }

    private void actualizarColores() {
        panelPrincipal.setBackground(Tema.getColorDeFondo());
        panelDerecho.setBackground(Tema.getColorDeFondo());

        formulario.setBackground(Tema.obtenerColorPanel());
        formulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.obtenerColorBorde(), 1, true),
            new EmptyBorder(25, 30, 25, 30)
        ));

        titulo.setForeground(Tema.ModoOscuro ? Color.WHITE : Tema.MORADO_UNAB);
        texto.setForeground(Tema.obtenerColorTextoSecundario());

        etiquetaUsuario.setForeground(Tema.obtenerColorTexto());
        etiquetaContrasena.setForeground(Tema.obtenerColorTexto());
        preguntaRegistro.setForeground(Tema.obtenerColorTextoSecundario());

        Tema.aplicarEstiloInput(campoUsuario);
        Tema.aplicarEstiloInput(campoContrasena);

        Color colorBoton = Tema.obtenerColorBotonLogin();
        Color colorBotonHover = Tema.obtenerColorBotonLoginHover();
        Tema.aplicarEstiloBotonPrimario(botonIngresar, colorBoton, colorBotonHover);

        botonRegistro.setForeground(Tema.NARANJA_UNAB);

        botonAlternarTema.setText(Tema.ModoOscuro ? "☀️ Modo Claro" : "🌙 Modo Oscuro");
        botonAlternarTema.setBackground(Tema.obtenerColorPanel());
        botonAlternarTema.setForeground(Tema.obtenerColorTexto());
        botonAlternarTema.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.obtenerColorBorde(), 1, true),
            new EmptyBorder(4, 10, 4, 10)
        ));

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
                String usuarioSesion = (controladorAutenticacion.getUsuarioActual() != null && controladorAutenticacion.getUsuarioActual().getUsuario() != null)
                        ? controladorAutenticacion.getUsuarioActual().getUsuario()
                        : usuarioTexto;
                DashboardFrame dashboard = new DashboardFrame(usuarioSesion);
                dashboard.setVisible(true);
                this.dispose();
            } catch (Exception ex) {
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
