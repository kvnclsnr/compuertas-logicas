package com.ui.circuit.pin;

import com.engine.LogicSource;
import com.ui.icons.LogicGate;
import com.ui.icons.Switch;

import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class Pin {
    private static final int COMPONENT_HIT_RADIUS = 12;
    private static final int CANVAS_HIT_RADIUS = 14;
    
    public enum Type {
        INPUT,
        OUTPUT
    }
    
    private final JComponent owner;
    private final Type type;
    
    private final int relativeX;
    private final int relativeY;
    
    private final int index;
    
    // CREA UN PIN QUE PERTENECE A UN COMPONENTE, EN UNA POSICIÓN RELATIVA A ÉL
    public Pin(JComponent owner, Type type, int relativeX, int relativeY, int index) {
        this.owner = owner;
        this.type = type;
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.index = index;
    }
    
    // DEVUELVE SI EL PIN ES DE ENTRADA O DE SALIDA
    public Type getType() {
        return type;
    }
    
    // DEVUELVE EL COMPONENTE AL QUE PERTENECE EL PIN
    public JComponent getOwner() {
        return owner;
    }
    
    // DEVUELVE EL NÚMERO DEL PIN DENTRO DE SU COMPUERTA
    public int getIndex() {
        return index;
    }
    
    // DEVUELVE LA POSICIÓN HORIZONTAL DEL PIN DENTRO DE SU COMPONENTE
    public int getRelativeX() {
        return relativeX;
    }
    
    // DEVUELVE LA POSICIÓN VERTICAL DEL PIN DENTRO DE SU COMPONENTE
    public int getRelativeY() {
        return relativeY;
    }
    
    // DEVUELVE LA POSICIÓN DEL PIN SOBRE EL LIENZO
    public Point getLocationOnCanvas() {
        if (owner.getParent() == null) {
            return new Point(owner.getX() + relativeX, owner.getY() + relativeY);
        }
        
        return SwingUtilities.convertPoint(owner, relativeX, relativeY, owner.getParent());
    }
    
    // DICE SI UN PUNTO, EN COORDENADAS DEL COMPONENTE DUEÑO, ESTÁ SOBRE EL PIN
    public boolean containsInComponent(Point point) {
        return isWithinRadius(point.x - relativeX, point.y - relativeY, COMPONENT_HIT_RADIUS);
    }
    
    // DICE SI UN PUNTO DEL LIENZO ESTÁ SOBRE EL PIN
    public boolean containsOnCanvas(Point canvasPoint) {
        Point pinPosition = getLocationOnCanvas();
        
        return isWithinRadius(
            canvasPoint.x - pinPosition.x,
            canvasPoint.y - pinPosition.y,
            CANVAS_HIT_RADIUS
        );
    }
    
    // DICE SI LA DISTANCIA AL CENTRO DEL PIN (DX, DY) NO PASA DEL RADIO
    private boolean isWithinRadius(int dx, int dy, int radius) {
        return dx * dx + dy * dy <= radius * radius;
    }
    
    // DEVUELVE EL VALOR DE LA SALIDA DEL PIN, O FALSO SI ES UNA ENTRADA
    public boolean getValue() {
        if (type != Type.OUTPUT) return false;
        
        if (owner instanceof Switch switchIcon) {
            return switchIcon.isOn();
        }
        
        if (owner instanceof LogicGate gate) {
            return gate.getOutputValue();
        }
        
        return false;
    }
    
    // DEVUELVE LA PARTE DEL MOTOR DE DONDE SALE LA SEÑAL DE ESTE PIN, O NULL SI NO HAY
    public LogicSource getLogicSource() {
        if (owner instanceof Switch switchIcon) {
            return switchIcon.getLogicInput();
        }
        
        if (owner instanceof LogicGate gate) {
            return gate.getEngineGate();
        }
        
        return null;
    }
}