package com.ui.gates;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class OrGate extends LogicGateIcon {
    
    public OrGate(int size) {
        super(size);
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
        
        paths.curveTo(6, 5, 15, 6, 18, 12);
        paths.curveTo(18, 12, 15, 18, 6, 19);
        
        paths.curveTo(6, 19, 10, 12, 6, 5);
        
        g2d.draw(paths);
    }
}
