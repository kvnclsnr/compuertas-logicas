package com.engine.gates;

import com.engine.LogicSource;

// COMPUERTA NAND, EL CONTRARIO DE AND
public class NandGate extends BinaryGate {
    private static final String OPERATOR = "NAND";
    
    // CREA LA COMPUERTA SIN ENTRADAS CONECTADAS
    public NandGate(String name) {
        super(name);
    }
    
    // CREA LA COMPUERTA CON SUS DOS ENTRADAS
    public NandGate(String name, LogicSource inputA, LogicSource inputB) {
        super(name, inputA, inputB);
    }
    
    // APLICA NAND A LOS DOS VALORES
    @Override
    protected boolean calculate(boolean valueA, boolean valueB) {
        return !(valueA && valueB);
    }
    
    // DEVUELVE EL NOMBRE DEL OPERADOR
    @Override
    public String getOperator() {
        return OPERATOR;
    }
}