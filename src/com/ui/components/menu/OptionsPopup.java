package com.ui.components.menu;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;

import com.ui.styles.Palette;
import com.ui.styles.Styles;

public class OptionsPopup extends JPopupMenu {
    private static final int POPUP_WIDTH = 224;
    
    // CREA UN POPUP CON UNA OPCIÓN POR CADA TEXTO DE LA LISTA
    public OptionsPopup(String[] options) {
        fill(this, options);
    }
    
    // DA ESTILO A UN POPUP Y LE AGREGA SUS OPCIONES
    // ES STATIC PORQUE MENUBUTTON TAMBIÉN LO USA, YA QUE UN JMENU TRAE SU PROPIO POPUP
    public static void fill(JPopupMenu popup, String[] options) {
        Styles.setBackground(popup, Palette.SURFACE);
        
        Styles.setBorder(popup, BorderFactory.createEmptyBorder());
        Styles.setBorder(popup, Palette.LINE);
        
        for (String option : options) {
            popup.add(new PopupItem(option));
        }
        
        popup.setPreferredSize(new Dimension(POPUP_WIDTH, popup.getPreferredSize().height));
    }
}