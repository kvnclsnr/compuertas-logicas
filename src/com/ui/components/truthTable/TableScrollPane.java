package com.ui.components.truthTable;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import com.ui.styles.Palette;
import com.ui.styles.Styles;

// SCROLL PROPIO DE CADA TABLA, SOLO SE DESPLAZA HORIZONTALMENTE Y SU BARRA QUEDA PEGADA A LA TABLA
public class TableScrollPane extends JScrollPane {
    private static final int BORDER_WIDTH = 1;
    
    // CREA EL SCROLL CON LA TABLA ADENTRO, EL ENCABEZADO SE DESPLAZA JUNTO CON ELLA
    public TableScrollPane(CircuitTable table) {
        super(table, VERTICAL_SCROLLBAR_NEVER, HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        setBackground(Palette.SURFACE);
        getViewport().setBackground(Palette.SURFACE);
        Styles.setBorder(this, BORDER_WIDTH, BORDER_WIDTH, 0, 0, Palette.LINE);
        
        passWheelToParent();
        revalidateParentOnResize();
    }
    
    // SI LA BARRA HORIZONTAL HACE FALTA SU ALTO SE SUMA PARA QUE NO TAPE LA ULTIMA FILA
    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        
        if (needsHorizontalScroll(size.width)) {
            size.height += getHorizontalScrollBar().getPreferredSize().height;
        }
        
        return size;
    }
    
    // QUITA LA RUEDA DE ESTE SCROLL PARA QUE EL SCROLL VERTICAL DEL PANEL LA RECIBA
    private void passWheelToParent() {
        for (MouseWheelListener listener : getMouseWheelListeners()) {
            removeMouseWheelListener(listener);
        }
    }
    
    // AL CAMBIAR EL ANCHO PUEDE APARECER O DESAPARECER LA BARRA, ASI QUE SE RECALCULA EL ALTO
    private void revalidateParentOnResize() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                revalidateParent();
            }
        });
    }
    
    // UN SCROLL NO AVISA A QUIEN LO CONTIENE CUANDO SE REVALIDA, ASI QUE SE REVALIDA EL CONTENEDOR
    private void revalidateParent() {
        if (getParent() instanceof JComponent parent) {
            parent.revalidate();
        }
    }
    
    // DICE SI LA TABLA ES MAS ANCHA QUE EL ESPACIO QUE TENDRA EL SCROLL
    private boolean needsHorizontalScroll(int tableWidth) {
        int availableWidth = getAvailableWidth();
        
        return availableWidth > 0 && availableWidth < tableWidth;
    }
    
    // DEVUELVE EL ANCHO QUE TENDRA EL SCROLL TOMADO DEL PANEL QUE LO CONTIENE, QUE YA TIENE ANCHO
    private int getAvailableWidth() {
        JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, this);
        
        if (viewport == null) {
            return getWidth();
        }
        
        Insets insets = getInsets();
        
        return viewport.getWidth() - insets.left - insets.right;
    }
}