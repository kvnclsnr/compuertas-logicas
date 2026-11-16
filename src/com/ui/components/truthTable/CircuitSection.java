package com.ui.components.truthTable;

import com.ui.styles.Palette;
import com.ui.styles.Styles;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class CircuitSection extends JPanel {
    private static final int TITLE_GAP = 4;
    
    // CREA LA SECCIÓN DE UN CIRCUITO: SU TÍTULO FIJO ARRIBA Y SU TABLA CON SCROLL DEBAJO
    public CircuitSection(String[][] truthTable, int circuitNumber) {
        super(new BorderLayout(0, TITLE_GAP));
        
        Styles.setBackground(this, Palette.SURFACE);
        
        add(new CircuitTitle(circuitNumber), BorderLayout.NORTH);
        add(new TableScrollPane(new CircuitTable(truthTable)), BorderLayout.CENTER);
    }
}