package com.engine.gates;

import com.engine.LogicSource;

// COMPUERTA AND, SALE 1 SOLO SI LAS DOS ENTRADAS SON 1
public class AndGate extends BinaryGate {
    private static final String OPERATOR = "AND";
    
    // CREA LA COMPUERTA SIN ENTRADAS CONECTADAS
    public AndGate(String name) {
        super(name);
    }
    
    // CREA LA COMPUERTA CON SUS DOS ENTRADAS
    public AndGate(String name, LogicSource inputA, LogicSource inputB) {
        super(name, inputA, inputB);
    }
    
    // APLICA AND A LOS DOS VALORES
    @Override
    protected boolean calculate(boolean valueA, boolean valueB) {
        return valueA && valueB;
    }
    
    // DEVUELVE EL NOMBRE DEL OPERADOR
    @Override
    public String getOperator() {
        return OPERATOR;
    }
}