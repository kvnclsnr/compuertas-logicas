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
        return getInputAValue() || getInputBValue();
    }
    
    @Override
    public String getOperator() {
        return "OR";
    }
}