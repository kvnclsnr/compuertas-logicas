package com.engine.circuit;

import java.util.ArrayList;
import java.util.List;

import com.engine.gates.Gate;

// GUARDA LAS COMPUERTAS DE UN CIRCUITO Y ENTREGA SU TABLA DE VERDAD
public class Circuit {
    private final String name;
    private final List<Gate> gates = new ArrayList<>();
    
    // CREA UN CIRCUITO VACIO CON SU NOMBRE
    public Circuit(String name) {
        this.name = name;
    }
    
    // DEVUELVE EL NOMBRE DEL CIRCUITO
    public String getName() {
        return name;
    }
    
    // AGREGA UNA COMPUERTA AL CIRCUITO
    public void addGate(Gate gate) {
        gates.add(gate);
    }
    
    // QUITA UNA COMPUERTA Y DICE SI ESTABA EN EL CIRCUITO
    public boolean removeGate(Gate gate) {
        return gates.remove(gate);
    }
    
    // DEVUELVE UNA COPIA CON LAS COMPUERTAS DEL CIRCUITO
    public List<Gate> getGates() {
        return new ArrayList<>(gates);
    }
    
    // DEVUELVE LA TABLA DE VERDAD DEL CIRCUITO
    public String[][] getTruthTable() {
        return TruthTableBuilder.build(gates);
    }
}