package com.ui.components.truthTable;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import com.ui.styles.Palette;

// DIBUJA CADA ENCABEZADO DE LA TABLA CON TEXTO CENTRADO Y FONDO DE ACENTO
public class HeaderRenderer extends DefaultTableCellRenderer {
    private static final Border HEADER_BORDER = BorderFactory.createMatteBorder(0, 0, 1, 1, Palette.LINE);
    
    // CREA EL DIBUJANTE CON EL TEXTO CENTRADO
    public HeaderRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    // PREPARA CADA ENCABEZADO CON SU FUENTE, SU COLOR Y UNA LINEA ABAJO Y A LA DERECHA
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
        super.getTableCellRendererComponent(table, value, selected, focused, row, column);
        
        setFont(table.getTableHeader().getFont());
        setForeground(Palette.INK);
        setBackground(Palette.ACCENT_TINT);
        setBorder(HEADER_BORDER);
        
        return this;
    }
}