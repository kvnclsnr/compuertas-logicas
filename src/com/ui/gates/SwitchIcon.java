package com.ui.gates;

import com.engine.LogicInput;
import com.ui.circuit.Pin;
import com.ui.panels.Canvas;
import com.ui.styles.Palette;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;

public class SwitchIcon extends JPanel {
    private final int size;
    private final LogicInput logicInput;
    private static int counter = 1;

    public SwitchIcon(int size) {
        this.size = size;
        this.logicInput = new LogicInput("Inp_" + (counter++), false);

        Dimension dim = new Dimension(size, size);
        setPreferredSize(dim);
        setMinimumSize(dim);
        setOpaque(false);
    }

    public void toggle() {
        boolean newState = !logicInput.getValue();
        logicInput.setValue(newState);
        repaint();
    }

    public boolean isOn() {
        return logicInput.getValue();
    }

    public LogicInput getLogicInput() {
        return logicInput;
    }

    public Pin getOutputPin() {
        return new Pin(this, Pin.Type.OUTPUT, size, size / 2, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        boolean inCanvas = (getParent() instanceof Canvas);
        boolean active = inCanvas && isOn();

        int pinWidth = size / 6;
        int availableWidth = size - pinWidth;
        int cx = availableWidth / 2;
        int cy = size / 2;

        int outerRadius = (int) (Math.min(availableWidth, size) * 0.40);
        int outerDiameter = outerRadius * 2;

        // Dibujar el pin de salida a la derecha
        Color pinColor = active ? Palette.ACCENT : Palette.MUTED;
        g2.setColor(pinColor);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(cx + outerRadius, cy, size, cy);

        // Terminal pad circle solo en el canvas
        if (inCanvas) {
            double tr = 3.0;
            g2.setColor(Palette.SURFACE);
            g2.fill(new Ellipse2D.Double(size - tr * 2, cy - tr, tr * 2, tr * 2));
            g2.setColor(pinColor);
            g2.draw(new Ellipse2D.Double(size - tr * 2, cy - tr, tr * 2, tr * 2));
        }

        // Fondo y borde del círculo exterior
        g2.setColor(Palette.SURFACE);
        g2.fillOval(cx - outerRadius, cy - outerRadius, outerDiameter, outerDiameter);

        g2.setColor(active ? Palette.ACCENT : Palette.LINE);
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval(cx - outerRadius, cy - outerRadius, outerDiameter, outerDiameter);

        // Círculo interior dinámico según el estado
        double innerRatio = active ? 0.75 : 0.28;
        int innerRadius = (int) (outerRadius * innerRatio);
        int innerDiameter = innerRadius * 2;

        Color innerColor = active ? Palette.ACCENT : Palette.MUTED;
        g2.setColor(innerColor);
        g2.fillOval(cx - innerRadius, cy - innerRadius, innerDiameter, innerDiameter);

        g2.dispose();
    }
}
