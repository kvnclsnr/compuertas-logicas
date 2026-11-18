package com.ui.layouts.main;

import com.ui.layouts.canvas.Canvas;
import com.ui.layouts.sidePanel.SidePanel;
import com.ui.layouts.workspace.Workspace;

import com.ui.styles.Palette;
import com.ui.styles.Styles;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class Content extends JPanel {
    private final Workspace workspace;

    // ORGANIZA EL ÁREA DE TRABAJO DENTRO DEL CONTENIDO PRINCIPAL
    public Content() {
        this(new Workspace());
    }

    public Content(Workspace workspace) {
        this.workspace = workspace;
        setLayout(new BorderLayout(0, 0));
        Styles.setPadding(this, 16);
        
        Styles.setBackground(this, Palette.BACKGROUND);
        
        add(this.workspace, BorderLayout.CENTER);
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public Canvas getCanvas() {
        return workspace != null ? workspace.getCanvas() : null;
    }

    public SidePanel getSidePanel() {
        return workspace != null ? workspace.getSidePanel() : null;
    }
}
