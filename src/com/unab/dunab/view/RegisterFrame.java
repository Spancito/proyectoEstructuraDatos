package com.unab.dunab.view;

import com.unab.dunab.controller.Autenticacion;
import com.unab.dunab.view.theme.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private Autenticacion controladorAutenticacion;
    private JFrame ventanaLogin;

    private JPanel panelEnvolvente;
    private JPanel panelFormulario;
    private JLabel titulo;
    private JLabel subtitulo;
    private JLabel etiquetaUsuario;
    private JTextField campoUsuario;
    private JLabel etiquetaContrasena;
    private JPasswordField campoContrasena;
    private JLabel etiquetaConfirmar;
    private JPasswordField campoConfirmarContrasena;
    private JButton botonRegistrar;
    private JButton botonVolver;

    public RegisterFrame(JFrame loginParent, Autenticacion controlador) {
        this.ventanaLogin = loginParent;
        this.controladorAutenticacion = controlador;

        setTitle("DUNAB - Crear nueva cuenta");
        setSize(500, 600);
        setMinimumSize(new Dimension(450, 540));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(loginParent);
        setLayout(new BorderLayout());

        crearInterfaz();
        actualizarColores();
    }

    private void crearInterfaz() {
        panelEnvolvente = new JPanel(new GridBagLayout());
        panelEnvolvente.setOpaque(true);

        panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setOpaque(true);
        panelFormulario.setPreferredSize(new Dimension(380, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 4, 0);

        titulo = new JLabel("Registro DUNAB", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        panelFormulario.add(titulo, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 18, 0);
        subtitulo = new JLabel("Crea tu cuenta institucional", SwingConstants.CENTER);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panelFormulario.add(subtitulo, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 4, 0);
        etiquetaUsuario = new JLabel("Nuevo Usuario");
        etiquetaUsuario.setFont(new Font("SansSerif", Font.BOLD, 12));
        panelFormulario.add(etiquetaUsuario, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 12, 0);
        campoUsuario = new JTextField();
        campoUsuario.setPreferredSize(new Dimension(310, 38));
        panelFormulario.add(campoUsuario, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 4, 0);
        etiquetaContrasena = new JLabel("Contraseña");
        etiquetaContrasena.setFont(new Font("SansSerif", Font.BOLD, 12));
        panelFormulario.add(etiquetaContrasena, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 12, 0);
        campoContrasena = new JPasswordField();
        campoContrasena.setPreferredSize(new Dimension(310, 38));
        panelFormulario.add(campoContrasena, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 4, 0);
        etiquetaConfirmar = new JLabel("Confirmar Contraseña");
        etiquetaConfirmar.setFont(new Font("SansSerif", Font.BOLD, 12));
        panelFormulario.add(etiquetaConfirmar, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 20, 0);
        campoConfirmarContrasena = new JPasswordField();
        campoConfirmarContrasena.setPreferredSize(new Dimension(310, 38));
        panelFormulario.add(campoConfirmarContrasena, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 12, 0);
        botonRegistrar = new JButton("Registrarse");
        botonRegistrar.setPreferredSize(new Dimension(310, 42));
        botonRegistrar.addActionListener(e -> registrarUsuario());
        panelFormulario.add(botonRegistrar, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        botonVolver = new JButton("Volver al inicio de sesión");
        botonVolver.setFont(new Font("SansSerif", Font.BOLD, 12));
        botonVolver.setBorderPainted(false);
        botonVolver.setFocusPainted(false);
        botonVolver.setContentAreaFilled(false);
        botonVolver.setOpaque(false);
        botonVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonVolver.addActionListener(e -> dispose());
        panelFormulario.add(botonVolver, gbc);

        GridBagConstraints gbcEnv = new GridBagConstraints();
        gbcEnv.gridx = 0;
        gbcEnv.gridy = 0;
        gbcEnv.anchor = GridBagConstraints.CENTER;
        panelEnvolvente.add(panelFormulario, gbcEnv);

        add(panelEnvolvente, BorderLayout.CENTER);
    }

    private void actualizarColores() {
        panelEnvolvente.setBackground(Tema.getColorDeFondo());

        panelFormulario.setBackground(Tema.obtenerColorPanel());
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.obtenerColorBorde(), 1, true),
            new EmptyBorder(25, 30, 25, 30)
        ));

        titulo.setForeground(Tema.ModoOscuro ? Color.WHITE : Tema.MORADO_UNAB);
        subtitulo.setForeground(Tema.obtenerColorTextoSecundario());

        etiquetaUsuario.setForeground(Tema.obtenerColorTexto());
        etiquetaContrasena.setForeground(Tema.obtenerColorTexto());
        etiquetaConfirmar.setForeground(Tema.obtenerColorTexto());

        Tema.aplicarEstiloInput(campoUsuario);
        Tema.aplicarEstiloInput(campoContrasena);
        Tema.aplicarEstiloInput(campoConfirmarContrasena);

        Color colorBoton = Tema.obtenerColorBotonLogin();
        Color colorBotonHover = Tema.obtenerColorBotonLoginHover();
        Tema.aplicarEstiloBotonPrimario(botonRegistrar, colorBoton, colorBotonHover);

        botonVolver.setForeground(Tema.NARANJA_UNAB);
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
            JOptionPane.showMessageDialog(this, "Error en el controlador:\n" + ex.getMessage(), "Excepción", JOptionPane.ERROR_MESSAGE);
        }
    }
}
