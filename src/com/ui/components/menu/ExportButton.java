package com.ui.components.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import com.ui.styles.Palette;
import com.ui.styles.Styles;

public class ExportButton extends JButton implements ActionListener {
    private final OptionsPopup popup;
    
    // CREA EL BOTÓN CON SU ETIQUETA Y LAS OPCIONES QUE SE ABREN AL PULSARLO
    public ExportButton(String label, String[] options) {
        super(label);
        
        popup = new OptionsPopup(options);
        
        Styles.setPadding(this, 0, 4);
        
        Styles.setFont(this, 12f, true);
        Styles.setForeground(this, Palette.SURFACE);
        
        Styles.setOpaque(this, true);
        Styles.setBackground(this, Palette.ACCENT);
        
        Styles.setBorder(this, Palette.ACCENT);
        
        Styles.setBorderPainted(this, false);
        Styles.setFocusPainted(this, false);
        
        addActionListener(this);
    }
    
    // MUESTRA LAS OPCIONES DEBAJO DEL BOTÓN CUANDO SE HACE CLIC
    @Override
    public void actionPerformed(ActionEvent event) {
        popup.show(this, 0, getHeight());
    }
}