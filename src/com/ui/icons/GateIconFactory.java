package com.ui.icons;

import javax.swing.JComponent;

// Crea el icono de una compuerta a partir de su nombre; devuelve null si no existe
public class GateIconFactory {
    public static JComponent create(String name, int size) {
        switch (name) {
            case "INPUT":
                return new Switch(size);
            case "AND":
                return new And(size);
            case "NAND":
                return new Nand(size);
            case "OR":
                return new Or(size);
            case "NOR":
                return new Nor(size);
            case "NOT":
                return new Not(size);
            case "XOR":
                return new Xor(size);
            case "XNOR":
                return new Xnor(size);
            default:
                return null;
        }
    }
}