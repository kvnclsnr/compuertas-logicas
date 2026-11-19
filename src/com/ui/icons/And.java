package com.ui.icons;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import com.engine.gates.AndGate;

// DIBUJO DE LA COMPUERTA AND
public class And extends LogicGate {
    private static int counter = 0;
    
    // CREA LA COMPUERTA CON SU NOMBRE NUMERADO
    public And(int size) {
        super(size, ElementType.AND);
        this.engineGate = new AndGate("AND-" + (counter++));
    }
    
    // CREA LA COMPUERTA CON EL TAMAÑO POR DEFECTO
    public And() {
        this(DEFAULT_SIZE);
    }
    
    // DIBUJA LAS PATAS Y EL CUERPO
    @Override
    protected void drawGate(Graphics2D g2d) {
        Path2D.Double paths = new Path2D.Double();
        
        // OUTPUT
        paths.moveTo(24, 14);
        paths.lineTo(19, 14);
        
        // A
        paths.moveTo(4, 11);
        paths.lineTo(9, 11);
        
        // B
        paths.moveTo(4, 17);
        paths.lineTo(9, 17);
        
        // GATE
        paths.moveTo(9, 7);
        paths.curveTo(9, 7, 19, 7, 19, 14);
        paths.curveTo(19, 14, 19, 21, 9, 21);
        paths.lineTo(9, 7);
        
        g2d.draw(paths);
    }
}