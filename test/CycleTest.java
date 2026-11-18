package test;

import com.ui.circuit.pin.Pin;
import com.ui.icons.LogicGate;
import com.ui.layouts.canvas.Canvas;
import java.awt.Point;
import javax.swing.JComponent;

public class CycleTest {
    public static void main(String[] args) {
        System.out.println("Running CycleTest...");
        Canvas canvas = new Canvas();
        
        // Add gates: NAND, NOT, AND
        canvas.addGate("NAND", new Point(100, 100));
        canvas.addGate("NOT", new Point(250, 100));
        canvas.addGate("AND", new Point(400, 100));
        
        LogicGate nandGate = (LogicGate) canvas.getComponents()[0];
        LogicGate notGate = (LogicGate) canvas.getComponents()[1];
        LogicGate andGate = (LogicGate) canvas.getComponents()[2];
        
        Pin nandOut = null;
        Pin nandIn0 = null;
        for (Pin p : nandGate.getPins()) {
            if (p.getType() == Pin.Type.OUTPUT) nandOut = p;
            else if (nandIn0 == null) nandIn0 = p;
        }
        
        Pin notOut = null;
        Pin notIn = null;
        for (Pin p : notGate.getPins()) {
            if (p.getType() == Pin.Type.OUTPUT) notOut = p;
            else notIn = p;
        }
        
        Pin andOut = null;
        Pin andIn0 = null;
        for (Pin p : andGate.getPins()) {
            if (p.getType() == Pin.Type.OUTPUT) andOut = p;
            else if (andIn0 == null) andIn0 = p;
        }
        
        assert nandOut != null && nandIn0 != null : "NAND pins";
        assert notOut != null && notIn != null : "NOT pins";
        assert andOut != null && andIn0 != null : "AND pins";
        
        // 1. Test Self-loop
        assert canvas.wouldCreateCycle(nandOut, nandIn0) : "Connecting output to input of same gate is a cycle!";
        canvas.connectWire(nandOut, nandIn0);
        assert canvas.getWires().size() == 0 : "Self-loop connection must be rejected";
        
        // 2. Connect NAND -> NOT (valid DAG connection)
        assert !canvas.wouldCreateCycle(nandOut, notIn) : "NAND -> NOT is not a cycle";
        canvas.connectWire(nandOut, notIn);
        assert canvas.getWires().size() == 1 : "NAND -> NOT wire connected";
        
        // 3. Test 2-gate cycle: NOT -> NAND (feedback loop reported by user)
        assert canvas.wouldCreateCycle(notOut, nandIn0) : "Connecting NOT -> NAND creates NOT <-> NAND cycle!";
        canvas.connectWire(notOut, nandIn0);
        assert canvas.getWires().size() == 1 : "NOT -> NAND must be rejected, wire count remains 1";
        
        // 4. Connect NOT -> AND (valid DAG connection: NAND -> NOT -> AND)
        assert !canvas.wouldCreateCycle(notOut, andIn0) : "NOT -> AND is not a cycle";
        canvas.connectWire(notOut, andIn0);
        assert canvas.getWires().size() == 2 : "NOT -> AND wire connected";
        
        // 5. Test 3-gate cycle: AND -> NAND (AND -> NAND creates NAND -> NOT -> AND -> NAND)
        assert canvas.wouldCreateCycle(andOut, nandIn0) : "AND -> NAND creates 3-gate cycle!";
        canvas.connectWire(andOut, nandIn0);
        assert canvas.getWires().size() == 2 : "AND -> NAND must be rejected, wire count remains 2";
        
        // 6. Verify circuit evaluates cleanly without freeze or StackOverflowError
        canvas.evaluateCircuit();
        
        System.out.println("ALL CYCLE TESTS PASSED SUCCESSFULLY!");
    }
}
