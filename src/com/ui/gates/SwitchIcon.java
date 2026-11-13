package com.ui.gates;

import com.engine.LogicInput;
import com.ui.styles.Palette;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class SwitchIcon extends JPanel {
    private final int size;
    private final LogicInput logicInput;
    private static int counter = 1;

    public SwitchIcon(int size){
        this.size = size;
        this.logicInput = new LogicInput("Inp_" + (counter++),false);

        Dimension dim = new Dimension(size, size);
        setPreferredSize(dim);
        setMinimumSize(dim);
        setOpaque(false);
    }

    public void toggle(){
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

    @Override
    protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g.create();
    
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

    int pinWidth = size / 6;
    int availableWidth = size - pinWidth;
    int cx = availableWidth / 2;
    int cy = size / 2;
    
    // Radio del círculo exterior
    int outerRadius = (int) (Math.min(availableWidth, size) * 0.40);
    int outerDiameter = outerRadius * 2;

    // 1. Dibujar el pin de salida a la derecha
    Color pinColor = isOn() ? Palette.ACCENT : Palette.MUTED;
    g2.setColor(pinColor);
    g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    g2.drawLine(cx + outerRadius, cy, size, cy);

    // 2. Fondo y borde del círculo exterior
    g2.setColor(Palette.SURFACE);
    g2.fillOval(cx - outerRadius, cy - outerRadius, outerDiameter, outerDiameter);

    g2.setColor(isOn() ? Palette.ACCENT : Palette.LINE);
    g2.setStroke(new BasicStroke(2f));
    g2.drawOval(cx - outerRadius, cy - outerRadius, outerDiameter, outerDiameter);

    // 3. Círculo interior dinámico según el estado
    // Si está encendido: radio del 75%. Si está apagado: radio del 28%
    double innerRatio = isOn() ? 0.75 : 0.28;
    int innerRadius = (int) (outerRadius * innerRatio);
    int innerDiameter = innerRadius * 2;

    Color innerColor = isOn() ? Palette.ACCENT : Palette.MUTED;
    g2.setColor(innerColor);
    g2.fillOval(cx - innerRadius, cy - innerRadius, innerDiameter, innerDiameter);

    g2.dispose();
}

}
