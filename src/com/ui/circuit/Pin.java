package com.ui.circuit;

import com.engine.LogicSource;
import com.ui.gates.LogicGateIcon;
import com.ui.gates.SwitchIcon;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class Pin {
    
    public enum Type {
        INPUT,
        OUTPUT
    }

    private final JComponent owner;
    private final Type type;
    private final int relativeX;
    private final int relativeY;
    private final int index;

    public Pin(JComponent owner, Type type, int relativeX, int relativeY, int index) {
        this.owner = owner;
        this.type = type;
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.index = index;
    }

    public Type getType() {
        return type;
    }

    public JComponent getOwner() {
        return owner;
    }

    public int getIndex() {
        return index;
    }

    public int getRelativeX() {
        return relativeX;
    }

    public int getRelativeY() {
        return relativeY;
    }

    public Point getLocationOnCanvas() {
        if (owner.getParent() == null) {
            return new Point(owner.getX() + relativeX, owner.getY() + relativeY);
        }
        return SwingUtilities.convertPoint(owner, relativeX, relativeY, owner.getParent());
    }

    public boolean containsInComponent(Point p) {
        int dx = p.x - relativeX;
        int dy = p.y - relativeY;
        return (dx * dx + dy * dy) <= (12 * 12);
    }

    public boolean containsOnCanvas(Point canvasPoint) {
        Point pinCanvasPos = getLocationOnCanvas();
        int dx = canvasPoint.x - pinCanvasPos.x;
        int dy = canvasPoint.y - pinCanvasPos.y;
        return (dx * dx + dy * dy) <= (14 * 14);
    }

    public boolean getValue() {
        if (type == Type.OUTPUT) {
            if (owner instanceof SwitchIcon) {
                return ((SwitchIcon) owner).isOn();
            } else if (owner instanceof LogicGateIcon) {
                return ((LogicGateIcon) owner).getOutputValue();
            }
        }
        return false;
    }

    public LogicSource getLogicSource() {
        if (owner instanceof SwitchIcon) {
            return ((SwitchIcon) owner).getLogicInput();
        } else if (owner instanceof LogicGateIcon) {
            return ((LogicGateIcon) owner).getEngineGate();
        }
        return null;
    }
}
