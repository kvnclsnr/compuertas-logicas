package com.ui.events.circuit;

// AVISA CUANDO CAMBIA EL CIRCUITO: SE AGREGA O QUITA UNA COMPUERTA O UN CABLE
public interface CircuitChangeListener {
    
    // SE LLAMA CADA VEZ QUE EL CIRCUITO CAMBIA
    void circuitChanged();
}