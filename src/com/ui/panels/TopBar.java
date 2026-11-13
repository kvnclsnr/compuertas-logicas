package com.ui.panels;

import com.ui.styles.Palette;
import com.ui.styles.Styles;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

public class TopBar extends JPanel {
    
    // TOOLBAR
    private static final String[] MENU_NAMES = { "Archivo", "Editar", "Ver", "Ayuda" };
    private static final String[][] MENU_OPTIONS = {
        { "Nuevo", "Abrir...", "Guardar" },
        { "Deshacer", "Rehacer" },
        { "Acercar", "Alejar" },
        { "Acerca de" },
    };
    
    // EXPORT
    // private static final String EXPORT_LABEL = "Exportar";
    // private static final String[] EXPORT_OPTIONS = { "JSON", "Tabla de verdad" };
    
    public TopBar() {
        UIManager.put("Menu.selectionForeground", Palette.ACCENT);
        UIManager.put("Menu.selectionBackground", Palette.SURFACE);
        UIManager.put("MenuItem.selectionForeground", Palette.INK);
        UIManager.put("MenuItem.selectionBackground", Palette.BACKGROUND);
        
        setLayout(new BorderLayout());
        Styles.setOpaque(this, true);
        Styles.setBackground(this, Palette.SURFACE);
        
        Styles.setPadding(this, 0, 16);
        Styles.setBorder(this, 0, 0, 1, 0, Palette.LINE);
        
        setPreferredSize(new Dimension(0, 36));
        
        add(createMenus(), BorderLayout.WEST);
        // add(createExportArea(), BorderLayout.EAST);
    }
    
    // COMPONENTS
    
    // Nada mas es pa centrar verticalmente
    private JPanel createMenus() {
        JMenuBar menuBar = new JMenuBar();
        Styles.setBorder(menuBar, BorderFactory.createEmptyBorder());
        
        for (int i = 0; i < MENU_NAMES.length; i++) {
            menuBar.add(createMenu(MENU_NAMES[i], MENU_OPTIONS[i]));
        }
        
        JPanel menus = new JPanel(new GridBagLayout());
        
        Styles.setOpaque(menus, true);
        Styles.setBackground(menus, Palette.SURFACE);
        
        GridBagConstraints constraints = new GridBagConstraints();
        
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weighty = 1;
        
        menus.add(menuBar, constraints);
        
        return menus;
    }
    
    // Cada botoncito
    private JMenu createMenu(String name, String[] options) {
        JMenu menu = new JMenu(name);
        
        Styles.setFont(menu, 14f);
        Styles.setForeground(menu, Palette.INK);
        
        Styles.setOpaque(menu, true);
        Styles.setBackground(menu, Palette.SURFACE);
        
        Styles.setBorder(menu, BorderFactory.createEmptyBorder());
        Styles.setPadding(menu, 0, 8);
        
        configurePopup(menu.getPopupMenu(), options);
        
        return menu;
    }
    
    private void configurePopup(JPopupMenu popup, String[] options) {
        Styles.setBackground(popup, Palette.SURFACE);
        
        Styles.setBorder(popup, BorderFactory.createEmptyBorder());
        Styles.setBorder(popup, Palette.LINE);
        
        for (String option : options) {
            popup.add(createPopupItem(option));
        }
        
        popup.setPreferredSize(new Dimension(224, popup.getPreferredSize().height));
    }
    
    private JMenuItem createPopupItem(String text) {
        JMenuItem item = new JMenuItem(text);
        Styles.setBorder(item, BorderFactory.createEmptyBorder());
        Styles.setPadding(item, 8);
        
        Styles.setFont(item, 12f);
        Styles.setForeground(item, Palette.MUTED);
        
        Styles.setOpaque(item, true);
        Styles.setBackground(item, Palette.SURFACE);
        
        return item;
    }
}
