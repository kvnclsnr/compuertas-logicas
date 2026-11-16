package com.ui.components.common;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.ui.styles.Styles;

public class CenteredPanel extends JPanel {
    
    // CENTRA UN COMPONENTE VERTICALMENTE; EL ANCHOR DICE SI QUEDA A LA IZQUIERDA O A LA DERECHA
    public CenteredPanel(JComponent content, int anchor) {
        super(new GridBagLayout());
        
        Styles.setOpaque(this, false);
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = anchor;
        constraints.weighty = 1;
        
        add(content, constraints);
    }
}