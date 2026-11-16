package com.ui.events.drag;

import com.ui.layouts.canvas.Canvas;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.TransferHandler;

// Recibe el nombre de la compuerta arrastrada desde la paleta y la agrega al canvas
public class GateDropHandler extends TransferHandler {
    private final Canvas canvas;
    
    public GateDropHandler(Canvas canvas) {
        this.canvas = canvas;
    }
    
    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDrop() && support.isDataFlavorSupported(DataFlavor.stringFlavor);
    }
    
    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }
        
        try {
            String name = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
            Point dropPoint = support.getDropLocation().getDropPoint();
            
            return canvas.addGate(name, dropPoint);
        } catch (UnsupportedFlavorException | IOException exception) {
            System.err.println("Could not add the dragged gate: " + exception.getMessage());
            return false;
        }
    }
}