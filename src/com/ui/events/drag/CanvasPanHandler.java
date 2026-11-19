package com.ui.events.drag;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

// DESPLAZA EL LIENZO AL ARRASTRAR CON CLIC IZQUIERDO SOBRE UN ESPACIO VACIO
public class CanvasPanHandler extends MouseAdapter {
    private final JComponent canvas;
    private final JViewport viewport;
    
    private Point pressPoint = null;
    private Point pressViewPosition = null;
    
    // CREA EL MANEJADOR PARA UN LIENZO Y LA VISTA QUE LO MUESTRA
    public CanvasPanHandler(JComponent canvas, JViewport viewport) {
        this.canvas = canvas;
        this.viewport = viewport;
    }
    
    // RECUERDA DONDE SE HIZO CLIC Y QUE PARTE DEL LIENZO SE ESTABA VIENDO
    @Override
    public void mousePressed(MouseEvent event) {
        if (!SwingUtilities.isLeftMouseButton(event)) return;
        
        pressPoint = toViewportPoint(event);
        pressViewPosition = viewport.getViewPosition();
        canvas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    // MUEVE LA VISTA EN SENTIDO CONTRARIO AL MOUSE PARA QUE EL LIENZO SIGA A LA MANO
    @Override
    public void mouseDragged(MouseEvent event) {
        if (pressPoint == null) return;
        
        Point currentPoint = toViewportPoint(event);
        int x = pressViewPosition.x + pressPoint.x - currentPoint.x;
        int y = pressViewPosition.y + pressPoint.y - currentPoint.y;
        
        viewport.setViewPosition(new Point(clampToCanvas(x, getMaxX()), clampToCanvas(y, getMaxY())));
    }
    
    // TERMINA EL DESPLAZAMIENTO
    @Override
    public void mouseReleased(MouseEvent event) {
        pressPoint = null;
        pressViewPosition = null;
        canvas.setCursor(Cursor.getDefaultCursor());
    }
    
    // CONVIERTE EL PUNTO DEL EVENTO A COORDENADAS DE LA VISTA, QUE NO SE MUEVEN CON EL LIENZO
    private Point toViewportPoint(MouseEvent event) {
        return SwingUtilities.convertPoint(canvas, event.getPoint(), viewport);
    }
    
    // CALCULA LA POSICION HORIZONTAL MAS LEJANA A LA QUE SE PUEDE LLEGAR
    private int getMaxX() {
        return canvas.getWidth() - viewport.getWidth();
    }
    
    // CALCULA LA POSICION VERTICAL MAS LEJANA A LA QUE SE PUEDE LLEGAR
    private int getMaxY() {
        return canvas.getHeight() - viewport.getHeight();
    }
    
    // LIMITA UNA POSICION PARA QUE ESTE ENTRE CERO Y EL MAXIMO
    private int clampToCanvas(int position, int max) {
        return Math.min(Math.max(0, position), Math.max(0, max));
    }
}