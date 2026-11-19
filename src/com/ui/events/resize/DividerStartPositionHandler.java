package com.ui.events.resize;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JSplitPane;

// COLOCA EL DIVISOR UNA SOLA VEZ, CUANDO EL PANEL YA TIENE ANCHO, DANDOLE AL DERECHO SU ANCHO PREFERIDO
public class DividerStartPositionHandler extends ComponentAdapter {
    private final JSplitPane split;
    private final Component leftPanel;
    private final Component rightPanel;
    
    // CREA EL MANEJADOR PARA UN PANEL PARTIDO Y SUS DOS LADOS
    public DividerStartPositionHandler(JSplitPane split, Component leftPanel, Component rightPanel) {
        this.split = split;
        this.leftPanel = leftPanel;
        this.rightPanel = rightPanel;
    }
    
    // FIJA LA POSICION DEL DIVISOR LA PRIMERA VEZ QUE EL PANEL TIENE ANCHO, SIN DEJAR EL IZQUIERDO MUY ANGOSTO
    @Override
    public void componentResized(ComponentEvent event) {
        if (split.getWidth() == 0) return;
        
        split.removeComponentListener(this);
        
        int availableWidth = split.getWidth() - split.getDividerSize();
        int maxRightWidth = availableWidth - leftPanel.getMinimumSize().width;
        int rightWidth = Math.min(rightPanel.getPreferredSize().width, maxRightWidth);
        
        split.setDividerLocation(availableWidth - rightWidth);
    }
}