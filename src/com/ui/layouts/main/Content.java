package com.ui.layouts.main;

import java.awt.BorderLayout;
import javax.swing.JPanel;

import com.ui.layouts.canvas.Canvas;
import com.ui.layouts.workspace.Workspace;
import com.ui.styles.Palette;
import com.ui.styles.Styles;

// CONTENIDO PRINCIPAL DE LA VENTANA, CONTIENE EL AREA DE TRABAJO CON UN MARGEN
public class Content extends JPanel {
    private static final int PADDING = 16;
    
    // CREA EL CONTENIDO CON EL AREA DE TRABAJO AL CENTRO
    public Content(Canvas canvas) {
        super(new BorderLayout());
        
        Styles.setPadding(this, PADDING);
        setBackground(Palette.BACKGROUND);
        
        add(new Workspace(canvas), BorderLayout.CENTER);
    }
}