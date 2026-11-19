package com.ui.layouts.workspace;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.ui.components.gatePalette.GateItem;
import com.ui.icons.ElementType;
import com.ui.styles.Palette;
import com.ui.styles.Styles;

// PALETA CON UNA FILA POR CADA ELEMENTO QUE SE PUEDE ARRASTRAR AL LIENZO, TAMBIEN ES LA ZONA DE BORRADO
public class GatePalette extends JScrollPane {
    private static final int PALETTE_WIDTH = 216;
    private static final int ITEM_GAP = 8;
    private static final int PANEL_PADDING = 8;
    private static final int SCROLL_UNIT = 16;
    
    // CREA LA PALETA CON TODOS LOS TIPOS DE ELEMENTO
    public GatePalette() {
        Styles.setBorder(this, Palette.LINE);
        
        setViewportView(createItemsPanel());
        
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT);
    }
    
    // CREA EL PANEL VERTICAL CON UNA FILA POR CADA TIPO Y UN ESPACIO ENTRE ELLAS
    private JPanel createItemsPanel() {
        JPanel itemsPanel = new JPanel();
        
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(Palette.SURFACE);
        Styles.setPadding(itemsPanel, PANEL_PADDING);
        
        for (ElementType type : ElementType.values()) {
            if (itemsPanel.getComponentCount() > 0) {
                itemsPanel.add(Box.createVerticalStrut(ITEM_GAP));
            }
            
            itemsPanel.add(new GateItem(type));
        }
        
        itemsPanel.setPreferredSize(new Dimension(PALETTE_WIDTH, itemsPanel.getPreferredSize().height));
        
        return itemsPanel;
    }
}