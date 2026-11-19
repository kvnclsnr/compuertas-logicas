package com.ui.layouts.workspace;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.ui.events.resize.DividerStartPositionHandler;
import com.ui.layouts.canvas.Canvas;
import com.ui.layouts.canvas.CanvasView;
import com.ui.layouts.sidePanel.SidePanel;

// AREA DE TRABAJO CON LA PALETA A LA IZQUIERDA Y EL LIENZO Y EL PANEL LATERAL A SU DERECHA
public class Workspace extends JPanel {
    private static final int PALETTE_GAP = 8;
    private static final int CANVAS_MIN_WIDTH = 256;
    private static final int SIDE_PANEL_MIN_WIDTH = 224;
    private static final int DIVIDER_SIZE = 8;
    private static final double CANVAS_RESIZE_WEIGHT = 0.75;
    
    // CREA EL AREA DE TRABAJO Y USA LA PALETA COMO ZONA DE BORRADO DEL LIENZO
    public Workspace(Canvas canvas) {
        super(new BorderLayout(PALETTE_GAP, 0));
        
        GatePalette gatePalette = new GatePalette();
        canvas.setDeleteZone(gatePalette);
        
        CanvasView canvasView = new CanvasView(canvas);
        SidePanel sidePanel = new SidePanel(canvas);
        
        add(gatePalette, BorderLayout.WEST);
        add(createSplit(canvasView, sidePanel), BorderLayout.CENTER);
    }
    
    // CREA EL PANEL PARTIDO ENTRE LA VISTA DEL LIENZO Y EL PANEL LATERAL
    private JSplitPane createSplit(CanvasView canvasView, SidePanel sidePanel) {
        canvasView.setMinimumSize(new Dimension(CANVAS_MIN_WIDTH, 0));
        sidePanel.setMinimumSize(new Dimension(SIDE_PANEL_MIN_WIDTH, 0));
        
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, canvasView, sidePanel);
        
        split.setContinuousLayout(true);
        split.setResizeWeight(CANVAS_RESIZE_WEIGHT);
        split.setDividerSize(DIVIDER_SIZE);
        split.setBorder(BorderFactory.createEmptyBorder());
        split.setUI(new WorkspaceSplit());
        split.addComponentListener(new DividerStartPositionHandler(split, canvasView, sidePanel));
        
        return split;
    }
}