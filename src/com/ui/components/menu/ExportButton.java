package com.ui.components.menu;

import javax.swing.JButton;

import com.ui.styles.Palette;
import com.ui.styles.Styles;

// BOTON QUE ABRE LA LISTA DE FORMATOS PARA EXPORTAR
public class ExportButton extends JButton {
    private static final int VERTICAL_PADDING = 0;
    private static final int HORIZONTAL_PADDING = 4;
    private static final float FONT_SIZE = 12f;
    
    private final OptionsPopup popup;
    
    // CREA EL BOTON CON SU ETIQUETA Y LAS OPCIONES QUE SE ABREN AL PULSARLO
    public ExportButton(String label, MenuOption[] options) {
        super(label);
        
        popup = new OptionsPopup(options);
        
        configureAppearance();
        
        addActionListener(event -> popup.show(this, 0, getHeight()));
    }
    
    // DA AL BOTON SU COLOR, SU FUENTE Y QUITA LOS BORDES DE ENFOQUE
    private void configureAppearance() {
        Styles.setPadding(this, VERTICAL_PADDING, HORIZONTAL_PADDING);
        Styles.setFont(this, FONT_SIZE, true);
        
        setForeground(Palette.SURFACE);
        setOpaque(true);
        setBackground(Palette.ACCENT);
        
        Styles.setBorder(this, Palette.ACCENT);
        setBorderPainted(false);
        setFocusPainted(false);
    }
}