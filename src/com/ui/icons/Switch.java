package com.ui.icons;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import com.engine.LogicInput;
import com.engine.LogicSource;
import com.engine.circuit.CircuitManager;

// INTERRUPTOR QUE ENTREGA UN 1 O UN 0 A LAS COMPUERTAS, SE CAMBIA CON UN CLIC
public class Switch extends CircuitElement {
    private static final String NAME_PREFIX = "IN-";
    private static final int[] NO_INPUT_PINS = { };
    private static final double BODY_CENTER_X = 11.67;
    private static final double BODY_RADIUS = 7;
    private static final double ACTIVE_FILL_RATIO = 0.75;
    
    private static int counter = 0;
    
    private final LogicInput logicInput;
    
    // CREA EL INTERRUPTOR APAGADO CON SU ENTRADA LOGICA
    public Switch(int size) {
        super(size, ElementType.INPUT);
        logicInput = new LogicInput(NAME_PREFIX + counter++);
    }
    
    // DEVUELVE EL NOMBRE DE LA ENTRADA LOGICA
    @Override
    public String getElementName() {
        return logicInput.getName();
    }
    
    // DEVUELVE SI EL INTERRUPTOR ESTA ENCENDIDO
    @Override
    public boolean getOutputValue() {
        return logicInput.getValue();
    }
    
    // LA SEÑAL DE SALIDA ES LA ENTRADA LOGICA
    @Override
    public LogicSource getOutputSource() {
        return logicInput;
    }
    
    // UN INTERRUPTOR SIN CABLES NO FORMA PARTE DE NINGUN CIRCUITO
    @Override
    public void addToCircuits(CircuitManager circuits) { }
    
    // AL QUITAR UN INTERRUPTOR LOS CIRCUITOS QUE UNIA PUEDEN SEPARARSE
    @Override
    public void removeFromCircuits(CircuitManager circuits) {
        circuits.reorganize();
    }
    
    // EL INTERRUPTOR NO TIENE ENTRADAS
    @Override
    protected int[] getInputPinYPositions() {
        return NO_INPUT_PINS;
    }
    
    // UN CLIC ENCIENDE O APAGA EL INTERRUPTOR
    @Override
    public void click() {
        logicInput.setValue(!logicInput.getValue());
        repaint();
    }
    
    // DIBUJA LA PATA, EL CIRCULO Y EL RELLENO CUANDO ESTA ENCENDIDO
    @Override
    protected void drawBody(Graphics2D g2d) {
        g2d.draw(new Line2D.Double(BODY_CENTER_X + BODY_RADIUS, CENTER_Y, OUTPUT_PIN_X, CENTER_Y));
        g2d.draw(createCircle(BODY_RADIUS));
        
        if (isActive()) {
            g2d.fill(createCircle(BODY_RADIUS * ACTIVE_FILL_RATIO));
        }
    }
    
    // CREA UN CIRCULO CENTRADO EN EL CUERPO DEL INTERRUPTOR
    private Ellipse2D createCircle(double radius) {
        double diameter = radius * 2;
        
        return new Ellipse2D.Double(BODY_CENTER_X - radius, CENTER_Y - radius, diameter, diameter);
    }
}