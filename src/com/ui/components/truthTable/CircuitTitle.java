package com.ui.components.truthTable;

import javax.swing.JLabel;

import com.ui.styles.Palette;
import com.ui.styles.Styles;

// TITULO DE UN CIRCUITO, POR EJEMPLO CIRCUITO 1
public class CircuitTitle extends JLabel {
    private static final String TITLE_PREFIX = "Circuito ";
    private static final int VERTICAL_PADDING = 6;
    private static final int HORIZONTAL_PADDING = 8;
    private static final float FONT_SIZE = 12f;
    
    // CREA EL TITULO NUMERADO CON FONDO Y TEXTO DEL COLOR DE ACENTO
    public CircuitTitle(int circuitNumber) {
        super(TITLE_PREFIX + circuitNumber);
        
        Styles.setFont(this, FONT_SIZE, true);
        setForeground(Palette.ACCENT);
        
        setOpaque(true);
        setBackground(Palette.ACCENT_TINT);
        Styles.setPadding(this, VERTICAL_PADDING, HORIZONTAL_PADDING);
    }
}