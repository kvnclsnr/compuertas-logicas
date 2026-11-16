package com.ui.components.common;

import java.awt.Graphics;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import com.ui.styles.Palette;

public class Divider extends BasicSplitPaneDivider {
    private static final int DOT_SIZE = 5;
    private static final int DOT_SPACING = 8;
    private static final int DOT_COUNT = 3;
    
    // CREA EL DIVISOR PARA UN PANEL PARTIDO
    public Divider(BasicSplitPaneUI ui) {
        super(ui);
    }
    
    // DIBUJA EL FONDO PLANO Y LOS TRES PUNTOS CENTRADOS
    @Override
    public void paint(Graphics graphics) {
        graphics.setColor(Palette.BACKGROUND);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        
        int dotsHeight = DOT_SIZE + DOT_SPACING * (DOT_COUNT - 1);
        int x = (getWidth() - DOT_SIZE) / 2;
        int y = (getHeight() - dotsHeight) / 2;
        
        graphics.setColor(Palette.ACCENT);
        
        for (int dot = 0; dot < DOT_COUNT; dot++) {
            graphics.fillOval(x, y + dot * DOT_SPACING, DOT_SIZE, DOT_SIZE);
        }
    }
}