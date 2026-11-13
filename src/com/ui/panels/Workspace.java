package com.ui.panels;

import java.awt.BorderLayout;
import javax.swing.JPanel;

public class Workspace extends JPanel {
    public Workspace() {
        setLayout(new BorderLayout(8, 0));
        
        Canvas canvas = new Canvas();
        GatePalette gatePalette = new GatePalette();
        
        add(gatePalette, BorderLayout.WEST);
        add(canvas, BorderLayout.CENTER);
    }
}
