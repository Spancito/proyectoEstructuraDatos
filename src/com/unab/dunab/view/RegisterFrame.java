package com.unab.dunab.view;

import com.unab.dunab.controller.Autenticacion;
import com.unab.dunab.view.theme.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterFrame extends JDialog {
    private Autenticacion controladorAutenticacion;
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JPanel panelPrincipal, panelCabecera, panelFormulario, panelBotones;
    private JLabel etiquetaTitulo, etiquetaUsuario, etiquetaContrasena;
    private JButton botonRegistrar, botonCancelar;

    public RegisterFrame(JFrame padre, Autenticacion controlador) {
        super(padre, "Registro de Usuario", true);
        this.controladorAutenticacion = controlador;

        setSize(400, 480);
        setLocationRelativeTo(padre);
        setResizable(false);

        panelPrincipal = new JPanel(new BorderLayout());

        panelCabecera = new JPanel(new GridBagLayout());
        panelCabecera.setPreferredSize(new Dimension(400, 80));
        etiquetaTitulo = new JLabel("Crear Cuenta");
        etiquetaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panelCabecera.add(etiquetaTitulo);
        panelPrincipal.add(panelCabecera, BorderLayout.NORTH);

        panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBorder(new EmptyBorder(30, 40, 30, 40));

        etiquetaUsuario = new JLabel("Ingrese su usuario");
        etiquetaUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        etiquetaUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFormulario.add(etiquetaUsuario);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 5)));

        campoUsuario = new JTextField();
        campoUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campoUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panelFormulario.add(campoUsuario);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 20)));

        etiquetaContrasena = new JLabel("Ingrese su contraseña");
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
        
        botonRegistrar = new JButton("Registrarse");
        botonRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        botonRegistrar.setFocusPainted(false);
        botonRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonRegistrar.addActionListener(e -> registrarse());
        panelBotones.add(botonRegistrar);

        botonCancelar = new JButton("Cancelar");
        botonCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botonCancelar.setFocusPainted(false);
        botonCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonCancelar.addActionListener(e -> dispose());
        panelBotones.add(botonCancelar);

        panelFormulario.add(panelBotones);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        add(panelPrincipal);

        actualizarColores();
    }

    private void registrarse() {
        String usuarioTexto = campoUsuario.getText();
        String contrasenaTexto = new String(campoContrasena.getPassword());

        if (usuarioTexto.isEmpty() || contrasenaTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, llena ambos campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (controladorAutenticacion.registrar(usuarioTexto, contrasenaTexto)) {
            JOptionPane.showMessageDialog(this, "Registro exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "El nombre de usuario ya está en uso.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarColores() {
        panelPrincipal.setBackground(Tema.getColorDeFondo());
        panelCabecera.setBackground(Tema.obtenerColorCabecera());
        etiquetaTitulo.setForeground(Tema.obtenerColorTextoCabecera());
        panelFormulario.setBackground(Tema.obtenerColorPanel());
        panelBotones.setBackground(Tema.obtenerColorPanel());
        
        etiquetaUsuario.setForeground(Tema.obtenerColorTexto());
        etiquetaContrasena.setForeground(Tema.obtenerColorTexto());

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

        botonRegistrar.setBackground(Tema.getColorDeBoton());
        botonRegistrar.setForeground(Color.WHITE);
        botonCancelar.setBackground(Tema.obtenerColorBotonSecundario());
        botonCancelar.setForeground(Tema.obtenerColorTexto());
    }
}
