package com.ui.panels;

import com.ui.styles.Palette;
import com.ui.styles.Styles;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class Content extends JPanel {
    public Content() {
        setLayout(new BorderLayout(0, 0));
        Styles.setPadding(this, 16);
        
        Styles.setBackground(this, Palette.BACKGROUND);
        
        add(new Workspace(), BorderLayout.CENTER);
    }
}
