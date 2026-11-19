package com.ui.components.menu;

import java.awt.event.ActionListener;

// OPCION DE UN MENU CON SU TEXTO Y LO QUE HACE AL ELEGIRLA
public record MenuOption(String label, ActionListener action) {
    
    // CREA UNA OPCION QUE TODAVIA NO HACE NADA
    public MenuOption(String label) {
        this(label, event -> { });
    }
}