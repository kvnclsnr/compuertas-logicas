package com.ui.icons;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import com.engine.LogicSource;
import com.engine.circuit.CircuitManager;
import com.engine.gates.Gate;

// BASE DE LAS COMPUERTAS DIBUJADAS, GUARDA LA COMPUERTA DEL ENGINE QUE HACE EL CALCULO
public abstract class LogicGate extends CircuitElement {
    protected static final int DEFAULT_SIZE = 240;
    
    private static final int[] TWO_INPUT_PIN_Y = { 11, 17 };
    private static final double BUBBLE_RADIUS = 1.5;
    
    protected Gate engineGate;
    
    // CREA LA COMPUERTA DIBUJADA, CADA SUBCLASE ASIGNA SU COMPUERTA DEL ENGINE
    protected LogicGate(int size, ElementType type) {
        super(size, type);
    }
    
    // DIBUJA LAS PATAS Y EL CUERPO PROPIOS DE CADA COMPUERTA
    protected abstract void drawGate(Graphics2D g2d);
    
    // DEVUELVE EL NOMBRE DE LA COMPUERTA DEL ENGINE
    @Override
    public String getElementName() {
        return engineGate.getName();
    }
    
    // DEVUELVE EL RESULTADO ACTUAL DE LA COMPUERTA
    @Override
    public boolean getOutputValue() {
        return engineGate.getValue();
    }
    
    // LA SEÑAL DE SALIDA ES LA PROPIA COMPUERTA DEL ENGINE
    @Override
    public LogicSource getOutputSource() {
        return engineGate;
    }
    
    // CONECTA UNA FUENTE A LA ENTRADA INDICADA
    @Override
    public void setInput(int index, LogicSource source) {
        engineGate.setInput(index, source);
    }
    
    // AGREGA LA COMPUERTA DEL ENGINE A LOS CIRCUITOS
    @Override
    public void addToCircuits(CircuitManager circuits) {
        circuits.addGate(engineGate);
    }
    
    // QUITA LA COMPUERTA DEL ENGINE DE LOS CIRCUITOS
    @Override
    public void removeFromCircuits(CircuitManager circuits) {
        circuits.removeGate(engineGate);
    }
    
    // POR DEFECTO LAS COMPUERTAS TIENEN DOS ENTRADAS
    @Override
    protected int[] getInputPinYPositions() {
        return TWO_INPUT_PIN_Y;
    }
    
    // EL CUERPO DE UNA COMPUERTA ES SU DIBUJO PROPIO
    @Override
    protected final void drawBody(Graphics2D g2d) {
        drawGate(g2d);
    }
    
    // DIBUJA EL CIRCULO QUE INDICA UNA INVERSION
    protected final void drawInversionBubble(Graphics2D g2d, double centerX) {
        double diameter = BUBBLE_RADIUS * 2;
        
        g2d.draw(new Ellipse2D.Double(centerX - BUBBLE_RADIUS, CENTER_Y - BUBBLE_RADIUS, diameter, diameter));
    }
}