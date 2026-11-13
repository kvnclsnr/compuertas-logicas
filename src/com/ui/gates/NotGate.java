package com.ui.gates;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class NotGate extends LogicGateIcon {
    private static int counter = 1;
    
    public NotGate(int size) {
        super(size);
        this.engineGate = new com.engine.gates.NotGate("NOT_" + (counter++));
    }
    
    public NotGate() {
        this(240);
    }
    
    @Override
    protected void drawGate(Graphics2D g2d) {
        Path2D.Double paths = new Path2D.Double();
        
        // OUTPUT
        paths.moveTo(22, 12);
        paths.lineTo(19, 12);
        
        // SINGLE INPUT at center
        paths.moveTo(2, 12);
        paths.lineTo(5, 12);
        
        // GATE TRIANGLE
        paths.moveTo(5, 5);
        paths.lineTo(15, 12);
        paths.lineTo(5, 19);
        paths.lineTo(5, 5);
        
        g2d.draw(paths);
        
        drawInversionBubble(g2d, 17);
    }
}
