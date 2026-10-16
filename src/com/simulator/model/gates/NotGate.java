package com.simulator.model.gates;

import com.simulator.model.LogicInput;

public class NotGate extends Gate {
    
    public NotGate(LogicInput inputA, String name) {
        super(name, inputA);
    }
    
    @Override
    public boolean evaluate() {
        return !getInputA().getValue();
    }
}