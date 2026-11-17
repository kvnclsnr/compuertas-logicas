package com.engine.gates;

import com.engine.LogicSource;

// BASE DE LAS COMPUERTAS DE DOS ENTRADAS, CADA UNA SOLO DEFINE COMO CALCULA SU RESULTADO
public abstract class BinaryGate extends Gate {
    private static final int INPUT_A_INDEX = 0;
    private static final int INPUT_B_INDEX = 1;
    
    private LogicSource inputA;
    private LogicSource inputB;
    
    // CREA LA COMPUERTA SIN ENTRADAS CONECTADAS
    public BinaryGate(String name) {
        this(name, null, null);
    }
    
    // CREA LA COMPUERTA CON SUS DOS ENTRADAS
    public BinaryGate(String name, LogicSource inputA, LogicSource inputB) {
        super(name);
        this.inputA = inputA;
        this.inputB = inputB;
    }
    
    // CALCULA LA SALIDA A PARTIR DE LOS DOS VALORES
    protected abstract boolean calculate(boolean valueA, boolean valueB);
    
    // LEE LAS DOS ENTRADAS Y CALCULA LA SALIDA
    @Override
    public boolean getValue() {
        return calculate(readValue(inputA), readValue(inputB));
    }
    
    // CAMBIA LA ENTRADA A O LA B SEGUN EL INDICE
    @Override
    public void setInput(int index, LogicSource input) {
        if (index == INPUT_A_INDEX) {
            inputA = input;
        }
        
        else if (index == INPUT_B_INDEX) {
            inputB = input;
        }
        
        else {
            throw new IndexOutOfBoundsException(index);
        }
    }
    
    // DEVUELVE LAS DOS ENTRADAS EN ORDEN
    @Override
    public LogicSource[] getInputs() {
        return new LogicSource[] { inputA, inputB };
    }
}