package com.engine.gates;

import com.engine.LogicSource;

public class NotGate extends Gate {
    private LogicSource input;
    
    public NotGate(String name){
        this(name,null);
    }
    
    public NotGate(String name, LogicSource input) {
        super(name);
        this.input = input;
    }
    
    public LogicSource getInput() {
        return input;
    }
    
    public void setInput(LogicSource input) {
        this.input = input;
    }
    
    @Override
    public boolean evaluate() {
        boolean val = input != null && input.getValue();
        return !val;
    }
    
    @Override
    public LogicSource[] getInputs() {
        if (input != null) {
            // Hay una entrada conectada: creamos un arreglo con ese elemento
            LogicSource[] entradas = new LogicSource[] { input };
            return entradas;
        } else {
            // No hay nada conectado: creamos un arreglo vacío de 0 elementos
            LogicSource[] sinEntradas = new LogicSource[0];
            return sinEntradas;
        }
    }
    
    @Override
    public String getOperator() {
        return "NOT";
    }
}