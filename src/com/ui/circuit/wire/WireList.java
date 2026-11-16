package com.ui.circuit.wire;

import com.ui.circuit.pin.Pin;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

// GUARDA LOS CABLES DEL LIENZO Y SABE BUSCARLOS
public class WireList {
    private final List<Wire> wires = new ArrayList<>();
    
    // AGREGA UN CABLE A LA LISTA
    public void add(Wire wire) {
        wires.add(wire);
    }
    
    // QUITA UN CABLE DE LA LISTA
    public void remove(Wire wire) {
        wires.remove(wire);
    }
    
    // BUSCA EL CABLE QUE LLEGA A ESA ENTRADA, O DEVUELVE NULL SI NO TIENE
    public Wire findConnectedToInput(Pin inputPin) {
        for (Wire wire : wires) {
            if (wire.getTarget().getOwner() == inputPin.getOwner()
                && wire.getTarget().getIndex() == inputPin.getIndex()) {
                return wire;
            }
        }
        return null;
    }
    
    // DEVUELVE UNA COPIA CON LOS CABLES QUE SALEN DEL DUEÑO DE ESE PIN
    public List<Wire> findFromSourceOf(Pin pin) {
        List<Wire> found = new ArrayList<>();
        
        for (Wire wire : wires) {
            if (wire.getSource().getOwner() == pin.getOwner()) {
                found.add(wire);
            }
        }
        
        return found;
    }
    
    // DEVUELVE UNA COPIA CON LOS CABLES QUE ENTRAN O SALEN DE ESA COMPUERTA
    public List<Wire> findConnectedTo(JComponent gate) {
        List<Wire> found = new ArrayList<>();
        
        for (Wire wire : wires) {
            if (wire.isConnectedTo(gate)) {
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