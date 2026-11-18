package com.ui.layouts.workspace;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.ui.components.gatePalette.GateItem;
import com.ui.styles.Palette;
import com.ui.styles.Styles;

public class GatePalette extends JScrollPane {
    private static final String[] GATE_NAMES = {
        "AND", "OR", "NOT", "NAND", "NOR", "XOR", "XNOR", "INPUT", "BULB"
    };
    
    private static final int PALETTE_WIDTH = 192;
    private static final int SCROLL_UNIT = 16;
    
    private final JPanel gatesPanel = new JPanel();
    
    // CREA LA LISTA VERTICAL DE ELEMENTOS QUE SE PUEDEN ARRASTRAR AL LIENZO
    public GatePalette() {
        Styles.setBorder(this, Palette.LINE);
        
        gatesPanel.setLayout(new BoxLayout(gatesPanel, BoxLayout.Y_AXIS));
        
        Styles.setBackground(gatesPanel, Palette.SURFACE);
        Styles.setPadding(gatesPanel, 8);
        
        for (int i = 0; i < GATE_NAMES.length; i++) {
            if (i > 0) {
                gatesPanel.add(Box.createVerticalStrut(8));
            }
            
            gatesPanel.add(new GateItem(GATE_NAMES[i]));
        }
        
        gatesPanel.setPreferredSize(new Dimension(PALETTE_WIDTH, gatesPanel.getPreferredSize().height));
        
        setViewportView(gatesPanel);
        
        // OCULTA LAS BARRAS AUNQUE EL PANEL PERMITE DESPLAZAMIENTO VERTICAL
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        
        getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT);
    }
}