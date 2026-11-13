package com.ui.icons;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class Not extends LogicGate {
    private static int counter = 1;
    
    public Not(int size) {
        super(size);
        this.engineGate = new com.engine.gates.NotGate("NOT_" + (counter++));
    }
    
    public Not() {
        this(240);
    }
    
    @Override
    protected void drawGate(Graphics2D g2d) {
        Path2D.Double paths = new Path2D.Double();
        
        // OUTPUT
        paths.moveTo(24, 14);
        paths.lineTo(20.5, 14);
        
        // SINGLE INPUT at center
        paths.moveTo(4, 14);
        paths.lineTo(7.5, 14);
        
        // GATE TRIANGLE
        paths.moveTo(7.5, 7);
        paths.lineTo(17.5, 14);
        paths.lineTo(7.5, 21);
        paths.lineTo(7.5, 7);
        
        g2d.draw(paths);
        
        drawInversionBubble(g2d, 19);
    }
}
