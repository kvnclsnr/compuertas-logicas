package com.ui.components.common;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

// CENTRA UN COMPONENTE VERTICALMENTE Y LO PEGA A UN LADO
public class CenteredPanel extends JPanel {
    
    // CREA EL PANEL, EL ANCHOR DICE SI EL COMPONENTE QUEDA A LA IZQUIERDA O A LA DERECHA
    public CenteredPanel(JComponent content, int anchor) {
        super(new GridBagLayout());
        
        setOpaque(false);
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = anchor;
        constraints.weighty = 1;
        
        add(content, constraints);
    }
}