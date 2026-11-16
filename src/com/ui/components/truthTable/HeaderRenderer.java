package com.ui.components.truthTable;

import com.ui.styles.Palette;
import com.ui.styles.Styles;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

public class HeaderRenderer extends DefaultTableCellRenderer {
    private static final Border HEADER_BORDER = BorderFactory.createMatteBorder(0, 0, 1, 1, Palette.LINE);
    
    // CREA EL RENDERER CON EL TEXTO CENTRADO
    public HeaderRenderer() {
        setHorizontalAlignment(JLabel.CENTER);
    }
    
    // PREPARA CADA ENCABEZADO: FONDO DE ACENTO SUAVE Y UNA LÍNEA ABAJO Y A LA DERECHA
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
        super.getTableCellRendererComponent(table, value, selected, focused, row, column);
        
        setFont(table.getTableHeader().getFont());
        Styles.setForeground(this, Palette.INK);
        Styles.setBackground(this, Palette.ACCENT_TINT);
        Styles.setBorder(this, HEADER_BORDER);
        
        return this;
    }
}