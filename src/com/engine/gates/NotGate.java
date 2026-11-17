package com.engine.gates;

import com.engine.LogicSource;

// COMPUERTA NOT, INVIERTE SU UNICA ENTRADA
public class NotGate extends Gate {
    private static final int INPUT_INDEX = 0;
    private static final String OPERATOR = "NOT";
    
    private LogicSource input;
    
    // CREA LA COMPUERTA SIN ENTRADA CONECTADA
    public NotGate(String name) {
        this(name, null);
    }
    
    // CREA LA COMPUERTA CON SU ENTRADA
    public NotGate(String name, LogicSource input) {
        super(name);
        this.input = input;
    }
    
    // DEVUELVE LO CONTRARIO DEL VALOR DE LA ENTRADA
    @Override
    public boolean getValue() {
        return !readValue(input);
    }
    
    // CAMBIA LA UNICA ENTRADA
    @Override
    public void setInput(int index, LogicSource input) {
        if (index != INPUT_INDEX) {
            throw new IndexOutOfBoundsException(index);
        }
        
        this.input = input;
    }
    
    // DEVUELVE LA UNICA ENTRADA
    @Override
    public LogicSource[] getInputs() {
        return new LogicSource[] { input };
    }
    
    // DEVUELVE EL NOMBRE DEL OPERADOR
    @Override
    public String getOperator() {
        return OPERATOR;
    }
}