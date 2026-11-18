package com.engine.gates;

import com.engine.LogicInput;
import com.ui.circuit.pin.Pin;
import com.ui.icons.Bulb;
import com.ui.icons.GateIconFactory;
import java.util.List;
import javax.swing.JComponent;

public class BulbTest {
    public static void main(String[] args) {
        System.out.println("Running Bulb tests...");
        
        // 1. BulbGate standalone
        BulbGate bulbGate = new BulbGate("FOCO-0");
        assertFalse(bulbGate.getValue(), "Unconnected bulb should be false");
        
        // 2. BulbGate with LogicInput
        LogicInput input = new LogicInput("A", false);
        bulbGate.setInput(0, input);
        assertFalse(bulbGate.getValue(), "Bulb with input=false should be false");
        
        input.setValue(true);
        assertTrue(bulbGate.getValue(), "Bulb with input=true should be true");
        
        input.setValue(false);
        assertFalse(bulbGate.getValue(), "Bulb with input=false should be false again");
        
        // 3. BulbGate connected to AND gate
        LogicInput in1 = new LogicInput("I1", true);
        LogicInput in2 = new LogicInput("I2", false);
        Gate andGate = GateFactory.createGate(GateFactory.Type.AND, "AND-0", in1, in2);
        bulbGate.setInput(0, andGate);
        assertFalse(bulbGate.getValue(), "Bulb connected to AND(1,0) should be false");
        
        in2.setValue(true);
        assertTrue(bulbGate.getValue(), "Bulb connected to AND(1,1) should be true");
        
        // 4. GateFactory creation
        Gate gBulb = GateFactory.createGate(GateFactory.Type.BULB, "B-TEST");
        assertTrue(gBulb instanceof BulbGate, "GateFactory BULB should return BulbGate");
        
        Gate gFoco = GateFactory.createGate(GateFactory.Type.FOCO, "F-TEST");
        assertTrue(gFoco instanceof BulbGate, "GateFactory FOCO should return BulbGate");
        
        // 5. GateIconFactory creation
        JComponent iconBulb = GateIconFactory.create("BULB", 96);
        assertTrue(iconBulb instanceof Bulb, "GateIconFactory BULB should return Bulb");
        
        JComponent iconFoco = GateIconFactory.create("FOCO", 96);
        assertTrue(iconFoco instanceof Bulb, "GateIconFactory FOCO should return Bulb");
        
        // 6. Pin count and type check
        Bulb bulb = (Bulb) iconBulb;
        List<Pin> pins = bulb.getPins();
        assertEquals(1, pins.size(), "Bulb must have exactly 1 pin");
        assertEquals(Pin.Type.INPUT, pins.get(0).getType(), "Bulb pin must be INPUT");
        assertEquals(0, pins.get(0).getIndex(), "Bulb pin index should be 0");
        
        // 7. Visual bulb state
        assertFalse(bulb.isOn(), "Initial bulb.isOn() should be false");
        bulb.setPinInput(0, in1);
        in1.setValue(true);
        assertTrue(bulb.isOn(), "bulb.isOn() should be true when input is true");
        assertTrue(bulb.isActive(), "bulb.isActive() should be true when input is true");
        
        in1.setValue(false);
        assertFalse(bulb.isOn(), "bulb.isOn() should be false when input is false");
        assertFalse(bulb.isActive(), "bulb.isActive() should be false when input is false");
        
        // 8. Canvas integration test with Switch, AND gate and Bulb
        com.ui.layouts.canvas.Canvas canvas = new com.ui.layouts.canvas.Canvas();
        canvas.addGate("INPUT", new java.awt.Point(50, 50));
        canvas.addGate("INPUT", new java.awt.Point(50, 150));
        canvas.addGate("AND", new java.awt.Point(200, 100));
        canvas.addGate("BULB", new java.awt.Point(350, 100));
        
        com.ui.icons.Switch sw1 = null;
        com.ui.icons.Switch sw2 = null;
        com.ui.icons.And andComp = null;
        Bulb canvasBulb = null;
        
        for (java.awt.Component c : canvas.getComponents()) {
            if (c instanceof com.ui.icons.Switch sw) {
                if (sw1 == null) sw1 = sw;
                else sw2 = sw;
            } else if (c instanceof com.ui.icons.And a) {
                andComp = a;
            } else if (c instanceof Bulb b) {
                canvasBulb = b;
            }
        }
        
        assertNotNull(sw1, "Switch 1 found");
        assertNotNull(sw2, "Switch 2 found");
        assertNotNull(andComp, "AND gate found");
        assertNotNull(canvasBulb, "Bulb found");
        
        // Connect sw1 -> andComp(pin 0)
        canvas.connectWire(sw1.getOutputPin(), andComp.getPins().get(0));
        // Connect sw2 -> andComp(pin 1)
        canvas.connectWire(sw2.getOutputPin(), andComp.getPins().get(1));
        // Connect andComp(out pin) -> canvasBulb(pin 0)
        canvas.connectWire(andComp.getPins().get(2), canvasBulb.getPins().get(0));
        
        // Both off -> Bulb off
        canvas.evaluateCircuit();
        assertFalse(canvasBulb.isOn(), "Bulb should be off when inputs are (0,0)");
        
        // sw1 on, sw2 off -> Bulb off
        sw1.toggle();
        canvas.evaluateCircuit();
        assertFalse(canvasBulb.isOn(), "Bulb should be off when inputs are (1,0)");
        
        // sw1 on, sw2 on -> Bulb on!
        sw2.toggle();
        canvas.evaluateCircuit();
        assertTrue(canvasBulb.isOn(), "Bulb should be on when inputs are (1,1)");
        
        // sw1 off -> Bulb off
        sw1.toggle();
        canvas.evaluateCircuit();
        assertFalse(canvasBulb.isOn(), "Bulb should be off when inputs are (0,1)");
        
        // 9. Truth table count test (User bug fix: Bulb should not create an extra truth table)
        com.ui.layouts.canvas.Canvas isolatedCanvas = new com.ui.layouts.canvas.Canvas();
        // Only an unconnected Bulb on canvas -> 0 truth tables
        isolatedCanvas.addGate("BULB", new java.awt.Point(100, 100));
        assertEquals(0, isolatedCanvas.getCircuitTruthTables().size(), "Unconnected bulb should generate 0 truth tables");
        
        // 1 AND gate + 1 unconnected Bulb -> exactly 1 truth table
        isolatedCanvas.addGate("AND", new java.awt.Point(250, 100));
        assertEquals(1, isolatedCanvas.getCircuitTruthTables().size(), "1 AND + 1 unconnected bulb should generate 1 truth table");
        
        // 10. Cycle protection test (NOT <-> NAND cycle)
        NotGate notGate = new NotGate("NOT-CYC");
        NandGate nandGate = new NandGate("NAND-CYC");
        notGate.setInput(0, nandGate);
        nandGate.setInput(0, notGate);
        
        // getValue() must not throw StackOverflowError
        boolean valNot = notGate.getValue();
        boolean valNand = nandGate.getValue();
        
        // getExpression() must not throw StackOverflowError and contain cycle marker
        String exprNot = notGate.getExpression();
        String exprNand = nandGate.getExpression();
        assertTrue(exprNot.contains("CICLO") || exprNand.contains("CICLO"), "Cycle expression should contain cycle marker");
        
        // TruthTableBuilder.build() must detect cycle and return non-combinational message
        String[][] tt = com.engine.circuit.TruthTableBuilder.build(java.util.List.of(notGate, nandGate));
        assertEquals(1, tt.length, "Cycle truth table should have 1 row");
        assertEquals("Circuito con retroalimentación (no combinacional)", tt[0][0], "Cycle truth table message");
        
        System.out.println("All Bulb tests passed successfully!");
    }
    
    private static void assertNotNull(Object obj, String message) {
        if (obj == null) {
            throw new AssertionError("FAILED: " + message + " was null");
        }
    }
    
    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("FAILED: " + message);
        }
    }
    
    private static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError("FAILED: " + message);
        }
    }
    
    private static void assertEquals(Object expected, Object actual, String message) {
        if (!java.util.Objects.equals(expected, actual)) {
            throw new AssertionError("FAILED: " + message + " (expected: " + expected + ", actual: " + actual + ")");
        }
    }
}
