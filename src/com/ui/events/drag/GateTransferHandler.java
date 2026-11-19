package com.ui.events.drag;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

import com.ui.icons.ElementType;

// ENVIA EL TIPO DE ELEMENTO COMO TEXTO AL ARRASTRARLO DESDE LA PALETA
public class GateTransferHandler extends TransferHandler {
    private final ElementType type;
    
    // CREA LA TRANSFERENCIA PARA UN TIPO DE ELEMENTO
    public GateTransferHandler(ElementType type) {
        this.type = type;
    }
    
    // CREA EL TEXTO QUE VIAJA CON EL ARRASTRE
    @Override
    protected Transferable createTransferable(JComponent component) {
        return new StringSelection(type.name());
    }
    
    // EL ARRASTRE SOLO COPIA, LA PALETA NUNCA SE VACIA
    @Override
    public int getSourceActions(JComponent component) {
        return COPY;
    }
}