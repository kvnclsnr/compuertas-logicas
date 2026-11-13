package com.ui.gates;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class XnorGate extends LogicGateIcon {
    private static int counter = 1;
    
    public XnorGate(int size) {
        super(size);
        this.engineGate = new com.engine.gates.XnorGate("XNOR_" + (counter++));
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
        paths.curveTo(5, 5, 11, 5, 17, 12);
        paths.curveTo(17, 12, 11, 19, 5, 19);
        paths.curveTo(5, 19, 8, 12, 5, 5);
        
        // EXTRA CURVE
        paths.moveTo(3, 5);
        paths.curveTo(3, 5, 6, 12, 3, 19);
        
        g2d.draw(paths);
        
        drawInversionBubble(g2d, 18);
    }
}
