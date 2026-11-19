package com.ui.export;

import java.util.List;

import com.ui.circuit.wire.Wire;
import com.ui.icons.CircuitElement;
import com.ui.icons.ElementType;

// CONVIERTE LOS ELEMENTOS Y LOS CABLES DEL LIENZO EN EL TEXTO QUE SE GUARDA EN EL ARCHIVO
public final class CircuitTextWriter {
    private static final String LINE_BREAK = "\n";
    
    private CircuitTextWriter() { }
    
    // CREA EL TEXTO COMPLETO, PRIMERO EL ENCABEZADO, LUEGO LOS ELEMENTOS Y AL FINAL LOS CABLES
    public static String write(List<CircuitElement> elements, List<Wire> wires) {
        StringBuilder text = new StringBuilder();
        
        writeHeader(text, elements.size(), wires.size());
        
        for (int id = 0; id < elements.size(); id++) {
            writeElement(text, id, elements.get(id));
        }
        
        for (Wire wire : wires) {
            writeWire(text, wire, elements);
        }
        
        return text.toString();
    }
    
    // ESCRIBE CUANTOS ELEMENTOS Y CUANTOS CABLES TRAE EL ARCHIVO
    private static void writeHeader(StringBuilder text, int elementCount, int wireCount) {
        text.append(CircuitFormat.CANVAS_SECTION).append(LINE_BREAK);
        writeData(text, CircuitFormat.ELEMENT_COUNT_KEY, elementCount);
        writeData(text, CircuitFormat.WIRE_COUNT_KEY, wireCount);
    }
    
    // ESCRIBE UN ELEMENTO CON SU NUMERO, SU TIPO Y SU POSICION, LOS INTERRUPTORES TAMBIEN SU VALOR
    private static void writeElement(StringBuilder text, int id, CircuitElement element) {
        writeSection(text, CircuitFormat.ELEMENT_SECTION);
        writeData(text, CircuitFormat.ID_KEY, id);
        writeData(text, CircuitFormat.TYPE_KEY, element.getType().name());
        writeData(text, CircuitFormat.X_KEY, element.getX());
        writeData(text, CircuitFormat.Y_KEY, element.getY());
        
        if (element.getType() == ElementType.INPUT) {
            writeData(text, CircuitFormat.VALUE_KEY, toText(element.getOutputValue()));
        }
    }
    
    // ESCRIBE UN CABLE CON LOS NUMEROS DE SUS DOS ELEMENTOS Y LA ENTRADA DONDE TERMINA
    private static void writeWire(StringBuilder text, Wire wire, List<CircuitElement> elements) {
        writeSection(text, CircuitFormat.WIRE_SECTION);
        writeData(text, CircuitFormat.SOURCE_KEY, elements.indexOf(wire.getSource().getOwner()));
        writeData(text, CircuitFormat.TARGET_KEY, elements.indexOf(wire.getTarget().getOwner()));
        writeData(text, CircuitFormat.INPUT_KEY, wire.getTarget().getIndex());
    }
    
    // ESCRIBE UNA LINEA EN BLANCO Y EL ENCABEZADO QUE ABRE UN BLOQUE NUEVO
    private static void writeSection(StringBuilder text, String section) {
        text.append(LINE_BREAK);
        text.append(section).append(LINE_BREAK);
    }
    
    // ESCRIBE UN DATO EN UNA LINEA CON LA FORMA CLAVE=VALOR
    private static void writeData(StringBuilder text, String key, Object value) {
        text.append(key);
        text.append(CircuitFormat.KEY_VALUE_SEPARATOR);
        text.append(value);
        text.append(LINE_BREAK);
    }
    
    // CONVIERTE UN VALOR LOGICO EN 1 O 0
    private static String toText(boolean value) {
        return value ? CircuitFormat.TRUE_TEXT : CircuitFormat.FALSE_TEXT;
    }
}