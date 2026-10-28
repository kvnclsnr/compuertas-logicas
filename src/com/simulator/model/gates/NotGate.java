package com.simulator.model.gates;

import com.simulator.model.LogicSource;

public class NotGate extends Gate {
    private LogicSource input;
    
    public NotGate(String name, LogicSource input) {
        super(name);
        this.input = input;
    }
    
    @Override
    public boolean evaluate() {
        return !input.getValue();
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