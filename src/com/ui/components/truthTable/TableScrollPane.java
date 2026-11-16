package com.ui.components.truthTable;

import com.ui.styles.Palette;
import com.ui.styles.Styles;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

// SCROLL PROPIO DE CADA TABLA: SOLO SE DESPLAZA HORIZONTALMENTE, Y SU BARRA QUEDA PEGADA A LA TABLA
public class TableScrollPane extends JScrollPane {
    
    // CREA EL SCROLL CON LA TABLA ADENTRO; EL ENCABEZADO SE DESPLAZA JUNTO CON ELLA
    public TableScrollPane(CircuitTable table) {
        super(table, VERTICAL_SCROLLBAR_NEVER, HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        Styles.setBackground(this, Palette.SURFACE);
        Styles.setBackground(getViewport(), Palette.SURFACE);
        
        // LÍNEA ARRIBA E IZQUIERDA; LA DE ABAJO Y LA DERECHA LAS DIBUJAN LAS CELDAS
        Styles.setBorder(this, 1, 1, 0, 0, Palette.LINE);
        
        passWheelToParent();
        
        // AL CAMBIAR EL ANCHO PUEDE APARECER O DESAPARECER LA BARRA, ASÍ QUE SE RECALCULA EL ALTO
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                revalidateParent();
            }
        });
    }
    
    // SI LA BARRA HORIZONTAL HACE FALTA, SU ALTO SE SUMA PARA QUE NO TAPE LA ÚLTIMA FILA
    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        
        if (needsHorizontalScroll(size.width)) {
            size.height += getHorizontalScrollBar().getPreferredSize().height;
        }
        
        return size;
    }
    
    // UN JSCROLLPANE NO AVISA A QUIEN LO CONTIENE CUANDO REVALIDA, ASÍ QUE SE REVALIDA EL CONTENEDOR
    // SI NO, LA LISTA NUNCA SE ENTERA DE QUE ESTE SCROLL NECESITA MÁS ALTO PARA SU BARRA
    private void revalidateParent() {
        if (getParent() instanceof JComponent parent) {
            parent.revalidate();
        }
    }
    
    // DICE SI LA TABLA ES MÁS ANCHA QUE EL ESPACIO QUE TENDRÁ EL SCROLL
    private boolean needsHorizontalScroll(int tableWidth) {
        int availableWidth = getAvailableWidth();
        
        return availableWidth > 0 && availableWidth < tableWidth;
    }
    
    // DEVUELVE EL ANCHO QUE TENDRÁ EL SCROLL, AUNQUE TODAVÍA NO SE HAYA ACOMODADO
    // SE TOMA DEL PANEL EN EL QUE ESTÁ, QUE YA TIENE ANCHO, PARA NO ESPERAR A QUE EL SCROLL TENGA EL SUYO
    private int getAvailableWidth() {
        JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, this);
        
        if (viewport == null) {
            return getWidth();
        }
        
        Insets insets = getInsets();
        
        return viewport.getWidth() - insets.left - insets.right;
    }
    
    // QUITA LA RUEDA DE ESTE SCROLL PARA QUE EL SCROLL VERTICAL DEL PANEL LA RECIBA
    private void passWheelToParent() {
        for (MouseWheelListener listener : getMouseWheelListeners()) {
            removeMouseWheelListener(listener);
        }
    }
}