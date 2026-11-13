package com.ui.icons;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class And extends LogicGate {
    private static int counter = 1;
    
    public And(int size) {
        super(size);
        this.engineGate = new com.engine.gates.AndGate("AND_" + (counter++));
    }
    
    public And() {
        this(240);
    }
    
    @Override
    protected void drawGate(Graphics2D g2d) {
        Path2D.Double paths = new Path2D.Double();
        
        // OUTPUT
        paths.moveTo(24, 14);
        paths.lineTo(19, 14);
        
        // A
        paths.moveTo(4, 11);
        paths.lineTo(9, 11);
        
        // B
        paths.moveTo(4, 17);
        paths.lineTo(9, 17);
        
        // GATE
        paths.moveTo(9, 7);
        paths.curveTo(9, 7, 19, 7, 19, 14);
        paths.curveTo(19, 14, 19, 21, 9, 21);
        paths.lineTo(9, 7);
        
        g2d.draw(paths);
    }
}
