package com.engine.gates;

import com.engine.LogicSource;

public class AndGate extends BinaryGate {
    public AndGate(String name){
        super(name);
    }
    
    public AndGate(String name, LogicSource inputA, LogicSource inputB) {
        super(name, inputA, inputB);
    }
    
    @Override
    public boolean evaluate() {
        return getInputAValue() && getInputBValue();
    }
    
    @Override
    public String getOperator() {
        return "AND";
    }
}