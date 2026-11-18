package test;

import com.ui.circuit.pin.Pin;
import com.ui.circuit.wire.Wire;
import com.ui.history.*;
import com.ui.icons.LogicGate;
import com.ui.layouts.canvas.Canvas;
import java.awt.Component;
import java.awt.Point;
import javax.swing.JComponent;

public class UndoRedoTest {
    public static void main(String[] args) {
        System.out.println("Running UndoRedoTest...");
        Canvas canvas = new Canvas();
        
        // 1. Initial state
        assert !canvas.hasUnsavedChanges() : "Should have no unsaved changes initially";
        assert !canvas.canUndo() : "Should not be able to undo initially";
        assert !canvas.canRedo() : "Should not be able to redo initially";
        
        // 2. Add component (AND gate)
        boolean added = canvas.addGate("AND", new Point(100, 100));
        assert added : "AND gate should be added";
        assert canvas.getComponents().length == 1 : "Should have 1 component";
        assert canvas.canUndo() : "Should be able to undo after adding gate";
        assert !canvas.canRedo() : "Should not be able to redo";
        assert canvas.hasUnsavedChanges() : "Should have unsaved changes after adding gate";
        
        Component gate1 = canvas.getComponents()[0];
        assert gate1 instanceof LogicGate : "Component should be LogicGate";
        
        // Undo add gate
        canvas.undo();
        assert canvas.getComponents().length == 0 : "Canvas should be empty after undo";
        assert !canvas.canUndo() : "Should not be able to undo anymore";
        assert canvas.canRedo() : "Should be able to redo";
        assert !canvas.hasUnsavedChanges() : "Should have no unsaved changes after undoing back to initial";
        
        // Redo add gate
        canvas.redo();
        assert canvas.getComponents().length == 1 : "Component should be back after redo";
        assert canvas.canUndo() : "Should be able to undo after redo";
        assert canvas.hasUnsavedChanges() : "Should have unsaved changes after redo";
        
        // 3. Move component
        JComponent comp = (JComponent) canvas.getComponents()[0];
        Point p1 = comp.getLocation();
        Point p2 = new Point(250, 300);
        comp.setLocation(p2);
        canvas.recordMove(comp, p1, p2);
        
        assert comp.getLocation().equals(p2) : "Component should be at p2";
        canvas.undo();
        assert comp.getLocation().equals(p1) : "Component should be back at p1 after undo move";
        canvas.redo();
        assert comp.getLocation().equals(p2) : "Component should be at p2 after redo move";
        
        // 4. Add another component (Switch / Input)
        boolean addedSwitch = canvas.addGate("INPUT", new Point(10, 100));
        assert addedSwitch : "Switch should be added";
        assert canvas.getComponents().length == 2 : "Canvas should have 2 components";
        
        JComponent switchComp = null;
        for (Component c : canvas.getComponents()) {
            if (c != comp) switchComp = (JComponent) c;
        }
        assert switchComp != null : "Switch component should exist";
        
        // 5. Connect wire from Switch output to AND gate input
        Pin switchOut = canvas.findPinAt(switchComp, new Point(switchComp.getWidth() - 5, switchComp.getHeight() / 2));
        assert switchOut != null && switchOut.getType() == Pin.Type.OUTPUT : "Switch output pin found";
        
        Pin gateIn = canvas.findPinAt(comp, new Point(5, comp.getHeight() / 3));
        assert gateIn != null && gateIn.getType() == Pin.Type.INPUT : "Gate input pin found";
        
        canvas.connectWire(switchOut, gateIn);
        assert canvas.getWires().size() == 1 : "Wire should be connected";
        
        // Undo wire connection
        canvas.undo();
        assert canvas.getWires().size() == 0 : "Wire should be removed after undo";
        
        // Redo wire connection
        canvas.redo();
        assert canvas.getWires().size() == 1 : "Wire should be restored after redo";
        
        // 6. Delete component with wire attached
        canvas.removeGate(comp);
        assert canvas.getComponents().length == 1 : "Only switch should remain after removing AND gate";
        assert canvas.getWires().size() == 0 : "Wire should be removed when connected component is deleted";
        
        // Undo delete component
        canvas.undo();
        assert canvas.getComponents().length == 2 : "Both components restored after undo removeGate";
        assert canvas.getWires().size() == 1 : "Wire restored after undo removeGate";
        
        // Redo delete component
        canvas.redo();
        assert canvas.getComponents().length == 1 : "Only switch remains after redo removeGate";
        assert canvas.getWires().size() == 0 : "Wire removed again after redo removeGate";
        
        // Undo again to bring it back
        canvas.undo();
        assert canvas.getComponents().length == 2 : "Restored again";
        assert canvas.getWires().size() == 1 : "Wire restored again";
        
        // 7. Test markSaved() and hasUnsavedChanges()
        canvas.markSaved();
        assert !canvas.hasUnsavedChanges() : "Should have no unsaved changes after markSaved()";
        
        canvas.undo(); // undoes the delete/restore? No, undoes whatever was before markSaved
        assert canvas.hasUnsavedChanges() : "Should have unsaved changes when diverging from save point";
        
        canvas.redo();
        assert !canvas.hasUnsavedChanges() : "Should have no unsaved changes when returning to save point";
        
        // 8. Test clearCanvas()
        canvas.clearCanvas();
        assert canvas.getComponents().length == 0 : "Canvas should be empty after clear";
        assert canvas.getWires().size() == 0 : "No wires after clear";
        assert !canvas.hasUnsavedChanges() : "No unsaved changes after clear";
        assert !canvas.canUndo() : "Cannot undo after clear";
        assert !canvas.canRedo() : "Cannot redo after clear";
        
        // 9. Test Wire replacement (overwriting existing wire on same input pin)
        canvas.addGate("INPUT", new Point(10, 50));
        canvas.addGate("INPUT", new Point(10, 150));
        canvas.addGate("NOT", new Point(200, 100));
        
        JComponent in1 = (JComponent) canvas.getComponents()[0];
        JComponent in2 = (JComponent) canvas.getComponents()[1];
        JComponent notG = (JComponent) canvas.getComponents()[2];
        
        Pin pinIn1Out = canvas.findPinAt(in1, new Point(in1.getWidth() - 5, in1.getHeight() / 2));
        Pin pinIn2Out = canvas.findPinAt(in2, new Point(in2.getWidth() - 5, in2.getHeight() / 2));
        Pin notInput = canvas.findPinAt(notG, new Point(5, notG.getHeight() / 2));
        
        assert pinIn1Out != null && pinIn2Out != null && notInput != null : "Pins should exist";
        
        // Connect in1 -> NOT
        canvas.connectWire(pinIn1Out, notInput);
        assert canvas.getWires().size() == 1 : "1 wire connected";
        assert canvas.getWires().get(0).getSource() == pinIn1Out;
        
        // Now connect in2 -> NOT (replaces in1 -> NOT)
        canvas.connectWire(pinIn2Out, notInput);
        assert canvas.getWires().size() == 1 : "Still 1 wire connected";
        assert canvas.getWires().get(0).getSource() == pinIn2Out : "Source should now be in2";
        
        // Undo replacement: should restore in1 -> NOT
        canvas.undo();
        assert canvas.getWires().size() == 1 : "Still 1 wire connected after undo";
        assert canvas.getWires().get(0).getSource() == pinIn1Out : "Source should be restored to in1";
        
        // Redo replacement: should bring back in2 -> NOT
        canvas.redo();
        assert canvas.getWires().size() == 1 : "Still 1 wire connected after redo";
        assert canvas.getWires().get(0).getSource() == pinIn2Out : "Source should be back to in2";
        
        // 10. Test disconnectPin
        canvas.disconnectPin(notInput);
        assert canvas.getWires().size() == 0 : "Wire disconnected via disconnectPin";
        canvas.undo();
        assert canvas.getWires().size() == 1 : "Wire restored after undo disconnectPin";
        assert canvas.getWires().get(0).getSource() == pinIn2Out;
        canvas.redo();
        assert canvas.getWires().size() == 0 : "Wire disconnected again after redo disconnectPin";
        
        // 11. Test UndoRedoListener
        final int[] listenerCalls = {0};
        canvas.getUndoRedoManager().addListener(() -> listenerCalls[0]++);
        canvas.undo();
        assert listenerCalls[0] == 1 : "Listener should have been called on undo";
        canvas.redo();
        assert listenerCalls[0] == 2 : "Listener should have been called on redo";
        
        System.out.println("ALL UNDO/REDO TESTS PASSED SUCCESSFULLY!");
    }
}
