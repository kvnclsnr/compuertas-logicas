package com.ui.icons;

import com.engine.LogicSource;
import com.engine.gates.Gate;
import com.ui.circuit.pin.Pin;
import com.ui.layouts.canvas.Canvas;
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

public abstract class LogicGate extends JPanel {
    
    // LIENZO DE 28 UNIDADES
    private static final int VIEW_SIZE = 28;
    
    private final int iconSize;
    protected Gate engineGate;
    
    // CREA LA COMPUERTA Y CONFIGURA SU TAMAÑO
    public LogicGate(int size) {
        if (size <= 0) {
            size = 240;
        }
        this.iconSize = size;
        
        Dimension preferredSize = new Dimension(size, size);
        
        setPreferredSize(preferredSize);
        setMinimumSize(preferredSize);
        setOpaque(false);
    }
    
    // DEVUELVE LA COMPUERTA QUE EVALÚA LA LÓGICA
    public Gate getEngineGate() {
        return engineGate;
    }
    
    // INDICA EL VALOR ACTUAL DE LA SALIDA
    public boolean getOutputValue() {
        return engineGate != null && engineGate.getValue();
    }
    
    // DIBUJA LA FORMA PROPIA DE CADA COMPUERTA
    protected abstract void drawGate(Graphics2D g2d);
    
    // DIBUJA EL CÍRCULO QUE INDICA UNA INVERSIÓN
    protected final void drawInversionBubble(Graphics2D g2d, double centerX) {
        g2d.draw(new Ellipse2D.Double(centerX - 1.5, 12.5, 3, 3));
    }
    
    public void setPinInput(int index, LogicSource input) {
        engineGate.setInput(index, input);
    }
    
    public void clearPinInput(int index) {
        engineGate.setInput(index, null);
    }
    
    // CALCULA LA POSICIÓN DE LOS PINES DE LA COMPUERTA
    public List<Pin> getPins() {
        List<Pin> pins = new ArrayList<>();
        
        int w = getWidth() > 0 ? getWidth() : iconSize;
        int h = getHeight() > 0 ? getHeight() : iconSize;
        
        double renderedSize = Math.min(iconSize, Math.min(w, h));
        double scale = renderedSize / VIEW_SIZE;
        
        double offsetX = (w - VIEW_SIZE * scale) / 2.0;
        double offsetY = (h - VIEW_SIZE * scale) / 2.0;
        
        int[] inputYPositions = getInputPinYPositions();
        
        for (int index = 0; index < inputYPositions.length; index++) {
            pins.add(createPin(
                Pin.Type.INPUT, 2, inputYPositions[index], index,
                offsetX, offsetY, scale
            ));
        }
        
        pins.add(createPin(Pin.Type.OUTPUT, 26, 14, 0, offsetX, offsetY, scale));
        
        return pins;
    }
    
    // DEVUELVE LAS POSICIONES VERTICALES DE LAS ENTRADAS
    private int[] getInputPinYPositions() {
        return this instanceof Not ? new int[] { 14 } : new int[] { 11, 17 };
    }
    
    // CREA UN PIN EN LA POSICIÓN ESCALADA DEL LIENZO
    private Pin createPin(Pin.Type type, int x, int y, int index, double offsetX, double offsetY, double scale) {
        int relativeX = (int) Math.round(offsetX + x * scale);
        int relativeY = (int) Math.round(offsetY + y * scale);
        
        return new Pin(this, type, relativeX, relativeY, index);
    }
    
    // DIBUJA LOS TERMINALES DE ENTRADA Y SALIDA
    private void drawPinTerminals(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(0.5f));
        
        for (int y : getInputPinYPositions()) {
            fillTerminal(g2d, 2, y);
        }
        
        fillTerminal(g2d, 26, 14);
    }
    
    // RELLENA Y DELIMITA UN TERMINAL
    private void fillTerminal(Graphics2D g2d, double x, double y) {
        double r = 1.5;
        
        g2d.setColor(Palette.SURFACE);
        g2d.fill(new Ellipse2D.Double(x - r, y - r, r * 2, r * 2));
        
        g2d.setColor(Palette.LINE);
        g2d.draw(new Ellipse2D.Double(x - r, y - r, r * 2, r * 2));
    }
    
    // DIBUJA LA COMPUERTA Y SUS TERMINALES EN EL COMPONENTE
    @Override
    protected final void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        
        boolean inCanvas = (getParent() instanceof Canvas);
        boolean active = inCanvas && getOutputValue();
        
        g2d.setColor(active ? Palette.ACCENT : Palette.INK);
        g2d.setStroke(
            new BasicStroke(1.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
        );
        
        double renderedSize = Math.min(iconSize, Math.min(getWidth(), getHeight()));
        
        double scale = renderedSize / VIEW_SIZE;
        
        double offsetX = (getWidth() - VIEW_SIZE * scale) / 2.0;
        double offsetY = (getHeight() - VIEW_SIZE * scale) / 2.0;
        
        g2d.translate(offsetX, offsetY);
        g2d.scale(scale, scale);
        
        drawGate(g2d);
        
        if (inCanvas) drawPinTerminals(g2d);
        
        g2d.dispose();
    }
}
