package com.ui.circuit.wire;

import java.awt.Point;
import java.awt.geom.CubicCurve2D;

// CURVA QUE USAN LOS CABLES, SALE HORIZONTAL DEL PRIMER PUNTO Y LLEGA HORIZONTAL AL SEGUNDO
public final class WireCurve {
    private static final int MIN_CONTROL_DISTANCE = 24;
    
    private WireCurve() { }
    
    // CREA LA CURVA QUE VA DEL PRIMER PUNTO AL SEGUNDO
    public static CubicCurve2D create(Point start, Point end) {
        int horizontalDistance = Math.abs(end.x - start.x);
        int controlDistance = Math.max(MIN_CONTROL_DISTANCE, horizontalDistance / 2);
        
        return new CubicCurve2D.Double(
            start.x, start.y,
            start.x + controlDistance, start.y,
            end.x - controlDistance, end.y,
            end.x, end.y
        );
    }
}