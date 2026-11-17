package com.engine.gates;

import com.engine.LogicSource;

// COMPUERTA OR, SALE 1 SI ALGUNA ENTRADA ES 1
public class OrGate extends BinaryGate {
    private static final String OPERATOR = "OR";
    
    // CREA LA COMPUERTA SIN ENTRADAS CONECTADAS
    public OrGate(String name) {
        super(name);
    }
    
    // CREA LA COMPUERTA CON SUS DOS ENTRADAS
    public OrGate(String name, LogicSource inputA, LogicSource inputB) {
        super(name, inputA, inputB);
    }
    
    // APLICA OR A LOS DOS VALORES
    @Override
    protected boolean calculate(boolean valueA, boolean valueB) {
        return valueA || valueB;
    }
    
    // DEVUELVE EL NOMBRE DEL OPERADOR
    @Override
    public String getOperator() {
        return OPERATOR;
    }
}