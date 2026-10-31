package com.ui.gates;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;

abstract class LogicGateIcon extends JPanel {
    
    private static final int VIEW_SIZE = 24;
    private final int iconSize;
    
    LogicGateIcon(int size) {
        if (size <= 0) {
            size = 240;
        }
        this.iconSize = size;
        
        Dimension preferredSize = new Dimension(size, size);
        
        setPreferredSize(preferredSize);
        setMinimumSize(preferredSize);
        setOpaque(false);
    }
    
    @Override
    protected final void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            double renderedSize = Math.min(iconSize, Math.min(getWidth(), getHeight()));
            
            double scale = renderedSize / VIEW_SIZE;
            
            double offsetX = (getWidth() - VIEW_SIZE * scale) / 2.0;
            double offsetY = (getHeight() - VIEW_SIZE * scale) / 2.0;
            
            g2d.translate(offsetX, offsetY);
            g2d.scale(scale, scale);
            
            drawGate(g2d);
        }
        
        finally {
            g2d.dispose();
        }
    }
    
    protected abstract void drawGate(Graphics2D g2d);
    
    protected final void drawInversionBubble(Graphics2D g2d, double centerX) {
        g2d.draw(new Ellipse2D.Double(centerX - 2, 10, 4, 4));
    }
}
