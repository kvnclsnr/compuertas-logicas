package com.ui.components.truthTable;

import com.ui.styles.Palette;
import com.ui.styles.Styles;
import javax.swing.JLabel;

public class CircuitTitle extends JLabel {
    
    // CREA EL TÍTULO "CIRCUITO N" CON FONDO DE ACENTO SUAVE Y TEXTO DE ACENTO
    public CircuitTitle(int circuitNumber) {
        super("Circuito " + circuitNumber);
        
        Styles.setFont(this, 12f, true);
        Styles.setForeground(this, Palette.ACCENT);
        Styles.setBackground(this, Palette.ACCENT_TINT);
        Styles.setOpaque(this, true);
        Styles.setPadding(this, 6, 8);
    }
}