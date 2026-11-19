package com.ui.components.truthTable;

import java.awt.BorderLayout;
import javax.swing.JPanel;

import com.ui.styles.Palette;

// SECCION DE UN CIRCUITO, SU TITULO FIJO ARRIBA Y SU TABLA CON SCROLL DEBAJO
public class CircuitSection extends JPanel {
    private static final int TITLE_GAP = 4;
    
    // CREA LA SECCION CON EL TITULO NUMERADO Y LA TABLA
    public CircuitSection(String[][] truthTable, int circuitNumber) {
        super(new BorderLayout(0, TITLE_GAP));
        
        setBackground(Palette.SURFACE);
        
        add(new CircuitTitle(circuitNumber), BorderLayout.NORTH);
        add(new TableScrollPane(new CircuitTable(truthTable)), BorderLayout.CENTER);
    }
}