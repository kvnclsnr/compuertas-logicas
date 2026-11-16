package com.ui.layouts.sidePanel;

import com.ui.layouts.canvas.Canvas;
import com.ui.styles.Palette;
import com.ui.styles.Styles;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SidePanel extends JPanel {
    private static final int PANEL_WIDTH = 360;
    private static final int PANEL_PADDING = 12;
    private static final int TITLE_GAP = 8;
    
    // CREA EL PANEL LATERAL CON SU TÍTULO ARRIBA Y LAS TABLAS DEBAJO
    public SidePanel(Canvas canvas) {
        super(new BorderLayout(0, TITLE_GAP));
        setPreferredSize(new Dimension(PANEL_WIDTH, 0));
        
        Styles.setBackground(this, Palette.SURFACE);
        Styles.setBorder(this, Palette.LINE);
        Styles.setPadding(this, PANEL_PADDING);
        
        add(createTitle(), BorderLayout.NORTH);
        add(new TruthTableView(canvas), BorderLayout.CENTER);
    }
    
    // CREA EL TÍTULO DEL PANEL
    private JLabel createTitle() {
        JLabel title = new JLabel("Tabla de verdad");
        
        Styles.setFont(title, 16, true);
        Styles.setForeground(title, Palette.INK);
        
        return title;
    }
}