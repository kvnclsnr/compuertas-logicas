package com.engine.gates;

import com.engine.LogicSource;

// COMPUERTA NOR, EL CONTRARIO DE OR
public class NorGate extends BinaryGate {
    private static final String OPERATOR = "NOR";
    
    // CREA LA COMPUERTA SIN ENTRADAS CONECTADAS
    public NorGate(String name) {
        super(name);
    }
    
    // CREA LA COMPUERTA CON SUS DOS ENTRADAS
    public NorGate(String name, LogicSource inputA, LogicSource inputB) {
        super(name, inputA, inputB);
    }
    
    // APLICA NOR A LOS DOS VALORES
    @Override
    protected boolean calculate(boolean valueA, boolean valueB) {
        return !(valueA || valueB);
    }
    
    // DEVUELVE EL NOMBRE DEL OPERADOR
    @Override
    public String getOperator() {
        return OPERATOR;
    }
}