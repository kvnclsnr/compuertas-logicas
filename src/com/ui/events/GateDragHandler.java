package com.ui.events;

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
        if (!SwingUtilities.isLeftMouseButton(event) || component.getParent() == null) {
            return;
        }
        
        dragOffset = event.getPoint();
        component.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }
    
    @Override
    public void mouseDragged(MouseEvent event) {
        if (dragOffset == null) {
            return;
        }
        
        Component parent = component.getParent();
        if (parent == null) {
            return;
        }
        
        Point mousePosition = SwingUtilities.convertPoint(component, event.getPoint(), parent);
        component.setLocation(mousePosition.x - dragOffset.x, mousePosition.y - dragOffset.y);
    }
    
    @Override
    public void mouseReleased(MouseEvent event) {
        dragOffset = null;
        component.setCursor(Cursor.getDefaultCursor());
    }
}
