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
import com.ui.components.menu.MenuOption;
import com.ui.export.TextExportAction;
import com.ui.importer.TextImportAction;
import com.ui.layouts.canvas.Canvas;
import com.ui.styles.Palette;
import com.ui.styles.Styles;
import com.ui.layouts.canvas.NewCircuitAction;

// BARRA SUPERIOR CON LOS MENUS A LA IZQUIERDA Y EL BOTON DE EXPORTAR A LA DERECHA
public class TopBar extends JPanel {
    
    // NOMBRE DE UN MENU CON SUS OPCIONES
    private record Menu(String name, MenuOption[] options) { }
    
    private static final String EXPORT_LABEL = "Exportar";
    private static final String EXPORT_TEXT_LABEL = "TXT";
    private static final String EXPORT_TRUTH_TABLE_LABEL = "Tabla de verdad";
    
    private static final int BAR_HEIGHT = 36;
    private static final int HORIZONTAL_PADDING = 16;
    private static final int BORDER_WIDTH = 1;
    
    // CREA LA BARRA CON LOS MENUS Y EL BOTON DE EXPORTAR
    public TopBar(Canvas canvas) {
        super(new BorderLayout());
        
        configureMenuColors();
        configureAppearance();
        
        add(new CenteredPanel(createMenuBar(canvas), GridBagConstraints.WEST), BorderLayout.WEST);
        add(new CenteredPanel(createExportButton(canvas), GridBagConstraints.EAST), BorderLayout.EAST);
    }
    
    // DEFINE LOS COLORES DE SELECCION DE LOS MENUS
    private void configureMenuColors() {
        UIManager.put("Menu.selectionForeground", Palette.ACCENT);
        UIManager.put("Menu.selectionBackground", Palette.SURFACE);
        UIManager.put("MenuItem.selectionForeground", Palette.INK);
        UIManager.put("MenuItem.selectionBackground", Palette.BACKGROUND);
    }
    
    // DA A LA BARRA SU COLOR, SU MARGEN, SU LINEA INFERIOR Y SU ALTO
    private void configureAppearance() {
        setOpaque(true);
        setBackground(Palette.SURFACE);
        
        Styles.setPadding(this, 0, HORIZONTAL_PADDING);
        Styles.setBorder(this, 0, 0, BORDER_WIDTH, 0, Palette.LINE);
        
        setPreferredSize(new Dimension(0, BAR_HEIGHT));
    }
    
    // CREA LOS MENUS, LA OPCION ABRIR CARGA UN ARCHIVO EN EL LIENZO
    private Menu[] createMenus(Canvas canvas) {
        return new Menu[] {
            new Menu("Archivo", new MenuOption[] {
                new MenuOption("Nuevo", new NewCircuitAction(canvas)),
                new MenuOption("Abrir", new TextImportAction(canvas)),
                new MenuOption("Guardar", new TextExportAction(canvas))
            }),
            new Menu("Ayuda", new MenuOption[] {
                new MenuOption("Acerca de")
            })
        };
    }
    
    // CREA LA BARRA DE MENUS CON UN BOTON POR CADA MENU
    private JMenuBar createMenuBar(Canvas canvas) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(BorderFactory.createEmptyBorder());
        
        for (Menu menu : createMenus(canvas)) {
            menuBar.add(new MenuButton(menu.name(), menu.options()));
        }
        
        return menuBar;
    }
    
    // CREA EL BOTON DE EXPORTAR CON UNA OPCION POR CADA FORMATO
    private ExportButton createExportButton(Canvas canvas) {
        MenuOption[] options = {
            new MenuOption(EXPORT_TEXT_LABEL, new TextExportAction(canvas)),
            new MenuOption(EXPORT_TRUTH_TABLE_LABEL)
        };
        
        return new ExportButton(EXPORT_LABEL, options);
    }
}