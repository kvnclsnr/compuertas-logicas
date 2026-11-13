package com.ui.gates;

import com.engine.LogicSource;
import com.engine.gates.BinaryGate;
import com.engine.gates.Gate;
import com.ui.circuit.Pin;
import com.ui.panels.Canvas;
import com.ui.styles.Palette;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public abstract class LogicGateIcon extends JPanel {
    
    private static final int VIEW_SIZE = 24;
    private final int iconSize;
    protected Gate engineGate;
    
    public LogicGateIcon(int size) {
        if (size <= 0) {
            size = 240;
        }
        this.iconSize = size;
        
        Dimension preferredSize = new Dimension(size, size);
        
        setPreferredSize(preferredSize);
        setMinimumSize(preferredSize);
        setOpaque(false);
    }
    
    public Gate getEngineGate() {
        return engineGate;
    }

    public boolean getOutputValue() {
        return engineGate != null && engineGate.evaluate();
    }

    public void setPinInput(int pinIndex, LogicSource source) {
        if (engineGate instanceof BinaryGate) {
            BinaryGate bg = (BinaryGate) engineGate;
            if (pinIndex == 0) {
                bg.setInputA(source);
            } else if (pinIndex == 1) {
                bg.setInputB(source);
            }
        } else if (engineGate instanceof com.engine.gates.NotGate) {
            ((com.engine.gates.NotGate) engineGate).setInput(source);
        }
    }

    public void clearPinInput(int pinIndex) {
        setPinInput(pinIndex, null);
    }

    public List<Pin> getPins() {
        List<Pin> pins = new ArrayList<>();
        
        int w = getWidth() > 0 ? getWidth() : iconSize;
        int h = getHeight() > 0 ? getHeight() : iconSize;
        double renderedSize = Math.min(iconSize, Math.min(w, h));
        double scale = renderedSize / VIEW_SIZE;
        double offsetX = (w - VIEW_SIZE * scale) / 2.0;
        double offsetY = (h - VIEW_SIZE * scale) / 2.0;

        if (this instanceof NotGate) {
            pins.add(new Pin(this, Pin.Type.INPUT, (int) Math.round(offsetX + 2 * scale), (int) Math.round(offsetY + 12 * scale), 0));
        } else {
            pins.add(new Pin(this, Pin.Type.INPUT, (int) Math.round(offsetX + 2 * scale), (int) Math.round(offsetY + 9 * scale), 0));
            pins.add(new Pin(this, Pin.Type.INPUT, (int) Math.round(offsetX + 2 * scale), (int) Math.round(offsetY + 15 * scale), 1));
        }

        pins.add(new Pin(this, Pin.Type.OUTPUT, (int) Math.round(offsetX + 22 * scale), (int) Math.round(offsetY + 12 * scale), 0));

        return pins;
    }
    
    @Override
    protected final void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            
            // Solo evaluar y cambiar de color si está dentro del Canvas de trabajo
            boolean inCanvas = (getParent() instanceof Canvas);
            boolean active = inCanvas && getOutputValue();
            
            g2d.setColor(active ? Palette.ACCENT : Palette.INK);
            g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            double renderedSize = Math.min(iconSize, Math.min(getWidth(), getHeight()));
            
            double scale = renderedSize / VIEW_SIZE;
            
            double offsetX = (getWidth() - VIEW_SIZE * scale) / 2.0;
            double offsetY = (getHeight() - VIEW_SIZE * scale) / 2.0;
            
            g2d.translate(offsetX, offsetY);
            g2d.scale(scale, scale);
            
            drawGate(g2d);
            
            // Solo dibujar los terminales interactivos si está en el lienzo
            if (inCanvas) {
                drawPinTerminals(g2d);
            }
        }
        
        finally {
            g2d.dispose();
        }
    }

    private void drawPinTerminals(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(0.6f));
        if (this instanceof NotGate) {
            fillTerminal(g2d, 2, 12);
        } else {
            fillTerminal(g2d, 2, 9);
            fillTerminal(g2d, 2, 15);
        }
        fillTerminal(g2d, 22, 12);
    }

    private void fillTerminal(Graphics2D g2d, double x, double y) {
        double r = 0.9;
        g2d.setColor(Palette.SURFACE);
        g2d.fill(new Ellipse2D.Double(x - r, y - r, r * 2, r * 2));
        g2d.setColor(Palette.LINE);
        g2d.draw(new Ellipse2D.Double(x - r, y - r, r * 2, r * 2));
    }
    
    protected abstract void drawGate(Graphics2D g2d);
    
    protected final void drawInversionBubble(Graphics2D g2d, double centerX) {
        g2d.draw(new Ellipse2D.Double(centerX - 2, 10, 4, 4));
    }
}
