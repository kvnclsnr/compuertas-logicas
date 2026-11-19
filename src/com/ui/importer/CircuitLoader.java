package com.ui.importer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ui.icons.CircuitElement;
import com.ui.importer.CircuitData.ElementData;
import com.ui.importer.CircuitData.WireData;
import com.ui.layouts.canvas.Canvas;

// RECREA EN EL LIENZO LOS ELEMENTOS Y LOS CABLES LEIDOS DEL ARCHIVO
public final class CircuitLoader {
    
    private CircuitLoader() { }
    
    // VACIA EL LIENZO Y LO LLENA CON LOS DATOS, PRIMERO LOS ELEMENTOS Y DESPUES LOS CABLES
    public static void load(CircuitData data, Canvas canvas) {
        canvas.clear();
        
        Map<Integer, CircuitElement> elementsById = createElements(data.elements(), canvas);
        
        connectWires(data.wires(), elementsById, canvas);
    }
    
    // CREA CADA ELEMENTO EN SU POSICION Y LOS GUARDA POR SU NUMERO PARA CONECTARLOS DESPUES
    private static Map<Integer, CircuitElement> createElements(List<ElementData> elements, Canvas canvas) {
        Map<Integer, CircuitElement> elementsById = new HashMap<>();
        
        for (ElementData data : elements) {
            CircuitElement element = canvas.addElementAt(data.type(), data.x(), data.y());
            
            if (data.value()) {
                element.click();
            }
            
            elementsById.put(data.id(), element);
        }
        
        return elementsById;
    }
    
    // CONECTA LOS ELEMENTOS SEGUN LOS CABLES DEL ARCHIVO
    private static void connectWires(List<WireData> wires, Map<Integer, CircuitElement> elementsById, Canvas canvas) {
        for (WireData wire : wires) {
            CircuitElement source = elementsById.get(wire.source());
            CircuitElement target = elementsById.get(wire.target());
            
            canvas.connectElements(source, target, wire.input());
        }
    }
}