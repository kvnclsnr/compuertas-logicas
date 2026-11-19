package com.ui.events.drag;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.TransferHandler;

import com.ui.icons.ElementType;
import com.ui.layouts.canvas.Canvas;

// RECIBE EL TIPO DE ELEMENTO ARRASTRADO DESDE LA PALETA Y LO AGREGA AL LIENZO
public class GateDropHandler extends TransferHandler {
    private final Canvas canvas;
    
    // CREA EL RECEPTOR PARA UN LIENZO
    public GateDropHandler(Canvas canvas) {
        this.canvas = canvas;
    }
    
    // SOLO ACEPTA SOLTAR TEXTO
    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDrop() && support.isDataFlavorSupported(DataFlavor.stringFlavor);
    }
    
    // AGREGA EL ELEMENTO EN EL PUNTO DONDE SE SOLTO, SI EL TEXTO NO ES UN TIPO CONOCIDO LO RECHAZA
    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) return false;
        
        try {
            String text = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
            ElementType type = ElementType.valueOf(text);
            Point dropPoint = support.getDropLocation().getDropPoint();
            
            canvas.addElement(type, dropPoint);
            
            return true;
        }
        
        catch (UnsupportedFlavorException | IOException | IllegalArgumentException exception) {
            return false;
        }
    }
}