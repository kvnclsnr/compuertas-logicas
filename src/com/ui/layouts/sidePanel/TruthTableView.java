package com.ui.layouts.sidePanel;

import com.ui.components.truthTable.CircuitList;
import com.ui.events.circuit.CircuitChangeListener;
import com.ui.layouts.canvas.Canvas;
import com.ui.styles.Palette;
import com.ui.styles.Styles;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

public class TruthTableView extends JScrollPane implements CircuitChangeListener {
    private final Canvas canvas;
    private final CircuitList circuitList = new CircuitList();
    
    // CREA LA VISTA Y SE SUSCRIBE A LOS CAMBIOS DEL LIENZO PARA MANTENER LAS TABLAS ACTUALIZADAS
    public TruthTableView(Canvas canvas) {
        this.canvas = canvas;
        
        setViewportView(circuitList);
        
        Styles.setBackground(this, Palette.SURFACE);
        Styles.setBackground(getViewport(), Palette.SURFACE);
        Styles.setBorder(this, BorderFactory.createEmptyBorder());
        
        hideScrollBars();
        
        canvas.setCircuitChangeListener(this);
        
        circuitChanged();
    }
    
    // SOLO SE DESPLAZA VERTICALMENTE, SIN BARRA VISIBLE
    // LA BARRA VERTICAL SE DEJA CON ANCHO 0 EN VEZ DE OCULTARLA, PORQUE SIN BARRA VISIBLE LA RUEDA NO FUNCIONA
    private void hideScrollBars() {
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
    }
    
    // VUELVE A CREAR LAS TABLAS CON EL ESTADO ACTUAL DEL CIRCUITO
    @Override
    public void circuitChanged() {
        circuitList.showCircuits(canvas.getCircuitTruthTables());
    }
}