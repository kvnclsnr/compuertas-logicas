package com.ui.persistence;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.ui.layouts.canvas.Canvas;

// GUARDA EL PROYECTO EN EL ARCHIVO FIJO CUANDO SE CIERRA LA VENTANA
public class SaveOnExitListener extends WindowAdapter {
    private final Canvas canvas;
    
    // CREA EL LISTENER PARA GUARDAR UN LIENZO AL CERRAR
    public SaveOnExitListener(Canvas canvas) {
        this.canvas = canvas;
    }
    
    // GUARDA EL PROYECTO ANTES DE QUE LA VENTANA SE CIERRE
    @Override
    public void windowClosing(WindowEvent event) {
        ProjectPersistence.save(canvas);
    }
}