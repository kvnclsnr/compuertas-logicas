package com.ui.circuit.wire;

import java.awt.Point;
import java.awt.geom.CubicCurve2D;

// LA CURVA QUE USAN LOS CABLES: SALE HORIZONTAL DEL PRIMER PUNTO Y LLEGA HORIZONTAL AL SEGUNDO
public class WireCurve {
    private static final int MIN_CONTROL_DISTANCE = 24;
    
    // CREA LA CURVA QUE VA DEL PRIMER PUNTO AL SEGUNDO
    public static CubicCurve2D create(Point start, Point end) {
        int dx = Math.abs(end.x - start.x);
        int controlDistance = Math.max(MIN_CONTROL_DISTANCE, dx / 2);
        
        return new CubicCurve2D.Double(
            start.x, start.y,
            start.x + controlDistance, start.y,
            end.x - controlDistance, end.y,
            end.x, end.y
        );
    }
}