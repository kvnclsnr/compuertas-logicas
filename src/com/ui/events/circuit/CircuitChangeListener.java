package com.ui.events.circuit;

// QUIEN LO IMPLEMENTA SE ENTERA CUANDO CAMBIA EL CIRCUITO DEL LIENZO
public interface CircuitChangeListener {
    
    // SE LLAMA CADA VEZ QUE SE AGREGA O QUITA UN ELEMENTO O UN CABLE
    void circuitChanged();
}