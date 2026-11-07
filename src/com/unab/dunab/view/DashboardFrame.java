package com.unab.dunab.view;

import com.unab.dunab.controller.Autenticacion;
import com.unab.dunab.controller.FinanzasController;
import com.unab.dunab.model.Materia;
import com.unab.dunab.model.MetaAhorro;
import com.unab.dunab.model.Transaccion;
import com.unab.dunab.model.Usuario;
import com.unab.dunab.view.theme.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DashboardFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenido;
    private String usuarioNombre;
    private FinanzasController finanzasController;

    private JPanel menuLateral;
    private JPanel barraSuperior;
    private JButton botonAlternarTema;
    private JLabel etiquetaTituloBarra;
    private JLabel etiquetaUsuarioBarra;
    private String[] secciones;
    private List<JButton> botonesMenuLateral;
    private String seccionActiva;

    private DefaultTableModel modeloTablaTransacciones;
    private DefaultTableModel modeloTablaMaterias;
    private DefaultTableModel modeloTablaMetas;
    private DefaultTableModel modeloTablaRankings;
    private JTable tablaTransacciones;
    private JTable tablaMaterias;
    private JTable tablaMetas;
    private JTable tablaRankings;

    private JLabel etiquetaSaldoNeto;
    private JLabel etiquetaTotalGastos;
    private JLabel etiquetaCreditosTotales;
    private List<JPanel> tarjetasMetricas;

    private JComboBox<String> comboTipoTransaccion;
    private JComboBox<Integer> comboSemestreTransaccion;
    private JComboBox<Integer> comboSemestreMateria;

    public DashboardFrame(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
        this.finanzasController = new FinanzasController();
        this.botonesMenuLateral = new ArrayList<>();
        this.tarjetasMetricas = new ArrayList<>();
        this.seccionActiva = "Inicio";

        setTitle("DUNAB - Panel principal");
        setSize(1280, 800);
        setMinimumSize(new Dimension(1000, 640));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        secciones = new String[]{
            "Inicio", "Mi perfil", "Resumen financiero", "Mi progreso", "Rankings", "Generar reporte"
        };

        crearMenuLateral();
        crearContenido();

        actualizarEstilosGlobales();
    }

    private void crearMenuLateral() {
        menuLateral = new JPanel(new BorderLayout());
        menuLateral.setOpaque(true);
        menuLateral.setBackground(Tema.MORADO_UNAB);
        menuLateral.setPreferredSize(new Dimension(250, 0));

        JPanel encabezado = new JPanel();
        encabezado.setOpaque(true);
        encabezado.setBackground(Tema.MORADO_UNAB);
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.setBorder(new EmptyBorder(35, 20, 30, 20));

        JLabel logoBadge = new JLabel("UNAB");
        logoBadge.setForeground(Tema.NARANJA_UNAB);
        logoBadge.setFont(new Font("SansSerif", Font.BOLD, 12));
        logoBadge.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logo = new JLabel("DUNAB");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 32));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Gestión Financiera");
        subtitulo.setForeground(new Color(225, 215, 235));
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        encabezado.add(logoBadge);
        encabezado.add(Box.createVerticalStrut(4));
        encabezado.add(logo);
        encabezado.add(Box.createVerticalStrut(4));
        encabezado.add(subtitulo);

        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);

        JPanel opciones = new JPanel();
        opciones.setOpaque(false);
        opciones.setLayout(new GridLayout(0, 1, 0, 8));
        opciones.setPreferredSize(new Dimension(210, 290));

        for (String nombre : secciones) {
            JButton boton = new JButton(nombre);
            boton.setFont(new Font("SansSerif", Font.BOLD, 13));
            boton.setForeground(Color.WHITE);
            boton.setBackground(Tema.MORADO_UNAB);
            boton.setHorizontalAlignment(SwingConstants.CENTER);
            boton.setFocusPainted(false);
            boton.setOpaque(true);
            boton.setContentAreaFilled(true);
            boton.setBorderPainted(false);
            boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            boton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent evt) {
                    if (!nombre.equals(seccionActiva)) {
                        boton.setBackground(Tema.MORADO_HOVER);
                    }
                }

                @Override
                public void mouseExited(MouseEvent evt) {
                    if (!nombre.equals(seccionActiva)) {
                        boton.setBackground(Tema.MORADO_UNAB);
                    }
                }
            });

            boton.addActionListener(e -> mostrarSeccion(nombre));
            botonesMenuLateral.add(boton);
            opciones.add(boton);
        }

        GridBagConstraints gbcCentro = new GridBagConstraints();
        gbcCentro.gridx = 0;
        gbcCentro.gridy = 0;
        gbcCentro.anchor = GridBagConstraints.CENTER;
        panelCentro.add(opciones, gbcCentro);

        JButton cerrarSesion = new JButton("Cerrar sesión");
        cerrarSesion.setFont(new Font("SansSerif", Font.BOLD, 13));
        Tema.aplicarEstiloBotonPrimario(cerrarSesion, Tema.NARANJA_UNAB, Tema.NARANJA_HOVER);
        cerrarSesion.setPreferredSize(new Dimension(200, 38));

        cerrarSesion.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas cerrar sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                LoginFrame login = new LoginFrame(new Autenticacion());
                login.setVisible(true);
                dispose();
            }
        });

        JPanel inferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        inferior.setOpaque(true);
        inferior.setBackground(Tema.MORADO_UNAB);
        inferior.add(cerrarSesion);

        menuLateral.add(encabezado, BorderLayout.NORTH);
        menuLateral.add(panelCentro, BorderLayout.CENTER);
        menuLateral.add(inferior, BorderLayout.SOUTH);

        add(menuLateral, BorderLayout.WEST);
    }

    private void crearContenido() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.setOpaque(true);

        barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setOpaque(true);
        barraSuperior.setBorder(new EmptyBorder(16, 25, 16, 25));

        etiquetaTituloBarra = new JLabel("Bienvenido a DUNAB");
        etiquetaTituloBarra.setFont(new Font("SansSerif", Font.BOLD, 22));

        JPanel panelDerechoBarra = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelDerechoBarra.setOpaque(false);

        etiquetaUsuarioBarra = new JLabel("Estudiante: " + usuarioNombre);
        etiquetaUsuarioBarra.setFont(new Font("SansSerif", Font.PLAIN, 14));

        botonAlternarTema = new JButton();
        botonAlternarTema.setFont(new Font("SansSerif", Font.BOLD, 12));
        botonAlternarTema.setFocusPainted(false);
        botonAlternarTema.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonAlternarTema.setPreferredSize(new Dimension(140, 32));
        botonAlternarTema.addActionListener(e -> alternarTemaDashboard());

        panelDerechoBarra.add(etiquetaUsuarioBarra);
        panelDerechoBarra.add(botonAlternarTema);

        barraSuperior.add(etiquetaTituloBarra, BorderLayout.WEST);
        barraSuperior.add(panelDerechoBarra, BorderLayout.EAST);

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setOpaque(true);
        panelContenido.setBorder(new EmptyBorder(20, 25, 20, 25));

        for (String seccion : secciones) {
            panelContenido.add(crearPanelSeccionEspecifico(seccion), seccion);
        }

        principal.add(barraSuperior, BorderLayout.NORTH);
        principal.add(panelContenido, BorderLayout.CENTER);

        add(principal, BorderLayout.CENTER);

        mostrarSeccion("Inicio");
    }

    private JPanel crearPanelSeccionEspecifico(String nombre) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(true);

        JPanel encabezadoSeccion = new JPanel();
        encabezadoSeccion.setLayout(new BoxLayout(encabezadoSeccion, BoxLayout.Y_AXIS));
        encabezadoSeccion.setOpaque(false);
        encabezadoSeccion.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel titulo = new JLabel(nombre);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descripcion = new JLabel(obtenerDescripcionSeccion(nombre));
        descripcion.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        encabezadoSeccion.add(titulo);
        encabezadoSeccion.add(Box.createVerticalStrut(4));
        encabezadoSeccion.add(descripcion);

        panel.add(encabezadoSeccion, BorderLayout.NORTH);
        panel.add(construirContenidoPorNombre(nombre), BorderLayout.CENTER);

        return panel;
    }

    private String obtenerDescripcionSeccion(String nombre) {
        switch (nombre) {
            case "Inicio": return "Panel general con estado financiero actual, avance de materias y balance.";
            case "Mi perfil": return "Consulta y edita tus datos institucionales y presupuesto mensual.";
            case "Resumen financiero": return "Registro y consulta de ingresos y gastos de tu periodo académico.";
            case "Mi progreso": return "Gestión y cálculo de materias inscritas y metas de ahorro.";
            case "Rankings": return "Cuadro de honor y clasificación de estudiantes destacados por rendimiento académico.";
            case "Generar reporte": return "Exportación oficial de balances, historial y materias a PDF y Excel.";
            default: return "Módulo de gestión del sistema DUNAB.";
        }
    }

    private JPanel construirContenidoPorNombre(String nombre) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setOpaque(true);
        tarjeta.setBorder(new EmptyBorder(20, 20, 20, 20));

        switch (nombre) {
            case "Inicio":
                JPanel panelInicio = new JPanel();
                panelInicio.setLayout(new BoxLayout(panelInicio, BoxLayout.Y_AXIS));
                panelInicio.setOpaque(false);

                JPanel metricas = new JPanel(new GridLayout(1, 3, 20, 0));
                metricas.setOpaque(false);

                etiquetaSaldoNeto = new JLabel("$ 0.00");
                etiquetaSaldoNeto.setFont(new Font("SansSerif", Font.BOLD, 24));

                etiquetaTotalGastos = new JLabel("$ 0.00");
                etiquetaTotalGastos.setFont(new Font("SansSerif", Font.BOLD, 24));

                etiquetaCreditosTotales = new JLabel("0 Créditos");
                etiquetaCreditosTotales.setFont(new Font("SansSerif", Font.BOLD, 24));

                JPanel card1 = crearTarjetaMetricaConComponente("Saldo Neto Actual", etiquetaSaldoNeto, Tema.MORADO_UNAB);
                JPanel card2 = crearTarjetaMetricaConComponente("Total Gastos Registrados", etiquetaTotalGastos, Tema.NARANJA_UNAB);
                JPanel card3 = crearTarjetaMetricaConComponente("Créditos Académicos", etiquetaCreditosTotales, Tema.EXITO);

                tarjetasMetricas.add(card1);
                tarjetasMetricas.add(card2);
                tarjetasMetricas.add(card3);

                metricas.add(card1);
                metricas.add(card2);
                metricas.add(card3);
                metricas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

                PanelGraficoFinanciero grafico = new PanelGraficoFinanciero();
                grafico.setPreferredSize(new Dimension(0, 280));

                panelInicio.add(metricas);
                panelInicio.add(Box.createVerticalStrut(25));
                panelInicio.add(grafico);
                tarjeta.add(panelInicio, BorderLayout.CENTER);
                break;

            case "Mi perfil":
                Usuario usuario = finanzasController.obtenerUsuario(usuarioNombre);

                JPanel panelPerfil = new JPanel();
                panelPerfil.setLayout(new BoxLayout(panelPerfil, BoxLayout.Y_AXIS));
                panelPerfil.setOpaque(false);

                JTextField campoNombre = new JTextField(usuario != null && usuario.getNombre() != null ? usuario.getNombre() : usuarioNombre);
                JTextField campoCorreo = new JTextField(usuario != null && usuario.getCorreo() != null ? usuario.getCorreo() : "");
                JTextField campoCarrera = new JTextField(usuario != null && usuario.getCarrera() != null ? usuario.getCarrera() : "");
                JTextField campoPresupuesto = new JTextField(usuario != null ? String.valueOf(usuario.getPresupuestoMensual()) : "0.0");

                panelPerfil.add(crearFilaFormulario("Nombre Completo:", campoNombre));
                panelPerfil.add(Box.createVerticalStrut(12));
                panelPerfil.add(crearFilaFormulario("Correo Electrónico:", campoCorreo));
                panelPerfil.add(Box.createVerticalStrut(12));
                panelPerfil.add(crearFilaFormulario("Programa / Carrera:", campoCarrera));
                panelPerfil.add(Box.createVerticalStrut(12));
                panelPerfil.add(crearFilaFormulario("Presupuesto Mensual ($):", campoPresupuesto));
                panelPerfil.add(Box.createVerticalStrut(24));

                JButton botonGuardarPerfil = new JButton("Guardar Cambios de Perfil");
                Tema.aplicarEstiloBotonPrimario(botonGuardarPerfil, Tema.NARANJA_UNAB, Tema.NARANJA_HOVER);
                botonGuardarPerfil.setPreferredSize(new Dimension(240, 40));
                botonGuardarPerfil.setMaximumSize(new Dimension(240, 40));
                botonGuardarPerfil.addActionListener(e -> {
                    try {
                        double presupuesto = Double.parseDouble(campoPresupuesto.getText().trim());
                        boolean ok = finanzasController.actualizarPerfilUsuario(
                            usuarioNombre,
                            campoNombre.getText().trim(),
                            campoCorreo.getText().trim(),
                            campoCarrera.getText().trim(),
                            presupuesto
                        );
                        if (ok) {
                            JOptionPane.showMessageDialog(this, "Perfil actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            actualizarDatosGenerales();
                        } else {
                            JOptionPane.showMessageDialog(this, "No se pudo actualizar el perfil.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Presupuesto mensual inválido. Ingrese un valor numérico.", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                });

                panelPerfil.add(botonGuardarPerfil);
                tarjeta.add(panelPerfil, BorderLayout.NORTH);
                break;

            case "Resumen financiero":
                JPanel panelFinanzas = new JPanel(new BorderLayout(0, 15));
                panelFinanzas.setOpaque(false);

                JPanel formularioTransaccion = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                formularioTransaccion.setOpaque(false);

                comboTipoTransaccion = new JComboBox<>(new String[]{"INGRESO", "GASTO"});
                JTextField campoMonto = new JTextField(7);
                JTextField campoCategoria = new JTextField(10);
                JTextField campoDescripcion = new JTextField(14);
                comboSemestreTransaccion = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

                Tema.aplicarEstiloCombo(comboTipoTransaccion);
                Tema.aplicarEstiloCombo(comboSemestreTransaccion);
                Tema.aplicarEstiloInput(campoMonto);
                Tema.aplicarEstiloInput(campoCategoria);
                Tema.aplicarEstiloInput(campoDescripcion);

                JButton botonAgregarTransaccion = new JButton("+ Agregar Movimiento");
                Tema.aplicarEstiloBotonPrimario(botonAgregarTransaccion, Tema.NARANJA_UNAB, Tema.NARANJA_HOVER);

                JButton botonEliminarTransaccion = new JButton("Eliminar Seleccionado");
                Tema.aplicarEstiloBotonPrimario(botonEliminarTransaccion, Tema.PELIGRO, new Color(230, 40, 40));

                formularioTransaccion.add(new JLabel("Tipo:"));
                formularioTransaccion.add(comboTipoTransaccion);
                formularioTransaccion.add(new JLabel("Monto:"));
                formularioTransaccion.add(campoMonto);
                formularioTransaccion.add(new JLabel("Categoría:"));
                formularioTransaccion.add(campoCategoria);
                formularioTransaccion.add(new JLabel("Descripción:"));
                formularioTransaccion.add(campoDescripcion);
                formularioTransaccion.add(new JLabel("Semestre:"));
                formularioTransaccion.add(comboSemestreTransaccion);
                formularioTransaccion.add(botonAgregarTransaccion);
                formularioTransaccion.add(botonEliminarTransaccion);

                String[] colTrans = {"ID", "Fecha", "Tipo", "Categoría", "Descripción", "Semestre", "Monto"};
                modeloTablaTransacciones = new DefaultTableModel(colTrans, 0) {
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                tablaTransacciones = new JTable(modeloTablaTransacciones);
                Tema.aplicarEstiloTabla(tablaTransacciones);

                botonAgregarTransaccion.addActionListener(e -> {
                    try {
                        double monto = Double.parseDouble(campoMonto.getText().trim());
                        String tipo = (String) comboTipoTransaccion.getSelectedItem();
                        String categoria = campoCategoria.getText().trim();
                        String descripcion = campoDescripcion.getText().trim();
                        int sem = (Integer) comboSemestreTransaccion.getSelectedItem();

                        if (monto <= 0 || categoria.isEmpty() || descripcion.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Complete los campos y coloque un monto válido mayor a 0.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        boolean ok = finanzasController.registrarTransaccion(usuarioNombre, monto, tipo, categoria, descripcion, sem);
                        if (ok) {
                            campoMonto.setText("");
                            campoCategoria.setText("");
                            campoDescripcion.setText("");
                            actualizarDatosGenerales();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Monto inválido. Ingrese un número válido.", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                });

                botonEliminarTransaccion.addActionListener(e -> {
                    int fila = tablaTransacciones.getSelectedRow();
                    if (fila >= 0) {
                        String id = (String) modeloTablaTransacciones.getValueAt(fila, 0);
                        finanzasController.eliminarTransaccion(id);
                        actualizarDatosGenerales();
                    } else {
                        JOptionPane.showMessageDialog(this, "Seleccione una transacción de la tabla para eliminar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    }
                });

                panelFinanzas.add(formularioTransaccion, BorderLayout.NORTH);
                JScrollPane scrollTrans = new JScrollPane(tablaTransacciones);
                scrollTrans.setOpaque(true);
                scrollTrans.getViewport().setOpaque(true);
                panelFinanzas.add(scrollTrans, BorderLayout.CENTER);
                tarjeta.add(panelFinanzas, BorderLayout.CENTER);
                break;

            case "Mi progreso":
                JPanel panelProgreso = new JPanel(new GridLayout(2, 1, 0, 15));
                panelProgreso.setOpaque(false);

                JPanel bloqueMaterias = new JPanel(new BorderLayout(0, 10));
                bloqueMaterias.setOpaque(false);
                bloqueMaterias.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Tema.obtenerColorBorde(), 1, true),
                    "Materias Académicas",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("SansSerif", Font.BOLD, 13),
                    Tema.MORADO_UNAB
                ));

                JPanel formMateria = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                formMateria.setOpaque(false);
                JTextField campoMateriaNombre = new JTextField(12);
                JTextField campoCreditos = new JTextField(4);
                JTextField campoValorCredito = new JTextField(7);
                comboSemestreMateria = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

                Tema.aplicarEstiloCombo(comboSemestreMateria);
                Tema.aplicarEstiloInput(campoMateriaNombre);
                Tema.aplicarEstiloInput(campoCreditos);
                Tema.aplicarEstiloInput(campoValorCredito);

                JButton botonAgregarMateria = new JButton("+ Inscribir Materia");
                Tema.aplicarEstiloBotonPrimario(botonAgregarMateria, Tema.NARANJA_UNAB, Tema.NARANJA_HOVER);

                JButton botonEliminarMateria = new JButton("Eliminar Materia");
                Tema.aplicarEstiloBotonPrimario(botonEliminarMateria, Tema.PELIGRO, new Color(230, 40, 40));

                formMateria.add(new JLabel("Materia:"));
                formMateria.add(campoMateriaNombre);
                formMateria.add(new JLabel("Créditos:"));
                formMateria.add(campoCreditos);
                formMateria.add(new JLabel("Valor/Crédito:"));
                formMateria.add(campoValorCredito);
                formMateria.add(new JLabel("Semestre:"));
                formMateria.add(comboSemestreMateria);
                formMateria.add(botonAgregarMateria);
                formMateria.add(botonEliminarMateria);

                String[] colMat = {"ID", "Nombre Asignatura", "Créditos", "Valor Crédito", "Semestre", "Costo Total"};
                modeloTablaMaterias = new DefaultTableModel(colMat, 0) {
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                tablaMaterias = new JTable(modeloTablaMaterias);
                Tema.aplicarEstiloTabla(tablaMaterias);

                botonAgregarMateria.addActionListener(e -> {
                    try {
                        String nom = campoMateriaNombre.getText().trim();
                        int cred = Integer.parseInt(campoCreditos.getText().trim());
                        double val = Double.parseDouble(campoValorCredito.getText().trim());
                        int sem = (Integer) comboSemestreMateria.getSelectedItem();

                        if (nom.isEmpty() || cred <= 0 || val < 0) {
                            JOptionPane.showMessageDialog(this, "Datos de materia inválidos.", "Error", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        boolean ok = finanzasController.agregarMateria(usuarioNombre, nom, cred, val, sem);
                        if (ok) {
                            campoMateriaNombre.setText("");
                            campoCreditos.setText("");
                            campoValorCredito.setText("");
                            actualizarDatosGenerales();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Créditos o valor por crédito numérico inválido.", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                });

                botonEliminarMateria.addActionListener(e -> {
                    int fila = tablaMaterias.getSelectedRow();
                    if (fila >= 0) {
                        String id = (String) modeloTablaMaterias.getValueAt(fila, 0);
                        finanzasController.eliminarMateria(id);
                        actualizarDatosGenerales();
                    } else {
                        JOptionPane.showMessageDialog(this, "Seleccione una materia para eliminar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    }
                });

                bloqueMaterias.add(formMateria, BorderLayout.NORTH);
                JScrollPane scrollMat = new JScrollPane(tablaMaterias);
                scrollMat.setOpaque(true);
                scrollMat.getViewport().setOpaque(true);
                bloqueMaterias.add(scrollMat, BorderLayout.CENTER);

                JPanel bloqueMetas = new JPanel(new BorderLayout(0, 10));
                bloqueMetas.setOpaque(false);
                bloqueMetas.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Tema.obtenerColorBorde(), 1, true),
                    "Metas de Ahorro",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("SansSerif", Font.BOLD, 13),
                    Tema.MORADO_UNAB
                ));

                JPanel formMetas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                formMetas.setOpaque(false);
                JTextField campoMetaNombre = new JTextField(12);
                JTextField campoMetaObjetivo = new JTextField(7);
                JTextField campoMetaActual = new JTextField(7);

                Tema.aplicarEstiloInput(campoMetaNombre);
                Tema.aplicarEstiloInput(campoMetaObjetivo);
                Tema.aplicarEstiloInput(campoMetaActual);

                JButton botonAgregarMeta = new JButton("+ Nueva Meta");
                Tema.aplicarEstiloBotonPrimario(botonAgregarMeta, Tema.NARANJA_UNAB, Tema.NARANJA_HOVER);

                JButton botonEliminarMeta = new JButton("Eliminar Meta");
                Tema.aplicarEstiloBotonPrimario(botonEliminarMeta, Tema.PELIGRO, new Color(230, 40, 40));

                formMetas.add(new JLabel("Nombre Meta:"));
                formMetas.add(campoMetaNombre);
                formMetas.add(new JLabel("Objetivo ($):"));
                formMetas.add(campoMetaObjetivo);
                formMetas.add(new JLabel("Ahorro Actual ($):"));
                formMetas.add(campoMetaActual);
                formMetas.add(botonAgregarMeta);
                formMetas.add(botonEliminarMeta);

                String[] colMetas = {"ID", "Meta", "Objetivo", "Actual", "Progreso (%)"};
                modeloTablaMetas = new DefaultTableModel(colMetas, 0) {
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                tablaMetas = new JTable(modeloTablaMetas);
                Tema.aplicarEstiloTabla(tablaMetas);

                botonAgregarMeta.addActionListener(e -> {
                    try {
                        String nMeta = campoMetaNombre.getText().trim();
                        double obj = Double.parseDouble(campoMetaObjetivo.getText().trim());
                        double act = Double.parseDouble(campoMetaActual.getText().trim());
                        if (nMeta.isEmpty() || obj <= 0) {
                            JOptionPane.showMessageDialog(this, "Datos de meta inválidos.", "Error", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        boolean ok = finanzasController.agregarMetaAhorro(usuarioNombre, nMeta, obj, act);
                        if (ok) {
                            campoMetaNombre.setText("");
                            campoMetaObjetivo.setText("");
                            campoMetaActual.setText("");
                            actualizarDatosGenerales();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Valores numéricos de meta inválidos.", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                });

                botonEliminarMeta.addActionListener(e -> {
                    int fila = tablaMetas.getSelectedRow();
                    if (fila >= 0) {
                        String id = (String) modeloTablaMetas.getValueAt(fila, 0);
                        finanzasController.eliminarMetaAhorro(id);
                        actualizarDatosGenerales();
                    } else {
                        JOptionPane.showMessageDialog(this, "Seleccione una meta para eliminar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    }
                });

                bloqueMetas.add(formMetas, BorderLayout.NORTH);
                JScrollPane scrollMetas = new JScrollPane(tablaMetas);
                scrollMetas.setOpaque(true);
                scrollMetas.getViewport().setOpaque(true);
                bloqueMetas.add(scrollMetas, BorderLayout.CENTER);

                panelProgreso.add(bloqueMaterias);
                panelProgreso.add(bloqueMetas);
                tarjeta.add(panelProgreso, BorderLayout.CENTER);
                break;

            case "Rankings":
                JPanel panelRankings = new JPanel(new BorderLayout(0, 15));
                panelRankings.setOpaque(false);

                JPanel cabeceraRankings = new JPanel(new GridLayout(1, 3, 15, 0));
                cabeceraRankings.setOpaque(false);

                JPanel cardOro = crearTarjetaPodio("🥇 1er Puesto", "Kevin Nuñez", "Ing. Sistemas", "4.88", new Color(212, 175, 55));
                JPanel cardPlata = crearTarjetaPodio("🥈 2do Puesto", "Mariana Gomez", "Medicina", "4.82", new Color(192, 192, 192));
                JPanel cardBronce = crearTarjetaPodio("🥉 3er Puesto", "Diego Garcia", "Ing. Sistemas", "4.75", new Color(205, 127, 50));

                cabeceraRankings.add(cardOro);
                cabeceraRankings.add(cardPlata);
                cabeceraRankings.add(cardBronce);
                cabeceraRankings.setPreferredSize(new Dimension(0, 95));

                String[] colRank = {"Posición", "Estudiante", "Carrera", "Semestre", "Promedio Académico / Progreso"};
                modeloTablaRankings = new DefaultTableModel(colRank, 0) {
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                tablaRankings = new JTable(modeloTablaRankings);
                Tema.aplicarEstiloTabla(tablaRankings);

                cargarDatosRankings();

                JScrollPane scrollRank = new JScrollPane(tablaRankings);
                scrollRank.setOpaque(true);
                scrollRank.getViewport().setOpaque(true);

                panelRankings.add(cabeceraRankings, BorderLayout.NORTH);
                panelRankings.add(scrollRank, BorderLayout.CENTER);
                tarjeta.add(panelRankings, BorderLayout.CENTER);
                break;

            case "Generar reporte":
                JPanel panelReporte = new JPanel();
                panelReporte.setLayout(new BoxLayout(panelReporte, BoxLayout.Y_AXIS));
                panelReporte.setOpaque(false);

                JLabel infoReporte = new JLabel("Exporta la información oficial de finanzas, materias y balance del estudiante:");
                infoReporte.setFont(new Font("SansSerif", Font.PLAIN, 14));

                JPanel panelBotonesReporte = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
                panelBotonesReporte.setOpaque(false);

                JButton btnPdf = new JButton("📄 Exportar Informe a PDF");
                Tema.aplicarEstiloBotonPrimario(btnPdf, Tema.NARANJA_UNAB, Tema.NARANJA_HOVER);
                btnPdf.setPreferredSize(new Dimension(220, 42));
                btnPdf.addActionListener(e -> exportarInforme("pdf"));

                JButton btnExcel = new JButton("📊 Exportar Historial a Excel");
                Tema.aplicarEstiloBotonPrimario(btnExcel, Tema.MORADO_UNAB, Tema.MORADO_HOVER);
                btnExcel.setPreferredSize(new Dimension(220, 42));
                btnExcel.addActionListener(e -> exportarInforme("excel"));

                panelBotonesReporte.add(btnPdf);
                panelBotonesReporte.add(btnExcel);

                panelReporte.add(infoReporte);
                panelReporte.add(Box.createVerticalStrut(15));
                panelReporte.add(panelBotonesReporte);

                tarjeta.add(panelReporte, BorderLayout.NORTH);
                break;
        }

        return tarjeta;
    }

    private JPanel crearTarjetaPodio(String medalla, String nombre, String carrera, String promedio, Color colorAcento) {
        JPanel p = new JPanel();
        p.setOpaque(true);
        p.setBackground(Tema.obtenerColorPanel());
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.obtenerColorBorde(), 1, true),
            new EmptyBorder(10, 14, 10, 14)
        ));

        JLabel lblMedalla = new JLabel(medalla);
        lblMedalla.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblMedalla.setForeground(colorAcento);

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblNombre.setForeground(Tema.obtenerColorTexto());

        JLabel lblInfo = new JLabel(carrera + " | Prom: " + promedio);
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblInfo.setForeground(Tema.obtenerColorTextoSecundario());

        p.add(lblMedalla);
        p.add(Box.createVerticalStrut(4));
        p.add(lblNombre);
        p.add(Box.createVerticalStrut(2));
        p.add(lblInfo);

        tarjetasMetricas.add(p);
        return p;
    }

    private void cargarDatosRankings() {
        if (modeloTablaRankings == null) return;
        modeloTablaRankings.setRowCount(0);

        Object[][] datos = {
            {"1°", "Kevin Nuñez (knunez153)", "Ingeniería de Sistemas", "3° Semestre", "4.88 / 96% Progreso"},
            {"2°", "Mariana Gomez (mgomez412)", "Medicina", "8° Semestre", "4.82 / 94% Progreso"},
            {"3°", "Diego Garcia (dgarcia745)", "Ingeniería de Sistemas", "3° Semestre", "4.75 / 92% Progreso"},
            {"4°", "David Rey (drey188)", "Ingeniería de Sistemas", "3° Semestre", "4.70 / 90% Progreso"},
            {"5°", "Santiago Paez (spaez109)", "Administración de Empresas", "5° Semestre", "4.64 / 88% Progreso"},
            {"6°", "Valentina Duarte (vduarte831)", "Derecho", "7° Semestre", "4.58 / 85% Progreso"},
            {"7°", "Laura Suarez (lsuarez562)", "Comunicación Social", "4° Semestre", "4.50 / 82% Progreso"}
        };

        for (Object[] fila : datos) {
            modeloTablaRankings.addRow(fila);
        }
    }

    private void exportarInforme(String tipo) {
        JFileChooser fileChooser = new JFileChooser();
        String extensionPredeterminada = tipo.equals("pdf") ? ".pdf" : ".csv";
        fileChooser.setDialogTitle("Guardar Reporte " + (tipo.equals("pdf") ? "PDF" : "EXCEL"));
        String defaultName = "Reporte_DUNAB_" + usuarioNombre + extensionPredeterminada;
        fileChooser.setSelectedFile(new File(defaultName));

        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File destino = fileChooser.getSelectedFile();
            String ruta = destino.getAbsolutePath();
            if (tipo.equals("pdf")) {
                if (!ruta.toLowerCase().endsWith(".pdf")) {
                    ruta += ".pdf";
                }
            } else {
                if (!ruta.toLowerCase().endsWith(".csv") && !ruta.toLowerCase().endsWith(".xls") && !ruta.toLowerCase().endsWith(".xlsx")) {
                    ruta += ".csv";
                }
            }

            boolean exitoso = false;
            try {
                if (tipo.equals("pdf")) {
                    exitoso = finanzasController.exportarReportePDF(usuarioNombre, ruta);
                } else {
                    exitoso = finanzasController.exportarReporteExcel(usuarioNombre, ruta);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Excepción durante la exportación:\n" + ex.getMessage(), "Error de Exportación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (exitoso) {
                JOptionPane.showMessageDialog(this, "Reporte generado con éxito en:\n" + ruta, "Exportación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo generar el reporte. Verifique permisos de escritura en la ruta seleccionada.", "Error de Exportación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarDatosGenerales() {
        double saldo = finanzasController.calcularSaldoNeto(usuarioNombre);
        double gastos = finanzasController.calcularTotalGastos(usuarioNombre);
        int creditos = finanzasController.calcularTotalCreditos(usuarioNombre);

        if (etiquetaSaldoNeto != null) {
            etiquetaSaldoNeto.setText(String.format("$ %,.2f", saldo));
            etiquetaSaldoNeto.setForeground(saldo >= 0 ? (Tema.ModoOscuro ? new Color(130, 230, 150) : Tema.EXITO) : Tema.PELIGRO);
        }
        if (etiquetaTotalGastos != null) {
            etiquetaTotalGastos.setText(String.format("$ %,.2f", gastos));
            etiquetaTotalGastos.setForeground(Tema.NARANJA_UNAB);
        }
        if (etiquetaCreditosTotales != null) {
            etiquetaCreditosTotales.setText(creditos + " Créditos");
            etiquetaCreditosTotales.setForeground(Tema.ModoOscuro ? Tema.TEXTO_CLARO : Tema.MORADO_UNAB);
        }

        if (modeloTablaTransacciones != null) {
            modeloTablaTransacciones.setRowCount(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            List<Transaccion> transacciones = finanzasController.obtenerTransaccionesUsuario(usuarioNombre);
            for (Transaccion t : transacciones) {
                String fecha = (t.getFechaHora() != null) ? t.getFechaHora().format(formatter) : "";
                modeloTablaTransacciones.addRow(new Object[]{
                    t.getId(), fecha, t.getTipo(), t.getCategoria(), t.getDescripcion(), t.getSemestreIndex(), String.format("$ %,.2f", t.getMonto())
                });
            }
        }

        if (modeloTablaMaterias != null) {
            modeloTablaMaterias.setRowCount(0);
            List<Materia> materias = finanzasController.obtenerMateriasUsuario(usuarioNombre);
            for (Materia m : materias) {
                modeloTablaMaterias.addRow(new Object[]{
                    m.getId(), m.getNombre(), m.getCreditos(), String.format("$ %,.2f", m.getValorPorCredito()), m.getSemestreIndex(), String.format("$ %,.2f", m.getCostoTotal())
                });
            }
        }

        if (modeloTablaMetas != null) {
            modeloTablaMetas.setRowCount(0);
            List<MetaAhorro> metas = finanzasController.obtenerMetasUsuario(usuarioNombre);
            for (MetaAhorro meta : metas) {
                modeloTablaMetas.addRow(new Object[]{
                    meta.getId(), meta.getNombreMeta(), String.format("$ %,.2f", meta.getValorObjetivo()), String.format("$ %,.2f", meta.getValorActual()), String.format("%.1f%%", meta.getProgresoPorcentaje())
                });
            }
        }

        if (modeloTablaRankings != null) {
            cargarDatosRankings();
        }

        repaint();
    }

    private JPanel crearTarjetaMetricaConComponente(String titulo, JLabel valor, Color acento) {
        JPanel p = new JPanel();
        p.setOpaque(true);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel(titulo);
        t.setFont(new Font("SansSerif", Font.PLAIN, 12));
        t.setForeground(Tema.obtenerColorTextoSecundario());

        p.add(t);
        p.add(Box.createVerticalStrut(6));
        p.add(valor);
        return p;
    }

    private JPanel crearFilaFormulario(String etiqueta, JTextField campo) {
        JPanel p = new JPanel(new BorderLayout(12, 0));
        p.setOpaque(false);
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(Tema.obtenerColorTexto());
        lbl.setPreferredSize(new Dimension(180, 32));

        Tema.aplicarEstiloInput(campo);
        campo.setPreferredSize(new Dimension(280, 32));

        p.add(lbl, BorderLayout.WEST);
        p.add(campo, BorderLayout.CENTER);
        return p;
    }

    private class PanelGraficoFinanciero extends JPanel {
        public PanelGraficoFinanciero() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(Tema.ModoOscuro ? Color.WHITE : Tema.MORADO_UNAB);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.drawString("Balance de Finanzas Personales (UNAB)", 15, 20);

            double ingresos = finanzasController.calcularTotalIngresos(usuarioNombre);
            double gastos = finanzasController.calcularTotalGastos(usuarioNombre);
            double saldo = finanzasController.calcularSaldoNeto(usuarioNombre);

            double maxVal = Math.max(ingresos, Math.max(gastos, Math.abs(saldo)));
            if (maxVal <= 0) maxVal = 100.0;

            String[] nombres = {"Ingresos", "Gastos", "Saldo Neto"};
            double[] valores = {ingresos, gastos, Math.max(0, saldo)};
            Color[] colores = {Tema.EXITO, Tema.NARANJA_UNAB, Tema.MORADO_UNAB};

            int anchoBarra = 65;
            int espacio = (w - (anchoBarra * 3)) / 4;

            g2.setColor(Tema.obtenerColorBorde());
            g2.drawLine(20, h - 35, w - 20, h - 35);

            for (int i = 0; i < 3; i++) {
                int x = espacio + i * (anchoBarra + espacio);
                int altura = (int) ((h - 90) * (valores[i] / maxVal));
                int y = h - 35 - altura;

                g2.setColor(colores[i]);
                g2.fillRect(x, y, anchoBarra, altura);

                g2.setColor(Tema.obtenerColorTexto());
                g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g2.drawString(nombres[i], x + 5, h - 16);
                g2.drawString(String.format("$%.0f", valores[i]), x, Math.max(20, y - 6));
            }
        }
    }

    private void mostrarSeccion(String nombre) {
        this.seccionActiva = nombre;
        etiquetaTituloBarra.setText(nombre.equals("Inicio") ? "Bienvenido a DUNAB" : nombre);

        for (JButton btn : botonesMenuLateral) {
            if (btn.getText().equals(nombre)) {
                btn.setBackground(Tema.NARANJA_UNAB);
            } else {
                btn.setBackground(Tema.MORADO_UNAB);
            }
        }

        cardLayout.show(panelContenido, nombre);
        actualizarDatosGenerales();
    }

    private void alternarTemaDashboard() {
        Tema.ModoOscuro = !Tema.ModoOscuro;
        actualizarEstilosGlobales();
    }

    private void actualizarEstilosGlobales() {
        getContentPane().setBackground(Tema.getColorDeFondo());

        barraSuperior.setBackground(Tema.obtenerColorPanel());
        barraSuperior.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.obtenerColorBorde()),
            new EmptyBorder(16, 25, 16, 25)
        ));

        etiquetaTituloBarra.setForeground(Tema.ModoOscuro ? Color.WHITE : Tema.MORADO_UNAB);
        etiquetaUsuarioBarra.setForeground(Tema.obtenerColorTexto());

        botonAlternarTema.setText(Tema.ModoOscuro ? "☀️ Modo Claro" : "🌙 Modo Oscuro");
        botonAlternarTema.setBackground(Tema.obtenerColorPanel());
        botonAlternarTema.setForeground(Tema.obtenerColorTexto());
        botonAlternarTema.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.obtenerColorBorde(), 1, true),
            new EmptyBorder(4, 10, 4, 10)
        ));

        panelContenido.setBackground(Tema.getColorDeFondo());

        for (JPanel card : tarjetasMetricas) {
            card.setOpaque(true);
            card.setBackground(Tema.obtenerColorPanel());
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.obtenerColorBorde(), 1, true),
                new EmptyBorder(10, 14, 10, 14)
            ));
        }

        for (Component comp : panelContenido.getComponents()) {
            if (comp instanceof Container) {
                actualizarComponentesRecursivo((Container) comp);
            }
        }

        if (comboTipoTransaccion != null) Tema.aplicarEstiloCombo(comboTipoTransaccion);
        if (comboSemestreTransaccion != null) Tema.aplicarEstiloCombo(comboSemestreTransaccion);
        if (comboSemestreMateria != null) Tema.aplicarEstiloCombo(comboSemestreMateria);

        if (tablaTransacciones != null) Tema.aplicarEstiloTabla(tablaTransacciones);
        if (tablaMaterias != null) Tema.aplicarEstiloTabla(tablaMaterias);
        if (tablaMetas != null) Tema.aplicarEstiloTabla(tablaMetas);
        if (tablaRankings != null) Tema.aplicarEstiloTabla(tablaRankings);

        mostrarSeccion(seccionActiva);
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void actualizarComponentesRecursivo(Container container) {
        container.setBackground(Tema.getColorDeFondo());
        for (Component c : container.getComponents()) {
            if (c instanceof JPanel) {
                JPanel jp = (JPanel) c;
                if (jp.getBorder() != null && jp.getBorder() instanceof TitledBorder) {
                    TitledBorder tb = (TitledBorder) jp.getBorder();
                    tb.setTitleColor(Tema.ModoOscuro ? Tema.NARANJA_UNAB : Tema.MORADO_UNAB);
                    tb.setBorder(BorderFactory.createLineBorder(Tema.obtenerColorBorde(), 1, true));
                    jp.setOpaque(false);
                } else if (jp.getParent() == panelContenido || jp.getParent() instanceof JViewport || jp.getLayout() instanceof BorderLayout) {
                    jp.setOpaque(true);
                    jp.setBackground(Tema.obtenerColorPanel());
                    jp.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Tema.obtenerColorBorde(), 1, true),
                        new EmptyBorder(20, 20, 20, 20)
                    ));
                }
            } else if (c instanceof JScrollPane) {
                JScrollPane sp = (JScrollPane) c;
                sp.setOpaque(true);
                sp.setBackground(Tema.obtenerColorPanel());
                sp.getViewport().setOpaque(true);
                sp.getViewport().setBackground(Tema.obtenerColorPanel());
                sp.setBorder(BorderFactory.createLineBorder(Tema.obtenerColorBorde(), 1));
            } else if (c instanceof JLabel) {
                JLabel jl = (JLabel) c;
                if (jl.getFont().getSize() > 18) {
                    jl.setForeground(Tema.ModoOscuro ? Color.WHITE : Tema.MORADO_UNAB);
                } else if (jl.getForeground() != Tema.NARANJA_UNAB && jl.getForeground() != Tema.EXITO && jl.getForeground() != Tema.PELIGRO) {
                    jl.setForeground(Tema.obtenerColorTexto());
                }
            } else if (c instanceof JTextField) {
                Tema.aplicarEstiloInput((JTextField) c);
            } else if (c instanceof JComboBox) {
                Tema.aplicarEstiloCombo((JComboBox<?>) c);
            }
            if (c instanceof Container) {
                actualizarComponentesRecursivo((Container) c);
            }
        }
    }
}
