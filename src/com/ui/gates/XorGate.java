package com.ui.gates;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class XorGate extends LogicGateIcon {
    
    public XorGate(int size) {
        super(size);
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
        
        paths.curveTo(7, 5, 16, 6, 19, 12);
        paths.curveTo(19, 12, 16, 18, 7, 19);
        
        paths.curveTo(7, 19, 11, 12, 7, 5);
        
        paths.moveTo(5, 19);
        paths.curveTo(5, 19, 9, 12, 5, 5);
        
        g2d.draw(paths);
    }
}
