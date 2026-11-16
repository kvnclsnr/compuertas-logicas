package com.ui.events.drag;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

// ENVÍA EL NOMBRE DE LA COMPUERTA COMO TEXTO AL ARRASTRARLA DESDE LA PALETA
public class GateTransferHandler extends TransferHandler {
    private final String gateName;
    
    public GateTransferHandler(String gateName) {
        this.gateName = gateName;
    }
    
    @Override
    protected Transferable createTransferable(JComponent component) {
        return new StringSelection(gateName);
    }
    
    @Override
    public int getSourceActions(JComponent component) {
        return COPY;
    }
}