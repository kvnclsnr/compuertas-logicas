package com.ui.styles;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

// AYUDAS PARA PONER BORDES, MARGENES Y FUENTES SIN PERDER LOS QUE EL COMPONENTE YA TIENE
public final class Styles {
    
    private Styles() { }
    
    // PONE UN BORDE DE LINEA EN LOS CUATRO LADOS Y CONSERVA EL MARGEN
    public static void setBorder(JComponent component, Color color) {
        setOuterBorder(component, BorderFactory.createLineBorder(color));
    }
    
    // PONE UN BORDE CON UN GROSOR DISTINTO EN CADA LADO Y CONSERVA EL MARGEN
    public static void setBorder(JComponent component, int top, int left, int bottom, int right, Color color) {
        setOuterBorder(component, BorderFactory.createMatteBorder(top, left, bottom, right, color));
    }
    
    // PONE EL MISMO MARGEN INTERNO EN LOS CUATRO LADOS Y CONSERVA EL BORDE
    public static void setPadding(JComponent component, int padding) {
        setPadding(component, padding, padding, padding, padding);
    }
    
    // PONE UN MARGEN VERTICAL Y UN MARGEN HORIZONTAL Y CONSERVA EL BORDE
    public static void setPadding(JComponent component, int vertical, int horizontal) {
        setPadding(component, vertical, horizontal, vertical, horizontal);
    }
    
    // PONE UN MARGEN INTERNO DISTINTO EN CADA LADO Y CONSERVA EL BORDE
    public static void setPadding(JComponent component, int top, int left, int bottom, int right) {
        Border padding = BorderFactory.createEmptyBorder(top, left, bottom, right);
        Border remainingBorder = removePadding(component.getBorder());
        
        component.setBorder(combine(remainingBorder, padding));
    }
    
    // CAMBIA EL TAMAÑO Y EL GROSOR DE LA FUENTE
    public static void setFont(JComponent component, float size, boolean bold) {
        int style = bold ? Font.BOLD : Font.PLAIN;
        
        component.setFont(component.getFont().deriveFont(style, size));
    }
    
    // CAMBIA EL TAMAÑO DE LA FUENTE Y LA DEJA SIN NEGRILLA
    public static void setFont(JComponent component, float size) {
        setFont(component, size, false);
    }
    
    // PONE UN BORDE POR FUERA Y REEMPLAZA EL BORDE DE LINEA QUE HUBIERA
    private static void setOuterBorder(JComponent component, Border outerBorder) {
        Border remainingBorder = removeLineBorder(component.getBorder());
        
        component.setBorder(combine(outerBorder, remainingBorder));
    }
    
    // QUITA EL BORDE DE LINEA EXTERIOR Y DEVUELVE LO QUE QUEDA
    private static Border removeLineBorder(Border border) {
        if (border instanceof CompoundBorder compoundBorder) {
            if (isLineBorder(compoundBorder.getOutsideBorder())) {
                return compoundBorder.getInsideBorder();
            }
            
            return border;
        }
        
        if (isLineBorder(border)) {
            return null;
        }
        
        return border;
    }
    
    // QUITA EL MARGEN INTERIOR Y DEVUELVE LO QUE QUEDA
    private static Border removePadding(Border border) {
        if (border instanceof CompoundBorder compoundBorder) {
            if (compoundBorder.getInsideBorder() instanceof EmptyBorder) {
                return compoundBorder.getOutsideBorder();
            }
            
            return border;
        }
        
        if (border instanceof EmptyBorder) {
            return null;
        }
        
        return border;
    }
    
    // DICE SI EL BORDE ES DE LINEA SIMPLE O DE GROSOR POR LADO
    private static boolean isLineBorder(Border border) {
        return border instanceof LineBorder || border instanceof MatteBorder;
    }
    
    // JUNTA DOS BORDES, SI UNO FALTA DEVUELVE EL OTRO
    private static Border combine(Border outer, Border inner) {
        if (outer == null) return inner;
        if (inner == null) return outer;
        
        return BorderFactory.createCompoundBorder(outer, inner);
    }
}