package com.ui.events;

import com.ui.circuit.Pin;
import com.ui.gates.SwitchIcon;
import com.ui.panels.Canvas;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

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
            
            // Clic derecho en un pin: desconecta inmediatamente el cable
            if (SwingUtilities.isRightMouseButton(event) && pin != null) {
                canvas.disconnectPin(pin);
                return;
            }

            // Clic izquierdo en un pin: inicia o reubica el cableado
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

        if (!hasDragged && component instanceof SwitchIcon) {
            ((SwitchIcon) component).toggle();
            if (component.getParent() instanceof Canvas) {
                ((Canvas) component.getParent()).evaluateCircuit();
            }
        }
    }

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
