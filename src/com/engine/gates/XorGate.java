package com.engine.gates;

import com.engine.LogicSource;

public class XorGate extends BinaryGate {
    
    public XorGate(String name){
        super(name);
    }
    
    public XorGate(String name, LogicSource inputA, LogicSource inputB) {
        super(name, inputA, inputB);
    }
    
    @Override
    public boolean evaluate() {
        return getInputAValue() != getInputBValue();
    }
    
    @Override
    public String getOperator() {
        return "XOR";
    }
}