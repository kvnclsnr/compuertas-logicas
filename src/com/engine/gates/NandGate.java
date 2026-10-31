package com.engine.gates;

import com.engine.LogicSource;

public class NandGate extends Gate {
    private LogicSource inputA;
    private LogicSource inputB;
    
    public NandGate(String name, LogicSource inputA, LogicSource inputB) {
        super(name);
        this.inputA = inputA;
        this.inputB = inputB;
    }
    
    @Override
    public boolean evaluate() {
        return !(inputA.getValue() && inputB.getValue());
    }
    
    @Override
    public LogicSource[] getInputs() {
        return new LogicSource[] { inputA, inputB };
    }
    
    @Override
    public String getOperator() {
        return "NAND";
    }
}