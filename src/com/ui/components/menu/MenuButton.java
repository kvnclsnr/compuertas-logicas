package com.ui.components.menu;

import javax.swing.BorderFactory;
import javax.swing.JMenu;

import com.ui.styles.Palette;
import com.ui.styles.Styles;

public class MenuButton extends JMenu {
    
    // CREA UN BOTÓN DE LA BARRA CON SU NOMBRE Y LAS OPCIONES QUE SE ABREN AL PULSARLO
    public MenuButton(String name, String[] options) {
        super(name);
        
        Styles.setFont(this, 14f);
        Styles.setForeground(this, Palette.INK);
        
        Styles.setOpaque(this, true);
        Styles.setBackground(this, Palette.SURFACE);
        
        Styles.setBorder(this, BorderFactory.createEmptyBorder());
        Styles.setPadding(this, 0, 8);
        
        OptionsPopup.fill(getPopupMenu(), options);
    }
}