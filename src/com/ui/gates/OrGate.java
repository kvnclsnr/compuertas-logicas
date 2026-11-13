package com.ui.gates;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class OrGate extends LogicGateIcon {
    private static int counter = 1;
    
    public OrGate(int size) {
        super(size);
        this.engineGate = new com.engine.gates.OrGate("OR_" + (counter++));
    }
    
    public OrGate() {
        this(240);
    }
    
    @Override
    protected void drawGate(Graphics2D g2d) {
        Path2D.Double paths = new Path2D.Double();
        
        // OUTPUT
        paths.moveTo(22, 12);
        paths.lineTo(18, 12);
        
        // A
        paths.moveTo(2, 9);
        paths.lineTo(7, 9);
        
        // B
        paths.moveTo(2, 15);
        paths.lineTo(7, 15);
        
        // GATE
        paths.moveTo(6, 5);
        paths.curveTo(6, 5, 12, 5, 18, 12);
        paths.curveTo(18, 12, 12, 19, 6, 19);
        paths.curveTo(6, 19, 9, 12, 6, 5);
        
        g2d.draw(paths);
    }
}
