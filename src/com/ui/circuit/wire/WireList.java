package com.ui.circuit.wire;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.ui.circuit.pin.Pin;
import com.ui.icons.CircuitElement;

// GUARDA LOS CABLES DEL LIENZO Y SABE BUSCARLOS
public class WireList {
    private final List<Wire> wires = new ArrayList<>();
    
    // AGREGA UN CABLE
    public void add(Wire wire) {
        wires.add(wire);
    }
    
    // QUITA UN CABLE
    public void remove(Wire wire) {
        wires.remove(wire);
    }
    
    // DEVUELVE UNA COPIA CON TODOS LOS CABLES
    public List<Wire> getAll() {
        return new ArrayList<>(wires);
    }
    
    // BUSCA EL CABLE QUE TERMINA EN ESE PIN, O DEVUELVE NULL SI NO HAY
    public Wire findEndingAt(Pin pin) {
        for (Wire wire : wires) {
            if (wire.getTarget().isSamePinAs(pin)) {
                return wire;
            }
        }
        
        return null;
    }
    
    // DEVUELVE UNA COPIA CON LOS CABLES QUE SALEN DE ESE ELEMENTO
    public List<Wire> findStartingAt(CircuitElement element) {
        List<Wire> found = new ArrayList<>();
        
        for (Wire wire : wires) {
            if (wire.getSource().getOwner() == element) {
                found.add(wire);
            }
        }
        
        return found;
    }
    
    // DEVUELVE UNA COPIA CON LOS CABLES QUE ENTRAN O SALEN DE ESE ELEMENTO
    public List<Wire> findConnectedTo(CircuitElement element) {
        List<Wire> found = new ArrayList<>();
        
        for (Wire wire : wires) {
            if (wire.isConnectedTo(element)) {
                found.add(wire);
            }
        }
        
        return found;
    }
    
    // DIBUJA TODOS LOS CABLES
    public void draw(Graphics2D g2d) {
        for (Wire wire : wires) {
            wire.draw(g2d);
        }
    }
}