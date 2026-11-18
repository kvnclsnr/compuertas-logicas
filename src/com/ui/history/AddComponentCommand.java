package com.ui.history;

import com.ui.layouts.canvas.Canvas;
import javax.swing.JComponent;

public class AddComponentCommand implements CanvasCommand {
    private final Canvas canvas;
    private final JComponent component;
    
    public AddComponentCommand(Canvas canvas, JComponent component) {
        this.canvas = canvas;
        this.component = component;
    }
    
    public JComponent getComponent() {
        return component;
    }
    
    @Override
    public void undo() {
        canvas.removeComponentDirect(component);
    }
    
    @Override
    public void redo() {
        canvas.restoreComponentDirect(component);
    }
}
