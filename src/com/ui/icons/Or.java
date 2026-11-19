package com.ui.icons;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import com.engine.gates.OrGate;

// DIBUJO DE LA COMPUERTA OR
public class Or extends LogicGate {
    private static int counter = 0;
    
    // CREA LA COMPUERTA CON SU NOMBRE NUMERADO
    public Or(int size) {
        super(size, ElementType.OR);
        this.engineGate = new OrGate("OR-" + (counter++));
    }
    
    // CREA LA COMPUERTA CON EL TAMAÑO POR DEFECTO
    public Or() {
        this(DEFAULT_SIZE);
    }
    
    // DIBUJA LAS PATAS Y EL CUERPO
    @Override
    protected void drawGate(Graphics2D g2d) {
        Path2D.Double paths = new Path2D.Double();
        
        // OUTPUT
        paths.moveTo(24, 14);
        paths.lineTo(20, 14);
        
        // A
        paths.moveTo(4, 11);
        paths.lineTo(9, 11);
        
        // B
        paths.moveTo(4, 17);
        paths.lineTo(9, 17);
        
        // GATE
        paths.moveTo(8, 7);
        paths.curveTo(8, 7, 14, 7, 20, 14);
        paths.curveTo(20, 14, 14, 21, 8, 21);
        paths.curveTo(8, 21, 11, 14, 8, 7);
        
        g2d.draw(paths);
    }
}