package com.ui.gates;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class NotGate extends LogicGateIcon {
    
    public NotGate(int size) {
        super(size);
    }
    
    public NotGate() {
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
        paths.lineTo(5, 9);
        
        // B
        paths.moveTo(2, 15);
        paths.lineTo(5, 15);
        
        // GATE
        paths.moveTo(5, 5);
        
        paths.lineTo(15, 12);
        paths.lineTo(5, 19);
        paths.lineTo(5, 5);
        
        g2d.draw(paths);
        
        drawInversionBubble(g2d, 17);
    }
}
