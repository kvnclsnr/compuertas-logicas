package com.ui.circuit;

import com.ui.styles.Palette;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import javax.swing.JComponent;

public class Wire {
    private final Pin source;
    private final Pin target;

    public Wire(Pin source, Pin target) {
        this.source = source;
        this.target = target;
    }

    public Pin getSource() {
        return source;
    }

    public Pin getTarget() {
        return target;
    }

    public boolean isActive() {
        return source != null && source.getValue();
    }

    public boolean isConnectedTo(JComponent component) {
        return (source != null && source.getOwner() == component) || (target != null && target.getOwner() == component);
    }

    public void draw(Graphics2D g2) {
        Point p1 = source.getLocationOnCanvas();
        Point p2 = target.getLocationOnCanvas();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color wireColor = isActive() ? Palette.ACCENT : Palette.MUTED;
        g2.setColor(wireColor);
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        int dx = Math.abs(p2.x - p1.x);
        int controlDist = Math.max(25, dx / 2);

        CubicCurve2D curve = new CubicCurve2D.Double(
            p1.x, p1.y,
            p1.x + controlDist, p1.y,
            p2.x - controlDist, p2.y,
            p2.x, p2.y
        );

        g2.draw(curve);

        // Terminal dots
        g2.fillOval(p1.x - 3, p1.y - 3, 6, 6);
        g2.fillOval(p2.x - 3, p2.y - 3, 6, 6);
    }
}
