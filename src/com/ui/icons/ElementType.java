package com.ui.icons;

// TIPOS DE ELEMENTO QUE SE ARRASTRAN DESDE LA PALETA, EN EL ORDEN EN QUE SE MUESTRAN
public enum ElementType {
    AND(2),
    OR(2),
    NOT(1),
    NAND(2),
    NOR(2),
    XOR(2),
    XNOR(2),
    INPUT(0),
    OUTPUT(1);
    
    private final int inputCount;
    
    // CREA EL TIPO CON SU NUMERO DE ENTRADAS
    ElementType(int inputCount) {
        this.inputCount = inputCount;
    }
    
    // DEVUELVE CUANTAS ENTRADAS TIENE ESTE TIPO
    public int getInputCount() {
        return inputCount;
    }
}