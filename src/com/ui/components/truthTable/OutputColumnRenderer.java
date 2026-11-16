package com.ui.components.truthTable;

import com.ui.styles.Palette;
import com.ui.styles.Styles;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class OutputColumnRenderer extends DefaultTableCellRenderer {
    private final int outputColumn;
    
    // CREA EL RENDERER CON EL TEXTO CENTRADO Y EL NÚMERO DE LA COLUMNA DE SALIDA
    public OutputColumnRenderer(int outputColumn) {
        this.outputColumn = outputColumn;
        
        setHorizontalAlignment(JLabel.CENTER);
    }
    
    // PREPARA CADA CELDA: FONDO DE ACENTO SUAVE SI ES LA SALIDA Y FONDO NORMAL SI NO
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
        super.getTableCellRendererComponent(table, value, selected, focused, row, column);
        
        if (column == outputColumn) {
            Styles.setBackground(this, Palette.ACCENT_TINT);
        }
        
        else {
            Styles.setBackground(this, Palette.SURFACE);
        }
        
        return this;
    }
}