package com.ui.events.drag;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

// INICIA EL ARRASTRE DESDE LA PALETA CUANDO EL MOUSE SE ALEJA AL MENOS CINCO PÍXELES DEL PUNTO DEL CLIC
public class DragStartHandler extends MouseAdapter {
    private static final int DRAG_THRESHOLD = 5;
    
    private final JComponent component;
    private final TransferHandler transferHandler;
    private Point pressPoint;
    
    public DragStartHandler(JComponent component, TransferHandler transferHandler) {
        this.component = component;
        this.transferHandler = transferHandler;
    }
    
    @Override
    public void mousePressed(MouseEvent event) {
        pressPoint = event.getPoint();
    }
    
    @Override
    public void mouseReleased(MouseEvent event) {
        pressPoint = null;
    }
    
    @Override
    public void mouseDragged(MouseEvent event) {
        if (pressPoint == null) return;
        if (pressPoint.distance(event.getPoint()) < DRAG_THRESHOLD) return;
        
        transferHandler.exportAsDrag(component, event, TransferHandler.COPY);
        pressPoint = null;
    }
}