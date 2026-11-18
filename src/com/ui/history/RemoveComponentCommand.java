package com.ui.history;

import com.ui.circuit.wire.Wire;
import com.ui.layouts.canvas.Canvas;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

public class RemoveComponentCommand implements CanvasCommand {
    private final Canvas canvas;
    private final JComponent component;
    private final List<Wire> connectedWires;
    
    public RemoveComponentCommand(Canvas canvas, JComponent component, List<Wire> connectedWires) {
        this.canvas = canvas;
        this.component = component;
        this.connectedWires = connectedWires != null ? new ArrayList<>(connectedWires) : new ArrayList<>();
    }
    
    public JComponent getComponent() {
        return component;
    }
    
    public List<Wire> getConnectedWires() {
        return new ArrayList<>(connectedWires);
    }
    
    @Override
    public void undo() {
        canvas.restoreComponentDirect(component);
        for (Wire wire : connectedWires) {
            canvas.addWireDirect(wire);
        }
        canvas.evaluateCircuit();
        canvas.reorganizeAndNotify();
        canvas.repaint();
    }
    
    @Override
    public void redo() {
        canvas.removeComponentDirect(component);
    }
}
