package com.engine.gates;

import com.engine.LogicInput;
import com.engine.circuit.Circuit;

public class GateTest {
    public static void main(String[] args) {
        LogicInput inputA = new LogicInput("A");
        LogicInput inputB = new LogicInput("B");
        LogicInput inputC = new LogicInput("C");
        LogicInput inputD = new LogicInput("D");
        
        Circuit circuit = new Circuit("Demostracion de compuertas");
        
        Gate andGate = new AndGate("AND", inputA, inputB);
        Gate nandGate = new NandGate("NAND", inputC, inputD);
        Gate outputGate = new OrGate("Output", andGate, nandGate);
        
        circuit.addGate(andGate);
        circuit.addGate(outputGate);
        
        circuit.printTruthTable();
        circuit.printTruthTable(nandGate);
    }
}
