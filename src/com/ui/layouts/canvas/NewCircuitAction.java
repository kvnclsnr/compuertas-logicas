package com.ui.layouts.canvas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// ACCION DE VACIAR EL LIENZO PARA EMPEZAR UN CIRCUITO NUEVO
public class NewCircuitAction implements ActionListener {
    private final Canvas canvas;
    
    // CREA LA ACCION PARA VACIAR UN LIENZO
    public NewCircuitAction(Canvas canvas) {
        this.canvas = canvas;
    }
    
    // VACIA EL LIENZO
    @Override
    public void actionPerformed(ActionEvent event) {
        canvas.clear();
    }
}