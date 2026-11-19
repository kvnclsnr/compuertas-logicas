package com.ui.layouts.sidePanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ui.layouts.canvas.Canvas;
import com.ui.styles.Palette;
import com.ui.styles.Styles;

// PANEL LATERAL CON SU TITULO ARRIBA Y LAS TABLAS DE VERDAD DEBAJO
public class SidePanel extends JPanel {
    private static final String TITLE = "Tabla de verdad";
    private static final float TITLE_FONT_SIZE = 16f;
    private static final int PANEL_WIDTH = 360;
    private static final int PANEL_PADDING = 12;
    private static final int TITLE_GAP = 8;
    
    // CREA EL PANEL PARA LAS TABLAS DE UN LIENZO
    public SidePanel(Canvas canvas) {
        super(new BorderLayout(0, TITLE_GAP));
        
        setPreferredSize(new Dimension(PANEL_WIDTH, 0));
        setBackground(Palette.SURFACE);
        Styles.setBorder(this, Palette.LINE);
        Styles.setPadding(this, PANEL_PADDING);
        
        add(createTitle(), BorderLayout.NORTH);
        add(new TruthTableView(canvas), BorderLayout.CENTER);
    }
    
    // CREA EL TITULO DEL PANEL
    private JLabel createTitle() {
        JLabel title = new JLabel(TITLE);
        
        Styles.setFont(title, TITLE_FONT_SIZE, true);
        title.setForeground(Palette.INK);
        
        return title;
    }
}