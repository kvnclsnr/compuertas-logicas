package com.ui.gates;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class XnorGate extends LogicGateIcon {
    
    public XnorGate(int size) {
        super(size);
    }
    
    public XnorGate() {
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
        paths.lineTo(4, 9);
        
        // B
        paths.moveTo(2, 15);
        paths.lineTo(4, 15);
        
        // GATE
        paths.moveTo(5, 5);
        
        paths.curveTo(5, 5, 14, 6, 16, 12);
        paths.curveTo(16, 12, 14, 18, 5, 19);
        
        paths.curveTo(5, 19, 9, 12, 5, 5);
        
        paths.moveTo(3, 19);
        paths.curveTo(3, 19, 7, 12, 3, 5);
        
        g2d.draw(paths);
        
        drawInversionBubble(g2d, 18);
    }
}
