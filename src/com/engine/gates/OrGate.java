package com.engine.gates;

import com.engine.LogicSource;

public class OrGate extends BinaryGate {

    public OrGate(String name){
        super(name);
    }
    
    public OrGate(String name, LogicSource inputA, LogicSource inputB) {
        super(name, inputA, inputB);
    }
    
    @Override
    public boolean evaluate() {
        boolean valA = inputA != null && inputA.getValue();
        boolean valB = inputB != null && inputB.getValue();
        return valA || valB;
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