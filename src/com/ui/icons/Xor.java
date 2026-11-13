package com.ui.icons;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class Xor extends LogicGate {
    private static int counter = 1;
    
    public Xor(int size) {
        super(size);
        this.engineGate = new com.engine.gates.XorGate("XOR_" + (counter++));
    }
    
    public Xor() {
        this(240);
    }
    
    @Override
    protected void drawGate(Graphics2D g2d) {
        Path2D.Double paths = new Path2D.Double();
        
        // OUTPUT
        paths.moveTo(24, 14);
        paths.lineTo(21, 14);
        
        // A
        paths.moveTo(4, 11);
        paths.lineTo(8, 11);
        
        // B
        paths.moveTo(4, 17);
        paths.lineTo(8, 17);
        
        // GATE
        paths.moveTo(9, 7);
        paths.curveTo(9, 7, 15, 7, 21, 14);
        paths.curveTo(21, 14, 15, 21, 9, 21);
        paths.curveTo(9, 21, 12, 14, 9, 7);
        
        // EXTRA CURVE
        paths.moveTo(7, 7);
        paths.curveTo(7, 7, 10, 14, 7, 21);
        
        g2d.draw(paths);
    }
}
