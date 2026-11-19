package com.ui.circuit.wire;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;

import com.ui.circuit.pin.Pin;
import com.ui.styles.Palette;

// CABLE PUNTEADO QUE SIGUE AL MOUSE MIENTRAS SE ARRASTRA DESDE UN PIN
public class PendingWire {
    private static final float[] DASH_PATTERN = { 6f, 6f };
    private static final BasicStroke STROKE = new BasicStroke(
        2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10f, DASH_PATTERN, 0f
    );
    private static final int GUIDE_DOT_RADIUS = 4;
    
    private Pin startPin = null;
    private Point mousePoint = null;
    
    // DICE SI HAY UN CABLE EN PROCESO DE CONEXION
    public boolean isActive() {
        return startPin != null;
    }
    
    // DEVUELVE EL PIN DESDE DONDE SE EMPEZO A ARRASTRAR
    public Pin getStartPin() {
        return startPin;
    }
    
    // EMPIEZA UN CABLE DESDE UN PIN
    public void start(Pin startPin, Point mousePoint) {
        this.startPin = startPin;
        this.mousePoint = mousePoint;
    }
    
    // MUEVE EL EXTREMO LIBRE DEL CABLE HASTA EL MOUSE
    public void moveTo(Point mousePoint) {
        this.mousePoint = mousePoint;
    }
    
    // CANCELA EL CABLE EN PROCESO
    public void clear() {
        startPin = null;
        mousePoint = null;
    }
    
    // DIBUJA LA CURVA PUNTEADA Y UN PUNTO GUIA EN EL MOUSE
    public void draw(Graphics2D g2d) {
        if (!isActive()) return;
        
        g2d.setColor(Palette.ACCENT);
        g2d.setStroke(STROKE);
        g2d.draw(WireCurve.create(startPin.getLocationOnCanvas(), mousePoint));
        
        int diameter = GUIDE_DOT_RADIUS * 2;
        g2d.fillOval(mousePoint.x - GUIDE_DOT_RADIUS, mousePoint.y - GUIDE_DOT_RADIUS, diameter, diameter);
    }
}