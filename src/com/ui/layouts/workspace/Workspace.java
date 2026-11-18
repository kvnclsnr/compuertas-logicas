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
    private final GatePalette gatePalette;
    private final Canvas canvas;
    private final SidePanel sidePanel;
    private final JSplitPane workspaceSplit;

    // ORGANIZA LA PALETA, EL LIENZO Y EL PANEL LATERAL
    public Workspace() {
        setLayout(new BorderLayout(8, 0));
        
        gatePalette = new GatePalette();
        
        canvas = new Canvas();
        canvas.setDeleteZone(gatePalette);
        
        sidePanel = new SidePanel(canvas);
        
        canvas.setMinimumSize(new Dimension(256, 0));
        sidePanel.setMinimumSize(new Dimension(224, 0));
        
        workspaceSplit = new JSplitPane(
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

    public Canvas getCanvas() {
        return canvas;
    }

    public SidePanel getSidePanel() {
        return sidePanel;
    }

    public GatePalette getGatePalette() {
        return gatePalette;
    }

    public JSplitPane getWorkspaceSplit() {
        return workspaceSplit;
    }
}
