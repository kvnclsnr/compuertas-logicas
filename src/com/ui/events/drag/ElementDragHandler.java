package com.ui.events.drag;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

import com.ui.circuit.pin.Pin;
import com.ui.icons.CircuitElement;
import com.ui.layouts.canvas.Canvas;

// PERMITE MOVER UN ELEMENTO DEL LIENZO, CONECTAR SUS PINES Y ACCIONARLO CON UN CLIC
public class ElementDragHandler extends MouseAdapter {
    private static final int CLICK_TOLERANCE = 3;
    
    private final CircuitElement element;
    private final Canvas canvas;
    
    private Point grabPoint = null;
    private boolean moved = false;
    private boolean wiring = false;
    
    // CREA EL MANEJADOR PARA UN ELEMENTO DE UN LIENZO
    public ElementDragHandler(CircuitElement element, Canvas canvas) {
        this.element = element;
        this.canvas = canvas;
    }
    
    // CLIC DERECHO EN UN PIN LO DESCONECTA, CLIC IZQUIERDO EN UN PIN EMPIEZA UN CABLE Y EN OTRO LUGAR EMPIEZA A MOVER
    @Override
    public void mousePressed(MouseEvent event) {
        Pin pin = element.findPinAt(event.getPoint());
        
        if (pin != null && SwingUtilities.isRightMouseButton(event)) {
            canvas.disconnectPin(pin);
        }
        
        else if (pin != null && SwingUtilities.isLeftMouseButton(event)) {
            wiring = true;
            canvas.startWiring(pin);
        }
        
        else if (SwingUtilities.isLeftMouseButton(event)) {
            startMoving(event.getPoint());
        }
    }
    
    // ARRASTRA EL CABLE O MUEVE EL ELEMENTO SEGUN LO QUE SE HAYA EMPEZADO
    @Override
    public void mouseDragged(MouseEvent event) {
        if (wiring) {
            canvas.dragWiring(toCanvasPoint(event));
        }
        
        else if (grabPoint != null) {
            moveElement(event);
        }
    }
    
    // TERMINA EL CABLE O EL MOVIMIENTO SEGUN LO QUE SE HAYA EMPEZADO
    @Override
    public void mouseReleased(MouseEvent event) {
        if (wiring) {
            wiring = false;
            canvas.finishWiring(toCanvasPoint(event));
        }
        
        else if (grabPoint != null) {
            finishMoving(event);
        }
    }
    
    // MUESTRA UNA CRUZ CUANDO EL MOUSE ESTA SOBRE UN PIN
    @Override
    public void mouseMoved(MouseEvent event) {
        boolean overPin = element.findPinAt(event.getPoint()) != null;
        int cursorType = overPin ? Cursor.CROSSHAIR_CURSOR : Cursor.DEFAULT_CURSOR;
        
        element.setCursor(Cursor.getPredefinedCursor(cursorType));
    }
    
    // GUARDA EL PUNTO DE AGARRE PARA QUE EL ELEMENTO NO SALTE AL MOVERLO
    private void startMoving(Point grabPoint) {
        this.grabPoint = grabPoint;
        moved = false;
        element.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }
    
    // MUEVE EL ELEMENTO SIGUIENDO AL MOUSE Y RECUERDA SI YA SE MOVIO LO SUFICIENTE PARA NO SER UN CLIC
    private void moveElement(MouseEvent event) {
        if (event.getPoint().distance(grabPoint) > CLICK_TOLERANCE) {
            moved = true;
        }
        
        Point mouseOnCanvas = toCanvasPoint(event);
        
        element.setLocation(mouseOnCanvas.x - grabPoint.x, mouseOnCanvas.y - grabPoint.y);
        canvas.repaint();
    }
    
    // SI FUE UN CLIC ACCIONA EL ELEMENTO, SI FUE UN MOVIMIENTO LO SUELTA EN EL LIENZO
    private void finishMoving(MouseEvent event) {
        grabPoint = null;
        element.setCursor(Cursor.getDefaultCursor());
        
        if (moved) {
            canvas.dropElement(element, event.getLocationOnScreen());
        }
        
        else {
            element.click();
            canvas.repaint();
        }
    }
    
    // CONVIERTE EL PUNTO DEL EVENTO A COORDENADAS DEL LIENZO
    private Point toCanvasPoint(MouseEvent event) {
        return SwingUtilities.convertPoint(element, event.getPoint(), canvas);
    }
}