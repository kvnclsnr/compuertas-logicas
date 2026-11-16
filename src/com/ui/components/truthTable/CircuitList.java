package com.ui.components.truthTable;

import com.ui.styles.Palette;
import com.ui.styles.Styles;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

public class CircuitList extends JPanel implements Scrollable {
    private static final int SECTION_GAP = 12;
    private static final int SCROLL_UNIT = 16;
    
    private final JLabel emptyMessage = new JLabel("Agrega compuertas al lienzo");
    
    // CREA LA LISTA VERTICAL, VACÍA POR AHORA
    public CircuitList() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        Styles.setBackground(this, Palette.SURFACE);
        Styles.setForeground(emptyMessage, Palette.MUTED);
    }
    
    // MUESTRA UNA SECCIÓN POR CADA TABLA, O UN MENSAJE SI TODAVÍA NO HAY CIRCUITOS
    public void showCircuits(List<String[][]> truthTables) {
        removeAll();
        
        if (truthTables.isEmpty()) {
            add(emptyMessage);
        }
        
        for (int index = 0; index < truthTables.size(); index++) {
            if (index > 0) {
                add(Box.createVerticalStrut(SECTION_GAP));
            }
            
            add(new CircuitSection(truthTables.get(index), index + 1));
        }
        
        revalidate();
        repaint();
    }
    
    // TAMAÑO VISIBLE POR DEFECTO DENTRO DEL SCROLL
    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }
    
    // CUÁNTO SE MUEVE EL SCROLL CON CADA PASO DE LA RUEDA DEL MOUSE
    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return SCROLL_UNIT;
    }
    
    // CUÁNTO SE MUEVE EL SCROLL AL HACER CLIC EN LA BARRA
    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.VERTICAL) {
            return visibleRect.height;
        }
        
        return visibleRect.width;
    }
    
    // SIEMPRE SE AJUSTA AL ANCHO DEL PANEL: EL SCROLL HORIZONTAL LO TIENE CADA TABLA
    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }
    
    // EL ALTO NUNCA SE ESTIRA: SIEMPRE ES EL QUE NECESITAN LAS TABLAS
    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}