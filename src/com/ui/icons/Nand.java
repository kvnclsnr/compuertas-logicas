package com.ui.icons;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class Nand extends LogicGate {
    private static int counter = 1;
    
    public Nand(int size) {
        super(size);
        this.engineGate = new com.engine.gates.NandGate("NAND_" + (counter++));
    }
    
    public Nand() {
        this(240);
    }    
    
    @Override
    protected void drawGate(Graphics2D g2d) {
        Path2D.Double paths = new Path2D.Double();
        
        // OUTPUT
        paths.moveTo(24, 14);
        paths.lineTo(21, 14);
        
        // A
        paths.moveTo(4, 11);
        paths.lineTo(7, 11);
        
        // B
        paths.moveTo(4, 17);
        paths.lineTo(7, 17);
        
        // GATE
        paths.moveTo(7, 7);
        paths.curveTo(7, 7, 17, 7, 17, 14);
        paths.curveTo(17, 14, 17, 21, 7, 21);
        paths.lineTo(7, 7);
        
        g2d.draw(paths);
        
        drawInversionBubble(g2d, 19);
    }
}
