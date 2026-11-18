package test;

import com.ui.circuit.pin.Pin;
import com.ui.circuit.wire.Wire;
import com.ui.icons.LogicGate;
import com.ui.layouts.canvas.Canvas;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class ZoomPanTest {
    public static void main(String[] args) {
        System.out.println("Running ZoomPanTest...");
        Canvas canvas = new Canvas();
        canvas.setSize(800, 600);
        
        // 1. Initial zoom
        assert Math.abs(canvas.getZoomFactor() - 1.0) < 0.001 : "Initial zoom should be 1.0";
        
        // Add components
        boolean added1 = canvas.addGate("AND", new Point(200, 200));
        boolean added2 = canvas.addGate("INPUT", new Point(100, 200));
        assert added1 && added2 : "Gates added";
        
        JComponent andGate = null;
        JComponent switchGate = null;
        for (java.awt.Component c : canvas.getComponents()) {
            if (c instanceof LogicGate) andGate = (JComponent) c;
            else switchGate = (JComponent) c;
        }
        assert andGate != null && switchGate != null : "Components found";
        
        // Connect wire
        Pin outPin = canvas.findPinAt(switchGate, new Point(switchGate.getWidth() - 5, switchGate.getHeight() / 2));
        Pin inPin = canvas.findPinAt(andGate, new Point(5, andGate.getHeight() / 3));
        assert outPin != null && inPin != null : "Pins found";
        canvas.connectWire(outPin, inPin);
        assert canvas.getWires().size() == 1 : "Wire connected";
        
        int initialAndWidth = andGate.getWidth();
        int initialAndX = andGate.getX();
        int initialAndY = andGate.getY();
        
        // 2. Test Zoom In
        canvas.zoomIn();
        assert canvas.getZoomFactor() > 1.0 : "Zoom factor should be > 1.0";
        assert andGate.getWidth() > initialAndWidth : "Component size should increase on zoomIn";
        
        // 3. Test Zoom Out
        canvas.zoomOut();
        assert Math.abs(canvas.getZoomFactor() - 1.0) < 0.01 : "Zoom factor should return near 1.0";
        assert andGate.getWidth() == initialAndWidth : "Component size should return to initial";
        
        // 4. Test Zoom Limits (Clamping)
        for (int i = 0; i < 20; i++) canvas.zoomIn();
        assert canvas.getZoomFactor() <= 2.5 : "Zoom factor should not exceed 2.5";
        
        for (int i = 0; i < 30; i++) canvas.zoomOut();
        assert canvas.getZoomFactor() >= 0.4 : "Zoom factor should not go below 0.4";
        
        // 5. Test Reset Zoom
        canvas.resetZoom();
        assert Math.abs(canvas.getZoomFactor() - 1.0) < 0.001 : "Reset zoom should restore 1.0";
        assert andGate.getWidth() == initialAndWidth : "Component size should be reset to 96";
        
        // 6. Test Pan
        Point beforePan = andGate.getLocation();
        Point switchBeforePan = switchGate.getLocation();
        canvas.pan(50, -30);
        assert andGate.getX() == beforePan.x + 50 : "X should increase by 50";
        assert andGate.getY() == beforePan.y - 30 : "Y should decrease by 30";
        assert switchGate.getX() == switchBeforePan.x + 50 : "Switch X should increase by 50";
        assert switchGate.getY() == switchBeforePan.y - 30 : "Switch Y should decrease by 30";
        
        // Wire should still connect the same pins
        assert canvas.getWires().size() == 1 : "Wire is still connected";
        
        // 7. Test Spacebar state and right click behavior
        assert !canvas.isSpacePressed() : "Spacebar not pressed initially";
        canvas.setSpacePressed(true);
        assert canvas.isSpacePressed() : "Spacebar state is true";
        assert canvas.getCursor().getType() == Cursor.HAND_CURSOR : "Cursor should be HAND_CURSOR";
        assert andGate.getCursor().getType() == Cursor.HAND_CURSOR : "Gate cursor should be HAND_CURSOR";
        
        // When space is held, right click on gate pin must NOT disconnect
        Point pinPoint = new Point(5, andGate.getHeight() / 3);
        MouseEvent rightClick = new MouseEvent(
            andGate, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(),
            MouseEvent.BUTTON3_DOWN_MASK, pinPoint.x, pinPoint.y, 1, false, MouseEvent.BUTTON3
        );
        andGate.dispatchEvent(rightClick);
        assert canvas.getWires().size() == 1 : "Wire should NOT be disconnected when space is held!";
        
        // Release space
        canvas.setSpacePressed(false);
        assert !canvas.isSpacePressed() : "Spacebar state is false";
        assert canvas.getCursor().getType() == Cursor.DEFAULT_CURSOR : "Cursor should be default";
        
        System.out.println("ALL ZOOM AND PAN TESTS PASSED SUCCESSFULLY!");
    }
}
