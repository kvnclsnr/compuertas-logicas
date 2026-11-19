package com.ui.components.menu;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;

import com.ui.styles.Palette;
import com.ui.styles.Styles;

// OPCION DE UNA LISTA DESPLEGABLE
public class PopupItem extends JMenuItem {
    private static final int PADDING = 8;
    private static final float FONT_SIZE = 12f;
    
    // CREA LA OPCION CON SU TEXTO, SU ESTILO Y SU ACCION
    public PopupItem(MenuOption option) {
        super(option.label());
        
        setBorder(BorderFactory.createEmptyBorder());
        Styles.setPadding(this, PADDING);
        
        Styles.setFont(this, FONT_SIZE);
        setForeground(Palette.MUTED);
        
        setOpaque(true);
        setBackground(Palette.SURFACE);
        
        addActionListener(option.action());
    }
}