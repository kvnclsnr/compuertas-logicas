package com.engine.gates;

import com.engine.LogicSource;

public abstract class BinaryGate extends Gate{
    // estas es una clase abstracta para las compuertas de 2 entradas logicas
    protected LogicSource inputA;
    protected LogicSource inputB;

    public BinaryGate(String name){
        this(name, null, null);
    }
    public BinaryGate(String name, LogicSource inputA, LogicSource inputB) {
        super(name);
        this.inputA = inputA;
        this.inputB = inputB;
    }

    public LogicSource getInputA() {
        return inputA;
    }
    
    public void setInputA(LogicSource inputA) {
        this.inputA = inputA;
    }
    
    public LogicSource getInputB() {
        return inputB;
    }
    
    public void setInputB(LogicSource inputB) {
        this.inputB = inputB;
    }
    
    public void setInputs(LogicSource inputA, LogicSource inputB) {
        this.inputA = inputA;
        this.inputB = inputB;
    }
    
    @Override
    public LogicSource[] getInputs() {
        return new LogicSource[] { inputA, inputB };
    }
}
