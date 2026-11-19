package com.ui.components.truthTable;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import com.ui.styles.Palette;
import com.ui.styles.Styles;

// LISTA VERTICAL CON UNA SECCION POR CADA CIRCUITO, SE PUEDE DESPLAZAR DENTRO DE UN SCROLL
public class CircuitList extends JPanel implements Scrollable {
    private static final String EMPTY_MESSAGE = "Agrega compuertas al lienzo";
    private static final float EMPTY_MESSAGE_FONT_SIZE = 14f;
    private static final int SECTION_GAP = 12;
    private static final int SCROLL_UNIT = 16;
    
    private final JLabel emptyMessage = new JLabel(EMPTY_MESSAGE);
    
    // CREA LA LISTA VACIA
    public CircuitList() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Palette.SURFACE);
        
        emptyMessage.setForeground(Palette.MUTED);
        Styles.setFont(emptyMessage, EMPTY_MESSAGE_FONT_SIZE);
    }
    
    // MUESTRA UNA SECCION POR CADA TABLA, O UN MENSAJE SI TODAVIA NO HAY CIRCUITOS
    public void showCircuits(List<String[][]> truthTables) {
        removeAll();
        
        if (truthTables.isEmpty()) {
            add(emptyMessage);
        }
        
        addSections(truthTables);
        
        revalidate();
        repaint();
    }
    
    // TAMAÑO VISIBLE POR DEFECTO DENTRO DEL SCROLL
    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }
    
    // CUANTO SE MUEVE EL SCROLL CON CADA PASO DE LA RUEDA
    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return SCROLL_UNIT;
    }
    
    // CUANTO SE MUEVE EL SCROLL AL HACER CLIC EN LA BARRA
    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return orientation == SwingConstants.VERTICAL ? visibleRect.height : visibleRect.width;
    }
    
    // SIEMPRE SE AJUSTA AL ANCHO DEL PANEL PORQUE EL SCROLL HORIZONTAL LO TIENE CADA TABLA
    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }
    
    // EL ALTO NUNCA SE ESTIRA, SIEMPRE ES EL QUE NECESITAN LAS TABLAS
    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
    
    // AGREGA LAS SECCIONES UNA DEBAJO DE OTRA CON UN ESPACIO ENTRE ELLAS
    private void addSections(List<String[][]> truthTables) {
        for (int index = 0; index < truthTables.size(); index++) {
            if (index > 0) {
                add(Box.createVerticalStrut(SECTION_GAP));
            }
            
            add(new CircuitSection(truthTables.get(index), index + 1));
        }
    }
}