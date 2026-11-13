package com.ui.icons;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class Or extends LogicGate {
    private static int counter = 1;
    
    public Or(int size) {
        super(size);
        this.engineGate = new com.engine.gates.OrGate("OR_" + (counter++));
    }
    
    public Or() {
        this(240);
    }
    
    @Override
    protected void drawGate(Graphics2D g2d) {
        Path2D.Double paths = new Path2D.Double();
        
        // OUTPUT
        paths.moveTo(24, 14);
        paths.lineTo(20, 14);
        
        // A
        paths.moveTo(4, 11);
        paths.lineTo(9, 11);
        
        // B
        paths.moveTo(4, 17);
        paths.lineTo(9, 17);
        
        // GATE
        paths.moveTo(8, 7);
        paths.curveTo(8, 7, 14, 7, 20, 14);
        paths.curveTo(20, 14, 14, 21, 8, 21);
        paths.curveTo(8, 21, 11, 14, 8, 7);
        
        g2d.draw(paths);
    }
}
