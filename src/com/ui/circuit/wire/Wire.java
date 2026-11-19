package com.ui.circuit.wire;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;

import com.ui.circuit.pin.Pin;
import com.ui.icons.CircuitElement;
import com.ui.styles.Palette;

// CABLE QUE LLEVA LA SEÑAL DE UN PIN DE SALIDA A UN PIN DE ENTRADA
public class Wire {
    private static final BasicStroke STROKE = new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private static final int TERMINAL_RADIUS = 3;
    
    private final Pin source;
    private final Pin target;
    
    // CREA UN CABLE QUE VA DE UNA SALIDA A UNA ENTRADA
    public Wire(Pin source, Pin target) {
        this.source = source;
        this.target = target;
    }
    
    // DEVUELVE EL PIN DE SALIDA DONDE EMPIEZA EL CABLE
    public Pin getSource() {
        return source;
    }
    
    // DEVUELVE EL PIN DE ENTRADA DONDE TERMINA EL CABLE
    public Pin getTarget() {
        return target;
    }
    
    // CONECTA LA SEÑAL EN EL ENGINE, LA ENTRADA RECIBE LA SALIDA DE ORIGEN
    public void attach() {
        target.getOwner().setInput(target.getIndex(), source.getOwner().getOutputSource());
    }
    
    // DESCONECTA LA SEÑAL EN EL ENGINE, LA ENTRADA QUEDA LIBRE
    public void detach() {
        target.getOwner().setInput(target.getIndex(), null);
    }
    
    // DICE SI EL CABLE LLEVA UN 1
    public boolean isActive() {
        return source.getOwner().getOutputValue();
    }
    
    // DICE SI EL CABLE ENTRA O SALE DE ESE ELEMENTO
    public boolean isConnectedTo(CircuitElement element) {
        return source.getOwner() == element || target.getOwner() == element;
    }
    
    // DIBUJA LA CURVA Y UN PUNTO EN CADA EXTREMO
    public void draw(Graphics2D g2d) {
        Point start = source.getLocationOnCanvas();
        Point end = target.getLocationOnCanvas();
        
        g2d.setColor(isActive() ? Palette.ACCENT : Palette.INK);
        g2d.setStroke(STROKE);
        
        g2d.draw(WireCurve.create(start, end));
        drawTerminal(g2d, start);
        drawTerminal(g2d, end);
    }
    
    // DIBUJA UN PUNTO LLENO EN UN EXTREMO DEL CABLE
    private void drawTerminal(Graphics2D g2d, Point center) {
        int diameter = TERMINAL_RADIUS * 2;
        
        g2d.fillOval(center.x - TERMINAL_RADIUS, center.y - TERMINAL_RADIUS, diameter, diameter);
    }
}