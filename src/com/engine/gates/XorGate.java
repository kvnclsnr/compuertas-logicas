package com.engine.gates;

import com.engine.LogicSource;

// COMPUERTA XOR, SALE 1 SI LAS ENTRADAS SON DISTINTAS
public class XorGate extends BinaryGate {
    private static final String OPERATOR = "XOR";
    
    // CREA LA COMPUERTA SIN ENTRADAS CONECTADAS
    public XorGate(String name) {
        super(name);
    }
    
    // CREA LA COMPUERTA CON SUS DOS ENTRADAS
    public XorGate(String name, LogicSource inputA, LogicSource inputB) {
        super(name, inputA, inputB);
    }
    
    // APLICA XOR A LOS DOS VALORES
    @Override
    protected boolean calculate(boolean valueA, boolean valueB) {
        return valueA != valueB;
    }
    
    // DEVUELVE EL NOMBRE DEL OPERADOR
    @Override
    public String getOperator() {
        return OPERATOR;
    }
}