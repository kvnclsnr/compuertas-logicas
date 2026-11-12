package com.engine.gates;

import com.engine.LogicSource;

public class XnorGate extends BinaryGate {
    
    public XnorGate(String name){
        super(name);
    }
    
    public XnorGate(String name, LogicSource inputA, LogicSource inputB) {
        super(name, inputA, inputB);
    }
    
    @Override
    public boolean evaluate() {
        return getInputAValue() == getInputBValue();
    }
    
    @Override
    public String getOperator() {
        return "XNOR";
    }
}