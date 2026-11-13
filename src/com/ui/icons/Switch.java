package com.ui.icons;

import com.engine.LogicInput;
import com.ui.circuit.Pin;
import com.ui.panels.Canvas;
import com.ui.styles.Palette;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;

public class Switch extends JPanel {
    
    private static final int VIEW_SIZE = 28;
    private static final double PIN_RADIUS = 1.5;
    
    private static final double PIN_X = 26;
    private static final double PIN_Y = 14;
    
    private final int iconSize;
    private final LogicInput logicInput;
    private static int counter = 1;
    
    public Switch(int size) {
        if (size <= 0) {
            size = 240;
        }
        this.iconSize = size;
        this.logicInput = new LogicInput("__inp-" + (counter++) + "__", false);
        
        Dimension preferredSize = new Dimension(size, size);
        setPreferredSize(preferredSize);
        setMinimumSize(preferredSize);
        setOpaque(false);
    }
    
    public void toggle() {
        logicInput.setValue(!logicInput.getValue());
        repaint();
    }
    
    public boolean isOn() {
        return logicInput.getValue();
    }
    
    public LogicInput getLogicInput() {
        return logicInput;
    }
    
    public Pin getOutputPin() {
        int width = getWidth() > 0 ? getWidth() : iconSize;
        int height = getHeight() > 0 ? getHeight() : iconSize;
        
        double renderedSize = Math.min(iconSize, Math.min(width, height));
        double scale = renderedSize / VIEW_SIZE;
        
        double offsetX = (width - VIEW_SIZE * scale) / 2.0;
        double offsetY = (height - VIEW_SIZE * scale) / 2.0;
        
        int x = (int) Math.round(offsetX + PIN_X * scale);
        int y = (int) Math.round(offsetY + PIN_Y * scale);
        
        return new Pin(this, Pin.Type.OUTPUT, x, y, 0);
    }
    
    private void drawSwitch(Graphics2D g2d, boolean active) {
        double centerX = VIEW_SIZE * 5.0 / 12;
        double centerY = VIEW_SIZE / 2.0;
        double outerRadius = VIEW_SIZE / 4.0;
        double outerDiameter = outerRadius * 2;
        
        g2d.setColor(active ? Palette.ACCENT : Palette.INK);
        g2d.setStroke(new BasicStroke(1.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(
            (int) Math.round(centerX + outerRadius),
            (int) centerY,
            (int) PIN_X,
            (int) PIN_Y
        );
        
        g2d.setColor(Palette.SURFACE);
        g2d.fill(new Ellipse2D.Double(
            centerX - outerRadius,
            centerY - outerRadius,
            outerDiameter,
            outerDiameter
        ));
        
        g2d.setColor(active ? Palette.ACCENT : Palette.INK);
        g2d.draw(new Ellipse2D.Double(
            centerX - outerRadius,
            centerY - outerRadius,
            outerDiameter,
            outerDiameter
        ));
        
        double innerRadius = outerRadius * (active ? 0.75 : 0);
        g2d.setColor(active ? Palette.ACCENT : Palette.INK);
        g2d.fill(new Ellipse2D.Double(
            centerX - innerRadius,
            centerY - innerRadius,
            innerRadius * 2,
            innerRadius * 2
        ));
    }
    
    private void drawPinTerminal(Graphics2D g2d) {
        double diameter = PIN_RADIUS * 2;
        g2d.setStroke(new BasicStroke(0.5f));
        
        g2d.setColor(Palette.SURFACE);
        g2d.fill(new Ellipse2D.Double(
            PIN_X - PIN_RADIUS,
            PIN_Y - PIN_RADIUS,
            diameter,
            diameter
        ));
        
        g2d.setColor(Palette.LINE);
        g2d.draw(new Ellipse2D.Double(
            PIN_X - PIN_RADIUS,
            PIN_Y - PIN_RADIUS,
            diameter,
            diameter
        ));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        
        boolean inCanvas = getParent() instanceof Canvas;
        boolean active = inCanvas && isOn();
        
        double renderedSize = Math.min(iconSize, Math.min(getWidth(), getHeight()));
        double scale = renderedSize / VIEW_SIZE;
        double offsetX = (getWidth() - VIEW_SIZE * scale) / 2.0;
        double offsetY = (getHeight() - VIEW_SIZE * scale) / 2.0;
        
        g2d.translate(offsetX, offsetY);
        g2d.scale(scale, scale);
        
        drawSwitch(g2d, active);
        if (inCanvas) {
            drawPinTerminal(g2d);
        }
        
        g2d.dispose();
    }
}
