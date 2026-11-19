package com.ui.events.drag;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

// INICIA EL ARRASTRE DESDE LA PALETA CUANDO EL MOUSE SE ALEJA UNOS PIXELES DEL CLIC
public class DragStartHandler extends MouseAdapter {
    private static final int DRAG_THRESHOLD = 5;
    
    private final JComponent component;
    private final TransferHandler transferHandler;
    private Point pressPoint = null;
    
    // CREA EL MANEJADOR PARA UN COMPONENTE Y SU TRANSFERENCIA
    public DragStartHandler(JComponent component, TransferHandler transferHandler) {
        this.component = component;
        this.transferHandler = transferHandler;
    }
    
    // RECUERDA DONDE SE HIZO CLIC
    @Override
    public void mousePressed(MouseEvent event) {
        pressPoint = event.getPoint();
    }
    
    // OLVIDA EL CLIC AL SOLTAR EL MOUSE
    @Override
    public void mouseReleased(MouseEvent event) {
        pressPoint = null;
    }
    
    // EMPIEZA EL ARRASTRE CUANDO EL MOUSE SE ALEJO LO SUFICIENTE
    @Override
    public void mouseDragged(MouseEvent event) {
        if (pressPoint == null) return;
        if (pressPoint.distance(event.getPoint()) < DRAG_THRESHOLD) return;
        
        transferHandler.exportAsDrag(component, event, TransferHandler.COPY);
        pressPoint = null;
    }
}