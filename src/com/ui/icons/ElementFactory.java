package com.ui.icons;

// CREA EL ELEMENTO DIBUJADO QUE CORRESPONDE A CADA TIPO
public final class ElementFactory {
    
    private ElementFactory() { }
    
    // CREA UN ELEMENTO NUEVO DEL TIPO PEDIDO CON EL TAMAÑO INDICADO
    public static CircuitElement create(ElementType type, int size) {
        return switch (type) {
            case AND -> new And(size);
            case OR -> new Or(size);
            case NOT -> new Not(size);
            case NAND -> new Nand(size);
            case NOR -> new Nor(size);
            case XOR -> new Xor(size);
            case XNOR -> new Xnor(size);
            case INPUT -> new Switch(size);
            case OUTPUT -> new Bulb(size);
        };
    }
}