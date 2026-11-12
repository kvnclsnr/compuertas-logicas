package com.engine.gates;

import com.engine.LogicSource;

public class NorGate extends BinaryGate {
    
    public NorGate(String name){
        super(name);
    }
    
    public NorGate(String name, LogicSource inputA, LogicSource inputB) {
        super(name, inputA, inputB);
    }
    
    @Override
    public boolean evaluate() {
        return !(getInputAValue() || getInputBValue());
    }
    
    @Override
    public String getOperator() {
        return "NOR";
    }
}