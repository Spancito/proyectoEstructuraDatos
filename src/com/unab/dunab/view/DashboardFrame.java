package com.unab.dunab.view;

import com.unab.dunab.view.theme.Tema;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenido;
    private String usuario;

    private JPanel menuLateral;
    private JPanel barraSuperior;
    private JButton botonAlternarTema;
    private JLabel etiquetaTituloBarra;
    private JLabel etiquetaUsuarioBarra;
    private String[] secciones;

    public DashboardFrame(String usuario) {
        this.usuario = usuario;

        setTitle("DUNAB - Panel principal");
        setSize(1250, 780);
        setMinimumSize(new Dimension(950, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        secciones = new String[]{
            "Inicio", "Mi perfil", "Resumen financiero", "Encuentros",
            "Participantes", "Notificaciones", "Ranking", "Mi progreso",
            "Roles y permisos", "Generar reporte"
        };

        crearMenuLateral();
        crearContenido();

        getContentPane().setBackground(Tema.getColorDeFondo());
    }

    private void crearMenuLateral() {
        menuLateral = new JPanel(new BorderLayout());
        menuLateral.setBackground(Tema.MORADO);
        menuLateral.setPreferredSize(new Dimension(245, 0));

        JPanel encabezado = new JPanel();
        encabezado.setBackground(Tema.MORADO);
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.setBorder(new EmptyBorder(30, 20, 25, 20));

        JLabel logo = new JLabel("DUNAB");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 30));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Panel universitario");
        subtitulo.setForeground(new Color(220, 215, 235));
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        encabezado.add(logo);
        encabezado.add(Box.createVerticalStrut(8));
        encabezado.add(subtitulo);

        JPanel opciones = new JPanel();
        opciones.setBackground(Tema.MORADO);
        opciones.setLayout(new GridLayout(0, 1, 0, 4));
        opciones.setBorder(new EmptyBorder(5, 12, 5, 12));

        for (String nombre : secciones) {
            JButton boton = new JButton(nombre);
            boton.setFont(new Font("Arial", Font.PLAIN, 13));
            boton.setForeground(Color.WHITE);
            boton.setBackground(Tema.MORADO);
            boton.setHorizontalAlignment(SwingConstants.LEFT);
            boton.setFocusPainted(false);
            boton.setBorder(new EmptyBorder(10, 15, 10, 10));
            boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            boton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    boton.setBackground(new Color(90, 25, 150));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    boton.setBackground(Tema.MORADO);
                }
            });

            boton.addActionListener(e -> mostrarSeccion(nombre));
            opciones.add(boton);
        }

        JScrollPane scrollOpciones = new JScrollPane(opciones);
        scrollOpciones.setBorder(null);
        scrollOpciones.setBackground(Tema.MORADO);
        scrollOpciones.getViewport().setBackground(Tema.MORADO);
        scrollOpciones.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton cerrarSesion = new JButton("Cerrar sesión");
        cerrarSesion.setFont(new Font("Arial", Font.BOLD, 13));
        cerrarSesion.setBackground(Tema.NARANJA);
        cerrarSesion.setForeground(Color.WHITE);
        cerrarSesion.setFocusPainted(false);
        cerrarSesion.setBorder(new EmptyBorder(12, 10, 12, 10));
        cerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cerrarSesion.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas cerrar sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                LoginFrame login = new LoginFrame(new com.unab.dunab.controller.Autenticacion());
                login.setVisible(true);
                dispose();
            }
        });

        menuLateral.add(encabezado, BorderLayout.NORTH);
        menuLateral.add(scrollOpciones, BorderLayout.CENTER);

        JPanel inferior = new JPanel(new BorderLayout());
        inferior.setBackground(Tema.MORADO);
        inferior.setBorder(new EmptyBorder(15, 15, 20, 15));
        inferior.add(cerrarSesion, BorderLayout.CENTER);

        menuLateral.add(inferior, BorderLayout.SOUTH);

        add(menuLateral, BorderLayout.WEST);
    }

    private void crearContenido() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(Tema.getColorDeFondo());

        barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(Tema.obtenerColorPanel());
        barraSuperior.setBorder(new EmptyBorder(15, 25, 15, 25));

        etiquetaTituloBarra = new JLabel("Bienvenido a DUNAB");
        etiquetaTituloBarra.setFont(new Font("Arial", Font.BOLD, 20));
        etiquetaTituloBarra.setForeground(Tema.MORADO);

        JPanel panelDerechoBarra = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelDerechoBarra.setOpaque(false);

        etiquetaUsuarioBarra = new JLabel("Usuario: " + usuario);
        etiquetaUsuarioBarra.setFont(new Font("Arial", Font.PLAIN, 14));
        etiquetaUsuarioBarra.setForeground(Tema.obtenerColorTexto());

        botonAlternarTema = new JButton(Tema.ModoOscuro ? "☀️ Modo Claro" : "🌙 Modo Oscuro");
        botonAlternarTema.setFont(new Font("Arial", Font.BOLD, 12));
        botonAlternarTema.setForeground(Tema.ModoOscuro ? Color.WHITE : Tema.MORADO);
        botonAlternarTema.setBackground(Tema.ModoOscuro ? new Color(60, 60, 60) : new Color(240, 235, 250));
        botonAlternarTema.setFocusPainted(false);
        botonAlternarTema.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.MORADO, 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        botonAlternarTema.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonAlternarTema.addActionListener(e -> alternarTemaDashboard());

        panelDerechoBarra.add(etiquetaUsuarioBarra);
        panelDerechoBarra.add(botonAlternarTema);

        barraSuperior.add(etiquetaTituloBarra, BorderLayout.WEST);
        barraSuperior.add(panelDerechoBarra, BorderLayout.EAST);

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(Tema.getColorDeFondo());
        panelContenido.setBorder(new EmptyBorder(25, 25, 25, 25));

        for (String seccion : secciones) {
            panelContenido.add(crearPanelSeccionEspecifico(seccion), seccion);
        }

        principal.add(barraSuperior, BorderLayout.NORTH);
        principal.add(panelContenido, BorderLayout.CENTER);

        add(principal, BorderLayout.CENTER);

        cardLayout.show(panelContenido, "Inicio");
    }

    private JPanel crearPanelSeccionEspecifico(String nombre) {
        JPanel panel = new JPanel();
        panel.setBackground(Tema.getColorDeFondo());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel(nombre);
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(Tema.MORADO);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descripcion = new JLabel(obtenerDescripcionSeccion(nombre));
        descripcion.setFont(new Font("Arial", Font.PLAIN, 14));
        descripcion.setForeground(Tema.obtenerColorTexto());
        descripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(descripcion);
        panel.add(Box.createVerticalStrut(20));

        JPanel contenidoEspecifico = construirContenidoPorNombre(nombre);
        panel.add(contenidoEspecifico);

        return panel;
    }

    private String obtenerDescripcionSeccion(String nombre) {
        switch (nombre) {
            case "Inicio": return "Panel general con accesos directos y estado actual de tu cuenta.";
            case "Mi perfil": return "Consulta y actualiza tu información personal y académica.";
            case "Resumen financiero": return "Estado de cuenta, pagos pendientes y historial de transacciones.";
            case "Encuentros": return "Gestión y programación de asesorías, tutorías y reuniones.";
            case "Participantes": return "Directorio de compañeros, profesores y coordinadores.";
            case "Notificaciones": return "Avisos importantes, alertas y comunicados institucionales.";
            case "Ranking": return "Tabla de posiciones y desempeño académico general.";
            case "Mi progreso": return "Seguimiento de materias cursadas, créditos y calificaciones.";
            case "Roles y permisos": return "Configuración de privilegios de usuario en el sistema.";
            case "Generar reporte": return "Exportación de certificados e informes detallados en PDF.";
            default: return "Módulo de gestión del sistema DUNAB.";
        }
    }

    private JPanel construirContenidoPorNombre(String nombre) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Tema.obtenerColorPanel());
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.GRIS, 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);

        switch (nombre) {
            case "Inicio":
                JPanel panelInicio = new JPanel();
                panelInicio.setLayout(new BoxLayout(panelInicio, BoxLayout.Y_AXIS));
                panelInicio.setOpaque(false);

                JPanel metricas = new JPanel(new GridLayout(1, 3, 15, 0));
                metricas.setOpaque(false);
                metricas.add(crearTarjetaMetrica("Créditos Aprobados", "36 / 48", Tema.MORADO));
                metricas.add(crearTarjetaMetrica("Promedio Ponderado", "4.45", Tema.NARANJA));
                metricas.add(crearTarjetaMetrica("Encuentros Activos", "2 Próximos", new Color(40, 130, 90)));
                metricas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

                JPanel graficoSimulado = new PanelGraficoBarrasEstilizado();
                graficoSimulado.setPreferredSize(new Dimension(0, 220));

                panelInicio.add(metricas);
                panelInicio.add(Box.createVerticalStrut(20));
                panelInicio.add(graficoSimulado);
                tarjeta.add(panelInicio, BorderLayout.CENTER);
                break;

            case "Mi perfil":
                JPanel panelPerfil = new JPanel();
                panelPerfil.setLayout(new BoxLayout(panelPerfil, BoxLayout.Y_AXIS));
                panelPerfil.setOpaque(false);
                panelPerfil.add(crearLineaDatos("Nombre completo:", usuario.toUpperCase()));
                panelPerfil.add(Box.createVerticalStrut(10));
                panelPerfil.add(crearLineaDatos("Código institucional:", "U202610485"));
                panelPerfil.add(Box.createVerticalStrut(10));
                panelPerfil.add(crearLineaDatos("Programa:", "Ingeniería de Sistemas / Computación"));
                panelPerfil.add(Box.createVerticalStrut(10));
                panelPerfil.add(crearLineaDatos("Correo electrónico:", usuario.toLowerCase() + "@unab.edu.co"));
                tarjeta.add(panelPerfil, BorderLayout.CENTER);
                break;

            case "Resumen financiero":
                String[] columnasFin = {"Concepto", "Referencia", "Valor", "Estado"};
                Object[][] datosFin = {
                    {"Matrícula Periodo 2026-1", "REF-98412", "$ 2,450,000", "Pagado"},
                    {"Derechos de Grado / Carné", "REF-98413", "$ 85,000", "Pendiente"}
                };
                tarjeta.add(crearTablaDinamica(columnasFin, datosFin), BorderLayout.CENTER);
                break;

            case "Encuentros":
                String[] columnasEnc = {"Asesor / Tutor", "Materia", "Fecha y Hora", "Modalidad"};
                Object[][] datosEnc = {
                    {"Dr. Raúl Ortiz", "Electromagnetismo", "10 Nov 2026 - 10:00 AM", "Presencial"},
                    {"Dra. Marcela Gomez", "Estructura de Datos", "12 Nov 2026 - 02:30 PM", "Virtual"}
                };
                tarjeta.add(crearTablaDinamica(columnasEnc, datosEnc), BorderLayout.CENTER);
                break;

            case "Participantes":
                String[] columnasPart = {"Nombre", "Rol", "Facultad", "Contacto"};
                Object[][] datosPart = {
                    {"Paulandrea Basto", "Estudiante / Par", "Ingeniería", "pbasto@unab.edu.co"},
                    {"Gabriela Zafra", "Estudiante / Par", "Ingeniería", "gzafra@unab.edu.co"},
                    {"Raúl Ortiz Rojas", "Profesor Supervisor", "Ciencias Exactas", "rortiz@unab.edu.co"}
                };
                tarjeta.add(crearTablaDinamica(columnasPart, datosPart), BorderLayout.CENTER);
                break;

            case "Notificaciones":
                JPanel panelNoti = new JPanel();
                panelNoti.setLayout(new BoxLayout(panelNoti, BoxLayout.Y_AXIS));
                panelNoti.setOpaque(false);
                panelNoti.add(crearItemNotificacion("⚠️ Recordatorio:", "Entrega de informe final de laboratorio programada para el viernes."));
                panelNoti.add(Box.createVerticalStrut(12));
                panelNoti.add(crearItemNotificacion("📢 Aviso General:", "Apertura de inscripciones para proyectos de investigación formativa."));
                tarjeta.add(panelNoti, BorderLayout.CENTER);
                break;

            case "Ranking":
                String[] columnasRank = {"Puesto", "Estudiante", "Programa", "Promedio"};
                Object[][] datosRank = {
                    {"1", "Carlos Mendoza", "Ingeniería Financiera", "4.82"},
                    {"2", usuario.toUpperCase(), "Ingeniería de Sistemas", "4.45"},
                    {"3", "Lucía Benítez", "Medicina", "4.40"}
                };
                tarjeta.add(crearTablaDinamica(columnasRank, datosRank), BorderLayout.CENTER);
                break;

            case "Mi progreso":
                String[] columnasProg = {"Asignatura", "Créditos", "Nota Parcial", "Estado"};
                Object[][] datosProg = {
                    {"Estructura de Datos", "4", "4.6", "Cursando"},
                    {"Electromagnetismo", "3", "4.3", "Cursando"},
                    {"Matemáticas Discretas", "3", "4.5", "Aprobado"}
                };
                tarjeta.add(crearTablaDinamica(columnasProg, datosProg), BorderLayout.CENTER);
                break;

            case "Roles y permisos":
                JPanel panelRoles = new JPanel();
                panelRoles.setLayout(new BoxLayout(panelRoles, BoxLayout.Y_AXIS));
                panelRoles.setOpaque(false);
                panelRoles.add(crearLineaDatos("Perfil asignado:", "Estudiante Regular"));
                panelRoles.add(Box.createVerticalStrut(10));
                panelRoles.add(crearLineaDatos("Nivel de acceso:", "Lectura y registro en módulos académicos"));
                panelRoles.add(Box.createVerticalStrut(10));
                panelRoles.add(crearLineaDatos("Permiso especial:", "Acceso a laboratorios de física y cómputo"));
                tarjeta.add(panelRoles, BorderLayout.CENTER);
                break;

            case "Generar reporte":
                JPanel panelReporte = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
                panelReporte.setOpaque(false);
                JButton btnPdf = new JButton("📄 Descargar Certificado de Notas (PDF)");
                btnPdf.setFont(new Font("Arial", Font.BOLD, 14));
                btnPdf.setBackground(Tema.NARANJA);
                btnPdf.setForeground(Color.WHITE);
                btnPdf.setFocusPainted(false);
                btnPdf.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnPdf.addActionListener(e -> JOptionPane.showMessageDialog(this, "Reporte generado y descargado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE));
                panelReporte.add(btnPdf);
                tarjeta.add(panelReporte, BorderLayout.CENTER);
                break;
        }

        return tarjeta;
    }

    private JPanel crearTarjetaMetrica(String titulo, String valor, Color colorAccent) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Tema.obtenerColorPanel());
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Tema.GRIS, 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        JLabel t = new JLabel(titulo);
        t.setFont(new Font("Arial", Font.PLAIN, 13));
        t.setForeground(Color.GRAY);
        JLabel v = new JLabel(valor);
        v.setFont(new Font("Arial", Font.BOLD, 22));
        v.setForeground(colorAccent);
        p.add(t);
        p.add(Box.createVerticalStrut(8));
        p.add(v);
        return p;
    }

    private JLabel crearLineaDatos(String etiqueta, String valor) {
        JLabel lbl = new JLabel("<html><b>" + etiqueta + "</b> " + valor + "</html>");
        lbl.setFont(new Font("Arial", Font.PLAIN, 15));
        lbl.setForeground(Tema.obtenerColorTexto());
        return lbl;
    }

    private JLabel crearItemNotificacion(String titulo, String desc) {
        JLabel lbl = new JLabel("<html><b>" + titulo + "</b> " + desc + "</html>");
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        lbl.setForeground(Tema.obtenerColorTexto());
        return lbl;
    }

    private JScrollPane crearTablaDinamica(String[] columnas, Object[][] datos) {
        JTable tabla = new JTable(new DefaultTableModel(datos, columnas));
        tabla.setFont(new Font("Arial", Font.PLAIN, 13));
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(Tema.MORADO);
        tabla.getTableHeader().setForeground(Color.WHITE);
        JScrollPane sp = new JScrollPane(tabla);
        sp.setPreferredSize(new Dimension(0, 160));
        return sp;
    }

    private class PanelGraficoBarrasEstilizado extends JPanel {
        public PanelGraficoBarrasEstilizado() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(Tema.MORADO);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("Rendimiento Académico por Periodo", 15, 20);

            g2.setColor(Color.GRAY);
            g2.drawLine(40, h - 30, w - 20, h - 30);

            int[] valores = {65, 80, 45, 90, 75};
            String[] etiquetas = {"2024-2", "2025-1", "2025-2", "2026-1", "Actual"};
            int anchoBarra = 40;
            int espacio = (w - 80 - (anchoBarra * valores.length)) / (valores.length + 1);

            for (int i = 0; i < valores.length; i++) {
                int x = 60 + i * (anchoBarra + espacio);
                int alturaBarra = (int) ((h - 80) * (valores[i] / 100.0));
                int y = h - 30 - alturaBarra;

                if (i == valores.length - 1) {
                    g2.setColor(Tema.NARANJA);
                } else {
                    g2.setColor(Tema.MORADO);
                }

                g2.fillRect(x, y, anchoBarra, alturaBarra);

                g2.setColor(Tema.obtenerColorTexto());
                g2.setFont(new Font("Arial", Font.PLAIN, 11));
                g2.drawString(etiquetas[i], x, h - 15);
            }
        }
    }

    private void mostrarSeccion(String nombre) {
        etiquetaTituloBarra.setText(nombre.equals("Inicio") ? "Bienvenido a DUNAB" : nombre);
        cardLayout.show(panelContenido, nombre);
    }

    private void alternarTemaDashboard() {
        Tema.ModoOscuro = !Tema.ModoOscuro;
        
        botonAlternarTema.setText(Tema.ModoOscuro ? "☀️ Modo Claro" : "🌙 Modo Oscuro");
        botonAlternarTema.setBackground(Tema.ModoOscuro ? new Color(60, 60, 60) : new Color(240, 235, 250));
        botonAlternarTema.setForeground(Tema.ModoOscuro ? Color.WHITE : Tema.MORADO);

        getContentPane().setBackground(Tema.getColorDeFondo());
        barraSuperior.setBackground(Tema.obtenerColorPanel());
        etiquetaTituloBarra.setForeground(Tema.MORADO);
        etiquetaUsuarioBarra.setForeground(Tema.obtenerColorTexto());
        panelContenido.setBackground(Tema.getColorDeFondo());

        for (Component comp : panelContenido.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(Tema.getColorDeFondo());
                for (Component subComp : ((JPanel) comp).getComponents()) {
                    if (subComp instanceof JLabel && subComp != comp.getComponent(0) && subComp != comp.getComponent(1)) {
                        subComp.setForeground(Tema.obtenerColorTexto());
                    } else if (subComp instanceof JPanel) {
                        subComp.setBackground(Tema.obtenerColorPanel());
                        subComp.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Tema.GRIS, 1, true),
                            new EmptyBorder(20, 20, 20, 20)
                        ));
                    }
                }
            }
        }

        SwingUtilities.updateComponentTreeUI(this);
        this.repaint();
    }
}