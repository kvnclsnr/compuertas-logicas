package com.simulator.model.gates;

import com.simulator.model.LogicInput;

public class XnorGate extends Gate {
    
    public XnorGate(LogicInput inputA, LogicInput inputB, String name) {
        super(name, inputA, inputB);
    }
    
    @Override
    public boolean evaluate() {
        return getInputA().getValue() == getInputB().getValue();
    }
}