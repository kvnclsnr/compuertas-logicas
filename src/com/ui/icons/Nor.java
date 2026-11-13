package com.ui.icons;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import com.engine.gates.NorGate;

public class Nor extends LogicGate {
    private static int counter = 1;
    
    public Nor(int size) {
        super(size);
        this.engineGate = new NorGate("NOR_" + (counter++));
    }
    
    public Nor() {
        this(240);
    }    
    
    @Override
    protected void drawGate(Graphics2D g2d) {
        Path2D.Double paths = new Path2D.Double();
        
        // OUTPUT
        paths.moveTo(24, 14);
        paths.lineTo(21.5, 14);
        
        // A
        paths.moveTo(4, 11);
        paths.lineTo(7.5, 11);
        
        // B
        paths.moveTo(4, 17);
        paths.lineTo(7.5, 17);
        
        // GATE
        paths.moveTo(6.5, 7);
        paths.curveTo(6.5, 7, 12.5, 7, 18.5, 14);
        paths.curveTo(18.5, 14, 12.5, 21, 6.5, 21);
        paths.curveTo(6.5, 21, 9.5, 14, 6.5, 7);
        
        g2d.draw(paths);
        
        drawInversionBubble(g2d, 20);
    }
}
