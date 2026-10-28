package com.simulator.model.gates;

import com.simulator.model.LogicSource;

public class OrGate extends Gate {
    private LogicSource inputA;
    private LogicSource inputB;
    
    public OrGate(String name, LogicSource inputA, LogicSource inputB) {
        super(name);
        this.inputA = inputA;
        this.inputB = inputB;
    }
    
    @Override
    public boolean evaluate() {
        return inputA.getValue() || inputB.getValue();
    }
    
    @Override
    public LogicSource[] getInputs() {
        return new LogicSource[] { inputA, inputB };
    }
    
    @Override
    public String getOperator() {
        return "OR";
    }
}