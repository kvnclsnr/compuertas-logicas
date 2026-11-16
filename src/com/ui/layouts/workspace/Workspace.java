package com.ui.layouts.workspace;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.ui.events.resize.DividerStartPositionHandler;
import com.ui.layouts.canvas.Canvas;
import com.ui.layouts.sidePanel.SidePanel;
import com.ui.styles.Styles;

public class Workspace extends JPanel {
    // ORGANIZA LA PALETA, EL LIENZO Y EL PANEL LATERAL
    public Workspace() {
        setLayout(new BorderLayout(8, 0));
        
        GatePalette gatePalette = new GatePalette();
        
        Canvas canvas = new Canvas();
        SidePanel sidePanel = new SidePanel(canvas);
        
        canvas.setMinimumSize(new Dimension(256, 0));
        sidePanel.setMinimumSize(new Dimension(224, 0));
        
        JSplitPane workspaceSplit = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            canvas,
            sidePanel
        );
        
        // ACTUALIZA EL CONTENIDO MIENTRAS SE MUEVE EL DIVISOR
        workspaceSplit.setContinuousLayout(true);
        workspaceSplit.setResizeWeight(0.75);
        workspaceSplit.setDividerSize(8);
        
        Styles.setBorder(workspaceSplit, BorderFactory.createEmptyBorder());
        
        workspaceSplit.setUI(new WorkspaceSplit());
        
        // ESTABLECE LA POSICIÓN INICIAL DEL DIVISOR CUANDO SE CONOCE EL TAMAÑO
        workspaceSplit.addComponentListener(
            new DividerStartPositionHandler(workspaceSplit, canvas, sidePanel)
        );
        
        add(gatePalette, BorderLayout.WEST);
        add(workspaceSplit, BorderLayout.CENTER);
    }
}