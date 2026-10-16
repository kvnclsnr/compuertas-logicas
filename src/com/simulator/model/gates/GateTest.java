package com.simulator.model.gates;

import com.simulator.model.LogicInput;

public class GateTest {
    public static void main(String[] args) {
        LogicInput inputA = new LogicInput("A", true);
        LogicInput inputB = new LogicInput("B", false);
        
        Gate and = new AndGate(inputA, inputB, "AND-Gate");
        Gate or = new OrGate(inputA, inputB, "OR-Gate");
        Gate not = new NotGate(inputA, "NOT-Gate");
        Gate nand = new NandGate(inputA, inputB, "NAND-Gate");
        Gate nor = new NorGate(inputA, inputB, "NOR-Gate");
        Gate xor = new XorGate(inputA, inputB, "XOR-Gate");
        Gate xnor = new XnorGate(inputA, inputB, "XNOR-Gate");
        
        and.showTruthTable();
        or.showTruthTable();
        not.showTruthTable();
        nand.showTruthTable();
        nor.showTruthTable();
        xor.showTruthTable();
        xnor.showTruthTable();
    }
}