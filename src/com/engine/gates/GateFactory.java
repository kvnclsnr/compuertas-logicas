package com.engine.gates;

import com.engine.LogicSource;

// CREA COMPUERTAS A PARTIR DE SU TIPO
public final class GateFactory {
    private static final int MAX_BINARY_INPUTS = 2;
    private static final int MAX_SINGLE_INPUTS = 1;
    
    public enum Type {
        AND, OR, NOT, NAND, NOR, XOR, XNOR, BULB, FOCO
    }
    
    private GateFactory() {
    }
    
    // CREA LA COMPUERTA DEL TIPO PEDIDO CON LAS ENTRADAS QUE SE LE DEN
    public static Gate createGate(Type type, String name, LogicSource... inputs) {
        if (type == null) {
            throw new IllegalArgumentException("Gate type cannot be null");
        }
        
        if (type == Type.NOT) {
            return createNotGate(name, inputs);
        }
        
        if (type == Type.BULB || type == Type.FOCO) {
            return createBulbGate(name, inputs);
        }
        
        return createBinaryGate(type, name, inputs);
    }
    
    // CREA UN FOCO CON CERO O UNA ENTRADA
    private static Gate createBulbGate(String name, LogicSource[] inputs) {
        if (inputs.length > MAX_SINGLE_INPUTS) {
            throw new IllegalArgumentException("BULB accepts only one input");
        }
        
        return new BulbGate(name, getInputOrNull(inputs, 0));
    }
    
    // CREA UNA COMPUERTA NOT CON CERO O UNA ENTRADA
    private static Gate createNotGate(String name, LogicSource[] inputs) {
        if (inputs.length > MAX_SINGLE_INPUTS) {
            throw new IllegalArgumentException("NOT accepts only one input");
        }
        
        return new NotGate(name, getInputOrNull(inputs, 0));
    }
    
    // CREA UNA COMPUERTA DE DOS ENTRADAS SEGUN SU TIPO
    private static Gate createBinaryGate(Type type, String name, LogicSource[] inputs) {
        if (inputs.length > MAX_BINARY_INPUTS) {
            throw new IllegalArgumentException(type + " accepts up to two inputs");
        }
        
        LogicSource inputA = getInputOrNull(inputs, 0);
        LogicSource inputB = getInputOrNull(inputs, 1);
        
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
            default:
                return new XorGate(name, inputA, inputB);
        }
    }
    
    // DEVUELVE LA ENTRADA EN ESA POSICION, O null SI NO LA DIERON
    private static LogicSource getInputOrNull(LogicSource[] inputs, int index) {
        return index < inputs.length ? inputs[index] : null;
    }
}