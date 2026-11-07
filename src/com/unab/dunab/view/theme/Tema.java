package com.unab.dunab.view.theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Tema {
    public static boolean ModoOscuro = false;

    public static final Color NARANJA_UNAB = new Color(243, 112, 33);
    public static final Color MORADO_UNAB = new Color(74, 26, 94);
    public static final Color MORADO_OSCURO = new Color(52, 17, 67);
    public static final Color NARANJA_HOVER = new Color(255, 130, 50);
    public static final Color MORADO_HOVER = new Color(95, 34, 120);

    public static final Color GRIS_BORDE = new Color(210, 210, 215);
    public static final Color GRIS_BORDE_OSCURO = new Color(60, 60, 85);
    public static final Color TEXTO_OSCURO = new Color(30, 30, 35);
    public static final Color TEXTO_CLARO = new Color(240, 240, 250);
    public static final Color TEXTO_SECUNDARIO_CLARO = new Color(110, 110, 120);
    public static final Color TEXTO_SECUNDARIO_OSCURO = new Color(180, 180, 205);

    public static final Color FONDO_CLARO = new Color(248, 249, 252);
    public static final Color FONDO_OSCURO = new Color(30, 30, 46);
    public static final Color PANEL_CLARO = Color.WHITE;
    public static final Color PANEL_OSCURO = new Color(37, 37, 56);

    public static final Color INPUT_FONDO_CLARO = Color.WHITE;
    public static final Color INPUT_FONDO_OSCURO = new Color(45, 45, 68);

    public static final Color FILA_PAR_OSCURO = new Color(37, 37, 56);
    public static final Color FILA_IMPAR_OSCURO = new Color(43, 43, 64);

    public static final Color EXITO = new Color(34, 139, 34);
    public static final Color PELIGRO = new Color(204, 0, 0);

    public static Color getColorDeFondo() {
        return ModoOscuro ? FONDO_OSCURO : FONDO_CLARO;
    }

    public static Color obtenerColorPanel() {
        return ModoOscuro ? PANEL_OSCURO : PANEL_CLARO;
    }

    public static Color obtenerColorTexto() {
        return ModoOscuro ? TEXTO_CLARO : TEXTO_OSCURO;
    }

    public static Color obtenerColorTextoSecundario() {
        return ModoOscuro ? TEXTO_SECUNDARIO_OSCURO : TEXTO_SECUNDARIO_CLARO;
    }

    public static Color obtenerColorBorde() {
        return ModoOscuro ? GRIS_BORDE_OSCURO : GRIS_BORDE;
    }

    public static Color obtenerColorFondoInput() {
        return ModoOscuro ? INPUT_FONDO_OSCURO : INPUT_FONDO_CLARO;
    }

    public static Color obtenerColorBotonLogin() {
        return ModoOscuro ? MORADO_UNAB : NARANJA_UNAB;
    }

    public static Color obtenerColorBotonLoginHover() {
        return ModoOscuro ? MORADO_HOVER : NARANJA_HOVER;
    }

    public static void aplicarEstiloBotonPrimario(JButton boton, Color colorBase, Color colorHover) {
        if (boton == null) return;
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setBackground(colorBase);
        boton.setForeground(Color.WHITE);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));

        for (java.awt.event.MouseListener ml : boton.getMouseListeners()) {
            if (ml.getClass().getName().contains("Tema$")) {
                boton.removeMouseListener(ml);
            }
        }

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(colorHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(colorBase);
            }
        });
    }

    public static void aplicarEstiloInput(JTextField campo) {
        if (campo == null) return;
        campo.setOpaque(true);
        campo.setBackground(obtenerColorFondoInput());
        campo.setForeground(obtenerColorTexto());
        campo.setCaretColor(obtenerColorTexto());
        campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(obtenerColorBorde(), 1, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
    }

    public static void aplicarEstiloCombo(JComboBox<?> combo) {
        if (combo == null) return;
        combo.setOpaque(true);
        combo.setBackground(obtenerColorFondoInput());
        combo.setForeground(obtenerColorTexto());
        combo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        combo.setBorder(BorderFactory.createLineBorder(obtenerColorBorde(), 1, true));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    c.setBackground(NARANJA_UNAB);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(obtenerColorFondoInput());
                    c.setForeground(obtenerColorTexto());
                }
                return c;
            }
        });
    }

    public static void aplicarEstiloTabla(JTable tabla) {
        if (tabla == null) return;
        tabla.setOpaque(true);
        tabla.setBackground(obtenerColorPanel());
        tabla.setForeground(obtenerColorTexto());
        tabla.setGridColor(obtenerColorBorde());
        tabla.setSelectionBackground(NARANJA_UNAB);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JTableHeader header = tabla.getTableHeader();
        if (header != null) {
            header.setOpaque(true);
            header.setBackground(MORADO_UNAB);
            header.setForeground(Color.WHITE);
            header.setFont(new Font("SansSerif", Font.BOLD, 12));
            header.setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable t, Object val, boolean isSelected, boolean hasFocus, int row, int col) {
                    Component c = super.getTableCellRendererComponent(t, val, isSelected, hasFocus, row, col);
                    c.setBackground(MORADO_UNAB);
                    c.setForeground(Color.WHITE);
                    setFont(new Font("SansSerif", Font.BOLD, 12));
                    setHorizontalAlignment(SwingConstants.CENTER);
                    return c;
                }
            });
        }

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable t, Object val, boolean isSelected, boolean hasFocus, int row, int col) {
                    Component c = super.getTableCellRendererComponent(t, val, isSelected, hasFocus, row, col);
                    if (isSelected) {
                        c.setBackground(NARANJA_UNAB);
                        c.setForeground(Color.WHITE);
                    } else {
                        Color fondoFila = (row % 2 == 0) ? (ModoOscuro ? FILA_PAR_OSCURO : PANEL_CLARO) : (ModoOscuro ? FILA_IMPAR_OSCURO : new Color(242, 244, 248));
                        c.setBackground(fondoFila);
                        c.setForeground(obtenerColorTexto());
                    }
                    return c;
                }
            });
        }
    }
}
