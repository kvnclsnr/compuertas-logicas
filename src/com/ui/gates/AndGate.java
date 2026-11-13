package com.ui.gates;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class AndGate extends LogicGateIcon {
    private static int counter = 1;
    
    public AndGate(int size) {
        super(size);
        this.engineGate = new com.engine.gates.AndGate("AND_" + (counter++));
    }
    
    public AndGate() {
        this(240);
    }
    
    @Override
    protected void drawGate(Graphics2D g2d) {
        Path2D.Double paths = new Path2D.Double();
        
        // OUTPUT
        paths.moveTo(22, 12);
        paths.lineTo(17, 12);
        
        // A
        paths.moveTo(2, 9);
        paths.lineTo(7, 9);
        
        // B
        paths.moveTo(2, 15);
        paths.lineTo(7, 15);
        
        // GATE
        paths.moveTo(7, 5);
        paths.curveTo(7, 5, 17, 5, 17, 12);
        paths.curveTo(17, 12, 17, 19, 7, 19);
        paths.lineTo(7, 5);
        
        g2d.draw(paths);
    }
}
