package com.ui.circuit.wire;
import com.ui.circuit.pin.Pin;

import com.ui.styles.Palette;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.CubicCurve2D;

// El cable punteado que sigue al mouse mientras se arrastra desde un pin
public class PendingWire {
    private Pin pin = null;
    private Point mousePoint = null;
    
    public boolean isActive() {
        return pin != null;
    }
    
    public Pin getPin() {
        return pin;
    }
    
    public void start(Pin pin, Point mousePoint) {
        this.pin = pin;
        this.mousePoint = mousePoint;
    }
    
    public void moveTo(Point mousePoint) {
        this.mousePoint = mousePoint;
    }
    
    public void clear() {
        this.pin = null;
        this.mousePoint = null;
    }
    
    public void draw(Graphics2D g2d) {
        if (pin == null || mousePoint == null) return;
        
        Point start = pin.getLocationOnCanvas();
        
        g2d.setColor(Palette.ACCENT);
        g2d.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, new float[]{6.0f, 6.0f}, 0.0f));
        
        int dx = Math.abs(mousePoint.x - start.x);
        int controlDist = Math.max(25, dx / 2);
        CubicCurve2D curve = new CubicCurve2D.Double(
            start.x, start.y,
            start.x + controlDist, start.y,
            mousePoint.x - controlDist, mousePoint.y,
            mousePoint.x, mousePoint.y
        );
        g2d.draw(curve);
        
        // Punto guía en el cursor
        g2d.fillOval(mousePoint.x - 4, mousePoint.y - 4, 8, 8);
    }
}