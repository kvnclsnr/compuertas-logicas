package com.ui.circuit.wire;

import com.ui.circuit.pin.Pin;
import com.ui.styles.Palette;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Objects;
import javax.swing.JComponent;

public class Wire {
    private static final float WIRE_WIDTH = 3.5f;
    private static final int TERMINAL_SIZE = 6;
    
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
    
    // DICE SI EL CABLE LLEVA UN 1, ES DECIR, SI SU SALIDA ESTÁ ENCENDIDA
    public boolean isActive() {
        return source.getValue();
    }
    
    // DICE SI EL CABLE ENTRA O SALE DE ESE COMPONENTE
    public boolean isConnectedTo(JComponent component) {
        return source.getOwner() == component || target.getOwner() == component;
    }
    
    // DIBUJA EL CABLE CON SU CURVA Y UN PUNTO EN CADA EXTREMO
    public void draw(Graphics2D g2) {
        Point start = source.getLocationOnCanvas();
        Point end = target.getLocationOnCanvas();
        
        g2.setColor(isActive() ? Palette.ACCENT : Palette.INK);
        g2.setStroke(new BasicStroke(WIRE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        g2.draw(WireCurve.create(start, end));
        
        // PUNTOS EN LOS EXTREMOS
        int radius = TERMINAL_SIZE / 2;
        g2.fillOval(start.x - radius, start.y - radius, TERMINAL_SIZE, TERMINAL_SIZE);
        g2.fillOval(end.x - radius, end.y - radius, TERMINAL_SIZE, TERMINAL_SIZE);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wire wire = (Wire) o;
        return Objects.equals(source, wire.source) && Objects.equals(target, wire.target);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}