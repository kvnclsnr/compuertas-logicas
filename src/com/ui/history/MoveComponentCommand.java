package com.ui.history;

import com.ui.layouts.canvas.Canvas;
import java.awt.Point;
import javax.swing.JComponent;

public class MoveComponentCommand implements CanvasCommand {
    private final Canvas canvas;
    private final JComponent component;
    private final Point oldLocation;
    private final Point newLocation;
    
    public MoveComponentCommand(Canvas canvas, JComponent component, Point oldLocation, Point newLocation) {
        this.canvas = canvas;
        this.component = component;
        this.oldLocation = new Point(oldLocation);
        this.newLocation = new Point(newLocation);
    }
    
    public JComponent getComponent() {
        return component;
    }
    
    public Point getOldLocation() {
        return new Point(oldLocation);
    }
    
    public Point getNewLocation() {
        return new Point(newLocation);
    }
    
    @Override
    public void undo() {
        component.setLocation(oldLocation);
        canvas.repaint();
    }
    
    @Override
    public void redo() {
        component.setLocation(newLocation);
        canvas.repaint();
    }
}
