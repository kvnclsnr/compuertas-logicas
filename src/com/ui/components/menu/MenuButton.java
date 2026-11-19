package com.ui.components.menu;

import javax.swing.BorderFactory;
import javax.swing.JMenu;

import com.ui.styles.Palette;
import com.ui.styles.Styles;

// BOTON DE LA BARRA SUPERIOR QUE ABRE UN MENU DE OPCIONES
public class MenuButton extends JMenu {
    private static final int VERTICAL_PADDING = 0;
    private static final int HORIZONTAL_PADDING = 8;
    private static final float FONT_SIZE = 14f;
    
    // CREA EL BOTON CON SU NOMBRE Y LAS OPCIONES QUE SE ABREN AL PULSARLO
    public MenuButton(String name, MenuOption[] options) {
        super(name);
        
        Styles.setFont(this, FONT_SIZE);
        setForeground(Palette.INK);
        
        setOpaque(true);
        setBackground(Palette.SURFACE);
        
        setBorder(BorderFactory.createEmptyBorder());
        Styles.setPadding(this, VERTICAL_PADDING, HORIZONTAL_PADDING);
        
        OptionsPopup.fillPopup(getPopupMenu(), options);
    }
}