package com.ui.icons;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

import com.engine.gates.NotGate;

// DIBUJO DE LA COMPUERTA NOT
public class Not extends LogicGate {
    private static final int[] SINGLE_INPUT_PIN_Y = { CENTER_Y };
    
    private static int counter = 0;
    
    // CREA LA COMPUERTA CON SU NOMBRE NUMERADO
    public Not(int size) {
        super(size, ElementType.NOT);
        this.engineGate = new NotGate("NOT-" + (counter++));
    }
    
    // CREA LA COMPUERTA CON EL TAMAÑO POR DEFECTO
    public Not() {
        this(DEFAULT_SIZE);
    }
    
    // LA COMPUERTA NOT TIENE UNA SOLA ENTRADA, EN EL CENTRO
    @Override
    protected int[] getInputPinYPositions() {
        return SINGLE_INPUT_PIN_Y;
    }
    
    // DIBUJA LAS PATAS, EL TRIANGULO Y EL CIRCULO DE INVERSION
    @Override
    protected void drawGate(Graphics2D g2d) {
        Path2D.Double paths = new Path2D.Double();
        
        // OUTPUT
        paths.moveTo(24, 14);
        paths.lineTo(20.5, 14);
        
        // SINGLE INPUT AT CENTER
        paths.moveTo(4, 14);
        paths.lineTo(7.5, 14);
        
        // GATE TRIANGLE
        paths.moveTo(7.5, 7);
        paths.lineTo(17.5, 14);
        paths.lineTo(7.5, 21);
        paths.lineTo(7.5, 7);
        
        g2d.draw(paths);
        
        drawInversionBubble(g2d, 19);
    }
}