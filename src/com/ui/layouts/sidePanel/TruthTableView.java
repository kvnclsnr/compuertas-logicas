package com.ui.layouts.sidePanel;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import com.ui.components.truthTable.CircuitList;
import com.ui.events.circuit.CircuitChangeListener;
import com.ui.layouts.canvas.Canvas;
import com.ui.styles.Palette;

// SCROLL VERTICAL SIN BARRA VISIBLE QUE MUESTRA LAS TABLAS Y SE ACTUALIZA CUANDO CAMBIA EL LIENZO
public class TruthTableView extends JScrollPane implements CircuitChangeListener {
    private final Canvas canvas;
    private final CircuitList circuitList = new CircuitList();
    
    // CREA LA VISTA Y SE SUSCRIBE A LOS CAMBIOS DEL LIENZO
    public TruthTableView(Canvas canvas) {
        this.canvas = canvas;
        
        setViewportView(circuitList);
        
        setBackground(Palette.SURFACE);
        getViewport().setBackground(Palette.SURFACE);
        setBorder(BorderFactory.createEmptyBorder());
        
        hideScrollBars();
        
        canvas.setCircuitChangeListener(this);
        circuitChanged();
    }
    
    // VUELVE A CREAR LAS TABLAS CON EL ESTADO ACTUAL DEL LIENZO
    @Override
    public void circuitChanged() {
        circuitList.showCircuits(canvas.getCircuitTruthTables());
    }
    
    // SOLO SE DESPLAZA VERTICALMENTE, LA BARRA SE DEJA CON ANCHO CERO PORQUE SI SE OCULTA LA RUEDA NO FUNCIONA
    private void hideScrollBars() {
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
    }
}