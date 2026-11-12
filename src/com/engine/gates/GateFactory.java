package com.engine.gates;

import com.engine.LogicSource;

public final class GateFactory {
    public enum Type {
        AND, OR, NOT, NAND, NOR, XOR, XNOR
    }
    
    public static Gate createGate(Type type, String name, LogicSource ...inputs) {
        if (type == null) {
            System.out.println("[!] Error: Gate type cannot be null.");
            return null;
        }
        
        if (inputs == null) {
            System.out.println("[!] Error: Gate inputs cannot be null.");
            return null;
        }
        
        if (type == Type.NOT) {
            if (inputs.length > 1) {
                System.out.println("[!] Error: NOT accepts only one input.");
                return null;
            }
            
            return new NotGate(name, inputs.length == 0 ? null : inputs[0]);
        }
        
        if (inputs.length > 2) {
            System.out.println("[!] Error: " + type + " accepts up to two inputs.");
            return null;
        }
        
        LogicSource inputA = inputs.length > 0 ? inputs[0] : null;
        LogicSource inputB = inputs.length > 1 ? inputs[1] : null;
        
        switch (type) {
            case AND:
                return new AndGate(name, inputA, inputB);
            case NAND:
                return new NandGate(name, inputA, inputB);
            case NOR:
                return new NorGate(name, inputA, inputB);
            case OR:
                return new OrGate(name, inputA, inputB);
            case XNOR:
                return new XnorGate(name, inputA, inputB);
            case XOR:
                return new XorGate(name, inputA, inputB);
            default:
                System.out.println("[!] Error: Unknown gate type: " + type);
                return null;
        }
    }
}
