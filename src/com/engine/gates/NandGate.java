package com.engine.gates;

import com.engine.LogicSource;

public class NandGate extends BinaryGate {
    
    public  NandGate(String name){
        super(name);
    }
    
    public NandGate(String name, LogicSource inputA, LogicSource inputB) {
        super(name, inputA, inputB);
    }
    
    @Override
    public boolean evaluate() {
        return !(getInputAValue() && getInputBValue());
    }
    
    @Override
    public String getOperator() {
        return "NAND";
    }
}