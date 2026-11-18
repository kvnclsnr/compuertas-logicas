package com.ui.history;

import com.ui.circuit.wire.Wire;
import com.ui.layouts.canvas.Canvas;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WireChangeCommand implements CanvasCommand {
    private final Canvas canvas;
    private final List<Wire> addedWires;
    private final List<Wire> removedWires;
    
    public WireChangeCommand(Canvas canvas, List<Wire> addedWires, List<Wire> removedWires) {
        this.canvas = canvas;
        this.addedWires = addedWires != null ? new ArrayList<>(addedWires) : Collections.emptyList();
        this.removedWires = removedWires != null ? new ArrayList<>(removedWires) : Collections.emptyList();
    }
    
    public List<Wire> getAddedWires() {
        return Collections.unmodifiableList(addedWires);
    }
    
    public List<Wire> getRemovedWires() {
        return Collections.unmodifiableList(removedWires);
    }
    
    @Override
    public void undo() {
        for (Wire wire : addedWires) {
            canvas.disconnectWireDirect(wire);
        }
        for (Wire wire : removedWires) {
            canvas.addWireDirect(wire);
        }
        canvas.evaluateCircuit();
        canvas.reorganizeAndNotify();
        canvas.repaint();
    }
    
    @Override
    public void redo() {
        for (Wire wire : removedWires) {
            canvas.disconnectWireDirect(wire);
        }
        for (Wire wire : addedWires) {
            canvas.addWireDirect(wire);
        }
        canvas.evaluateCircuit();
        canvas.reorganizeAndNotify();
        canvas.repaint();
    }
}
