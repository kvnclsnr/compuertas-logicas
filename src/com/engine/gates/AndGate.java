package com.engine.gates;

import com.engine.LogicSource;

public class AndGate extends BinaryGate {
    private LogicSource inputA;
    private LogicSource inputB;

    public AndGate(String name){
        super(name);
    }
    
    public AndGate(String name, LogicSource inputA, LogicSource inputB) {
        super(name);
        this.inputA = inputA;
        this.inputB = inputB;
    }
    
    @Override
    public boolean evaluate() {
        boolean valA = inputA != null && inputA.getValue();
        boolean valB = inputB != null && inputB.getValue();
        return  valA && valB;
    }
    
    @Override
    public LogicSource[] getInputs() {
        return new LogicSource[] { inputA, inputB };
    }
    
    @Override
    public String getOperator() {
        return "AND";
    }
}