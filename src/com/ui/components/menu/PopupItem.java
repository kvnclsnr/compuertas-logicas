package com.ui.components.menu;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;

import com.ui.styles.Palette;
import com.ui.styles.Styles;

public class PopupItem extends JMenuItem {
    
    // CREA UNA OPCIÓN DE POPUP CON SU TEXTO Y SU ESTILO
    public PopupItem(String text) {
        super(text);
        
        Styles.setBorder(this, BorderFactory.createEmptyBorder());
        Styles.setPadding(this, 8);
        
        Styles.setFont(this, 12f);
        Styles.setForeground(this, Palette.MUTED);
        
        Styles.setOpaque(this, true);
        Styles.setBackground(this, Palette.SURFACE);
    }
}