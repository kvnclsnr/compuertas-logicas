package com.engine.gates;

import com.engine.LogicSource;

// COMPUERTA XNOR, SALE 1 SI LAS ENTRADAS SON IGUALES
public class XnorGate extends BinaryGate {
    private static final String OPERATOR = "XNOR";
    
    // CREA LA COMPUERTA SIN ENTRADAS CONECTADAS
    public XnorGate(String name) {
        super(name);
    }
    
    // CREA LA COMPUERTA CON SUS DOS ENTRADAS
    public XnorGate(String name, LogicSource inputA, LogicSource inputB) {
        super(name, inputA, inputB);
    }
    
    // APLICA XNOR A LOS DOS VALORES
    @Override
    protected boolean calculate(boolean valueA, boolean valueB) {
        return valueA == valueB;
    }
    
    // DEVUELVE EL NOMBRE DEL OPERADOR
    @Override
    public String getOperator() {
        return OPERATOR;
    }
}