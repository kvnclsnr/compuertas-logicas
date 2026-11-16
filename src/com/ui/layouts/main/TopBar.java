package com.ui.layouts.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.swing.BorderFactory;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.ui.components.common.CenteredPanel;
import com.ui.components.menu.ExportButton;
import com.ui.components.menu.MenuButton;
import com.ui.styles.Palette;
import com.ui.styles.Styles;

public class TopBar extends JPanel {
    
    // TOOLBAR
    private static final String[] MENU_NAMES = { "Archivo", "Editar", "Ver", "Ayuda" };
    private static final String[][] MENU_OPTIONS = {
        { "Nuevo", "Abrir", "Guardar" },
        { "Deshacer", "Rehacer" },
        { "Acercar", "Alejar" },
        { "Acerca de" },
    };
    
    // EXPORT
    private static final String EXPORT_LABEL = "Exportar";
    private static final String[] EXPORT_OPTIONS = { "JSON", "Tabla de verdad" };
    
    // ORGANIZA LOS MENÚS Y EL BOTÓN DE EXPORTACIÓN EN LA BARRA SUPERIOR
    public TopBar() {
        configureMenuColors();
        
        setLayout(new BorderLayout());
        Styles.setOpaque(this, true);
        Styles.setBackground(this, Palette.SURFACE);
        
        Styles.setPadding(this, 0, 16);
        Styles.setBorder(this, 0, 0, 1, 0, Palette.LINE);
        
        setPreferredSize(new Dimension(0, 36));
        
        add(new CenteredPanel(createMenuBar(), GridBagConstraints.WEST), BorderLayout.WEST);
        add(new CenteredPanel(createExportButton(), GridBagConstraints.EAST), BorderLayout.EAST);
    }
    
    // COMPONENTS
    
    // DEFINE LOS COLORES DE SELECCIÓN DE LOS MENÚS EN LA INTERFAZ
    private void configureMenuColors() {
        UIManager.put("Menu.selectionForeground", Palette.ACCENT);
        UIManager.put("Menu.selectionBackground", Palette.SURFACE);
        UIManager.put("MenuItem.selectionForeground", Palette.INK);
        UIManager.put("MenuItem.selectionBackground", Palette.BACKGROUND);
    }
    
    // CREA CADA MENÚ CON LAS OPCIONES DE SU MISMA POSICIÓN EN LA LISTA
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        Styles.setBorder(menuBar, BorderFactory.createEmptyBorder());
        
        for (int i = 0; i < MENU_NAMES.length; i++) {
            menuBar.add(new MenuButton(MENU_NAMES[i], MENU_OPTIONS[i]));
        }
        
        return menuBar;
    }
    
    // CREA EL BOTÓN CON LOS FORMATOS DISPONIBLES PARA EXPORTAR
    private ExportButton createExportButton() {
        return new ExportButton(EXPORT_LABEL, EXPORT_OPTIONS);
    }
}