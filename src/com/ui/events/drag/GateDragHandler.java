package com.ui.events.drag;

import com.ui.circuit.pin.Pin;
import com.ui.icons.Switch;
import com.ui.layouts.canvas.Canvas;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

// PERMITE MOVER LAS COMPUERTAS, CONECTAR SUS PINES Y ACCIONAR LOS INTERRUPTORES
public class GateDragHandler extends MouseAdapter {
    private final JComponent component;
    private Point dragOffset;
    private boolean hasDragged = false;
    private boolean isWiring = false;
    
    public GateDragHandler(JComponent component) {
        if (component == null) {
            throw new IllegalArgumentException("The component cannot be null.");
        }
        
        this.component = component;
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }
    
    @Override
    public void mousePressed(MouseEvent event) {
        if (component.getParent() instanceof Canvas) {
            Canvas canvas = (Canvas) component.getParent();
            Pin pin = canvas.findPinAt(component, event.getPoint());
            
            // EL CLIC DERECHO SOBRE UN PIN DESCONECTA SU CABLE
            if (SwingUtilities.isRightMouseButton(event) && pin != null) {
                canvas.disconnectPin(pin);
                return;
            }
            
            // EL CLIC IZQUIERDO SOBRE UN PIN PERMITE CONECTAR O MOVER UN CABLE
            if (SwingUtilities.isLeftMouseButton(event) && pin != null) {
                isWiring = true;
                canvas.startWiring(pin);
                return;
            }
        }
        
        if (!SwingUtilities.isLeftMouseButton(event) || component.getParent() == null) {
            return;
        }
        
        isWiring = false;
        
        // GUARDA EL PUNTO DE AGARRE PARA QUE LA COMPUERTA NO SALTE AL MOVERLA
        
        dragOffset = event.getPoint();
        hasDragged = false;
        component.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }
    
    @Override
    public void mouseDragged(MouseEvent event) {
        if (isWiring) {
            if (component.getParent() instanceof Canvas) {
                Canvas canvas = (Canvas) component.getParent();
                Point canvasPoint = SwingUtilities.convertPoint(component, event.getPoint(), canvas);
                canvas.dragWiring(canvasPoint);
            }
            
            return;
        }
        
        if (dragOffset == null) {
            return;
        }
        
        // DISTINGUE UN CLIC DE UN ARRASTRE SEGÚN CUÁNTO SE MOVIÓ EL MOUSE
        if (event.getPoint().distance(dragOffset) > 3) {
            hasDragged = true;
        }
        
        Component parent = component.getParent();
        if (parent == null) {
            return;
        }
        
        Point mousePosition = SwingUtilities.convertPoint(component, event.getPoint(), parent);
        component.setLocation(mousePosition.x - dragOffset.x, mousePosition.y - dragOffset.y);
        parent.repaint();
    }
    
    @Override
    public void mouseReleased(MouseEvent event) {
        if (isWiring) {
            isWiring = false;
            if (component.getParent() instanceof Canvas) {
                Canvas canvas = (Canvas) component.getParent();
                Point canvasPoint = SwingUtilities.convertPoint(component, event.getPoint(), canvas);
                canvas.finishWiring(canvasPoint);
            }
            return;
        }
        
        dragOffset = null;
        component.setCursor(Cursor.getDefaultCursor());
        
        // CAMBIO DESDE AQUÍ: SI SE SOLTÓ SOBRE LA PALETA SE BORRA; SI SE SOLTÓ FUERA DEL LIENZO SE VUELVE A METER
        if (hasDragged && component.getParent() instanceof Canvas canvas) {
            if (canvas.isOverDeleteZone(event.getLocationOnScreen())) {
                canvas.removeGate(component);
                return;
            }
            
            canvas.keepInside(component);
        }
        // CAMBIO HASTA AQUÍ
        
        // SI SE HIZO CLIC EN UN INTERRUPTOR, CAMBIA SU ESTADO Y VUELVE A EVALUAR EL CIRCUITO
        if (!hasDragged && component instanceof Switch) {
            ((Switch) component).toggle();
            
            if (component.getParent() instanceof Canvas) {
                ((Canvas) component.getParent()).evaluateCircuit();
            }
        }
    }
    
    // MUESTRA UN CURSOR EN FORMA DE CRUZ CUANDO EL MOUSE PASA SOBRE UN PIN
    @Override
    public void mouseMoved(MouseEvent event) {
        if (component.getParent() instanceof Canvas) {
            Canvas canvas = (Canvas) component.getParent();
            Pin pin = canvas.findPinAt(component, event.getPoint());
            if (pin != null) {
                component.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                return;
            }
        }
        component.setCursor(Cursor.getDefaultCursor());
    }
}
