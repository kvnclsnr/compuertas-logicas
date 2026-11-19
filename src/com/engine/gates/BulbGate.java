package com.engine.gates;

import com.engine.LogicSource;

// FOCO QUE MUESTRA EL VALOR DE SU ENTRADA, NO INVIERTE NI COMBINA NADA
public class BulbGate extends Gate {
    private static final int INPUT_INDEX = 0;
    private static final String OPERATOR = "OUTPUT";
    
    private LogicSource input;
    
    // CREA EL FOCO SIN ENTRADA CONECTADA
    public BulbGate(String name) {
        this(name, null);
    }
    
    // CREA EL FOCO CON SU ENTRADA
    public BulbGate(String name, LogicSource input) {
        super(name);
        this.input = input;
    }
    
    // DEVUELVE EL VALOR DE LA ENTRADA CONECTADA
    @Override
    public boolean getValue() {
        return readValue(input);
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