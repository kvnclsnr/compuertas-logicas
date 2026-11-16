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
    
    @Override
    public void setInput(int index, LogicSource input) {
        if (index != 0) {
            throw new IndexOutOfBoundsException(index);
        }
        
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
        return new LogicSource[] { input };
    }
    
    @Override
    public String getOperator() {
        return "NOT";
    }
}