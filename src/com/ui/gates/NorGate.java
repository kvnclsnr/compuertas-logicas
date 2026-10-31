package com.ui.gates;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class NorGate extends LogicGateIcon {
    
    public NorGate(int size) {
        super(size);
    }
    
    public NorGate() {
        this(240);
    }    
    
    @Override
    protected void drawGate(Graphics2D g2d) {
        
        Path2D.Double paths = new Path2D.Double();
        
        // OUTPUT
        paths.moveTo(22, 12);
        paths.lineTo(20, 12);
        
        // A
        paths.moveTo(2, 9);
        paths.lineTo(5, 9);
        
        // B
        paths.moveTo(2, 15);
        paths.lineTo(5, 15);
        
        // GATE
        paths.moveTo(4, 5);
        
        paths.curveTo(4, 5, 13, 6, 16, 12);
        paths.curveTo(16, 12, 13, 18, 4, 19);
        
        paths.curveTo(4, 19, 8, 12, 4, 5);
        
        g2d.draw(paths);
        
        drawInversionBubble(g2d, 18);
    }
}
