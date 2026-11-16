package com.ui.events.resize;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JSplitPane;
import com.ui.layouts.canvas.Canvas;
import com.ui.layouts.sidePanel.SidePanel;

// COLOCA EL DIVISOR UNA SOLA VEZ, CUANDO EL PANEL YA TIENE ANCHO
// LE DA AL PANEL LATERAL SU ANCHO PREFERIDO SI EL LIENZO PUEDE CONSERVAR SU ANCHO MÍNIMO
public class DividerStartPositionHandler extends ComponentAdapter {
    private final JSplitPane split;
    private final Canvas canvas;
    private final SidePanel sidePanel;
    
    // GUARDA EL PANEL PARTIDO Y LOS DOS PANELES QUE SE REPARTEN EL ANCHO
    public DividerStartPositionHandler(JSplitPane split, Canvas canvas, SidePanel sidePanel) {
        this.split = split;
        this.canvas = canvas;
        this.sidePanel = sidePanel;
    }
    
    // FIJA LA POSICIÓN DEL DIVISOR LA PRIMERA VEZ QUE EL PANEL TIENE ANCHO
    @Override
    public void componentResized(ComponentEvent event) {
        if (split.getWidth() == 0) return;
        
        split.removeComponentListener(this);
        
        int maxPanelWidth = split.getWidth()
            - split.getDividerSize()
            - canvas.getMinimumSize().width;
        
        int panelWidth = Math.min(sidePanel.getPreferredSize().width, maxPanelWidth);
        int canvasWidth = split.getWidth() - split.getDividerSize() - panelWidth;
        
        split.setDividerLocation(canvasWidth);
    }
}