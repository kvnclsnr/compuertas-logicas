package com.ui.icons;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class Xnor extends LogicGate {
    private static int counter = 1;
    
    public Xnor(int size) {
        super(size);
        this.engineGate = new com.engine.gates.XnorGate("XNOR_" + (counter++));
    }
    
    public Xnor() {
        this(240);
    }
    
    @Override
    protected void drawGate(Graphics2D g2d) {
        Path2D.Double paths = new Path2D.Double();
        
        // OUTPUT
        paths.moveTo(24, 14);
        paths.lineTo(21.5, 14);
        
        // A
        paths.moveTo(4, 11);
        paths.lineTo(6.5, 11);
        
        // B
        paths.moveTo(4, 17);
        paths.lineTo(6.5, 17);
        
        // GATE
        paths.moveTo(7.5, 7.5);
        paths.curveTo(7.5, 7.5, 13, 8, 18.5, 14);
        paths.curveTo(18.5, 14, 13, 20, 7.5, 20.5);
        paths.curveTo(7.5, 20.5, 10.5, 14, 7.5, 7.5);
        
        // EXTRA CURVE
        paths.moveTo(5.5, 7.5);
        paths.curveTo(5.5, 7.5, 8.5, 14, 5.5, 20.5);
        
        g2d.draw(paths);
        
        drawInversionBubble(g2d, 20);
    }
}
