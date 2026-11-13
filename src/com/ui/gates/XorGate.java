package com.ui.gates;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class XorGate extends LogicGateIcon {
    private static int counter = 1;
    
    public XorGate(int size) {
        super(size);
        this.engineGate = new com.engine.gates.XorGate("XOR_" + (counter++));
    }
    
    public XorGate() {
        this(240);
    }
    
    @Override
    protected void drawGate(Graphics2D g2d) {
        Path2D.Double paths = new Path2D.Double();
        
        // OUTPUT
        paths.moveTo(22, 12);
        paths.lineTo(19, 12);
        
        // A
        paths.moveTo(2, 9);
        paths.lineTo(6, 9);
        
        // B
        paths.moveTo(2, 15);
        paths.lineTo(6, 15);
        
        // GATE
        paths.moveTo(7, 5);
        paths.curveTo(7, 5, 13, 5, 19, 12);
        paths.curveTo(19, 12, 13, 19, 7, 19);
        paths.curveTo(7, 19, 10, 12, 7, 5);
        
        // EXTRA CURVE
        paths.moveTo(5, 5);
        paths.curveTo(5, 5, 8, 12, 5, 19);
        
        g2d.draw(paths);
    }
}
