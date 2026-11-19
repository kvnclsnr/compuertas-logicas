package com.ui.components.menu;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;

import com.ui.styles.Palette;

// LISTA DE OPCIONES QUE SE ABRE DEBAJO DE UN BOTON
public class OptionsPopup extends JPopupMenu {
    private static final int POPUP_WIDTH = 224;
    
    // CREA LA LISTA CON UNA OPCION POR CADA ELEMENTO
    public OptionsPopup(MenuOption[] options) {
        fillPopup(this, options);
    }
    
    // DA ESTILO A UNA LISTA Y LE AGREGA SUS OPCIONES, ES ESTATICO PORQUE UN JMENU TRAE SU PROPIA LISTA
    public static void fillPopup(JPopupMenu popup, MenuOption[] options) {
        popup.setBackground(Palette.SURFACE);
        popup.setBorder(BorderFactory.createLineBorder(Palette.LINE));
        
        for (MenuOption option : options) {
            popup.add(new PopupItem(option));
        }
        
        popup.setPreferredSize(new Dimension(POPUP_WIDTH, popup.getPreferredSize().height));
    }
}