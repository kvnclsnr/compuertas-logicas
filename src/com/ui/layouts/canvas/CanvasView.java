package com.ui.layouts.canvas;

import java.awt.Dimension;
import javax.swing.JScrollPane;

import com.ui.events.drag.CanvasPanHandler;
import com.ui.styles.Palette;
import com.ui.styles.Styles;

// VISTA DEL LIENZO QUE SE DESPLAZA EN LAS DOS DIRECCIONES SIN MOSTRAR LAS BARRAS
public class CanvasView extends JScrollPane {
    
    // CREA LA VISTA PARA UN LIENZO Y LE AGREGA EL DESPLAZAMIENTO CON EL MOUSE
    public CanvasView(Canvas canvas) {
        super(canvas);
        
        Styles.setBorder(this, Palette.LINE);
        getViewport().setBackground(Palette.SURFACE);
        
        hideScrollBars();
        installPan(canvas);
    }
    
    // LAS BARRAS SE DEJAN CON TAMAÑO CERO PORQUE SI SE OCULTAN LA RUEDA NO FUNCIONA
    private void hideScrollBars() {
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
    }
    
    // PERMITE MOVER EL LIENZO ARRASTRANDO SOBRE UN ESPACIO VACIO
    private void installPan(Canvas canvas) {
        CanvasPanHandler panHandler = new CanvasPanHandler(canvas, getViewport());
        
        canvas.addMouseListener(panHandler);
        canvas.addMouseMotionListener(panHandler);
    }
}