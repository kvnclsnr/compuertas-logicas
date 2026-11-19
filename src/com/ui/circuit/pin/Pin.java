package com.ui.circuit.pin;

import java.awt.Point;

import com.ui.icons.CircuitElement;

// PUNTO DE CONEXION DE UN ELEMENTO, PUEDE SER DE ENTRADA O DE SALIDA
public class Pin {
    private static final int OWNER_HIT_RADIUS = 12;
    private static final int CANVAS_HIT_RADIUS = 14;
    
    // INDICA SI EL PIN RECIBE LA SEÑAL O LA ENTREGA
    public enum Type {
        INPUT,
        OUTPUT
    }
    
    private final CircuitElement owner;
    private final Type type;
    private final int relativeX;
    private final int relativeY;
    private final int index;
    
    // CREA UN PIN DE UN ELEMENTO EN UNA POSICION RELATIVA A EL
    public Pin(CircuitElement owner, Type type, int relativeX, int relativeY, int index) {
        this.owner = owner;
        this.type = type;
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.index = index;
    }
    
    // DEVUELVE SI ES UN PIN DE ENTRADA O DE SALIDA
    public Type getType() {
        return type;
    }
    
    // DEVUELVE EL ELEMENTO AL QUE PERTENECE EL PIN
    public CircuitElement getOwner() {
        return owner;
    }
    
    // DEVUELVE EL NUMERO DEL PIN DENTRO DE SU ELEMENTO
    public int getIndex() {
        return index;
    }
    
    // DEVUELVE LA POSICION DEL PIN SOBRE EL LIENZO
    public Point getLocationOnCanvas() {
        return new Point(owner.getX() + relativeX, owner.getY() + relativeY);
    }
    
    // DICE SI UN PUNTO, MEDIDO DENTRO DEL ELEMENTO, ESTA SOBRE EL PIN
    public boolean containsInOwner(Point pointInOwner) {
        return isWithinRadius(new Point(relativeX, relativeY), pointInOwner, OWNER_HIT_RADIUS);
    }
    
    // DICE SI UN PUNTO DEL LIENZO ESTA SOBRE EL PIN
    public boolean containsOnCanvas(Point canvasPoint) {
        return isWithinRadius(getLocationOnCanvas(), canvasPoint, CANVAS_HIT_RADIUS);
    }
    
    // DICE SI OTRO PIN ES EL MISMO PIN DEL MISMO ELEMENTO
    public boolean isSamePinAs(Pin other) {
        return owner == other.owner && type == other.type && index == other.index;
    }
    
    // DICE SI UN PUNTO ESTA A UNA DISTANCIA DEL CENTRO IGUAL O MENOR QUE EL RADIO
    private boolean isWithinRadius(Point center, Point point, int radius) {
        return center.distance(point) <= radius;
    }
}