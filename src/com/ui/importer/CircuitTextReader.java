package com.ui.importer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ui.export.CircuitFormat;
import com.ui.icons.ElementType;
import com.ui.importer.CircuitData.ElementData;
import com.ui.importer.CircuitData.WireData;

// CONVIERTE LAS LINEAS DEL ARCHIVO DE TEXTO EN DATOS DEL LIENZO Y COMPRUEBA QUE SEAN VALIDOS
public final class CircuitTextReader {
    private static final String SECTION_START = "[";
    private static final String SECTION_END = "]";
    private static final String MISSING_HEADER_MESSAGE = "falta el encabezado " + CircuitFormat.CANVAS_SECTION;
    private static final String INVALID_LINE_MESSAGE = "linea no valida: ";
    private static final String UNKNOWN_SECTION_MESSAGE = "seccion desconocida: ";
    private static final String MISSING_KEY_MESSAGE = "falta el dato ";
    private static final String COUNT_MESSAGE = "las cantidades no coinciden con el encabezado";
    private static final String DUPLICATE_ID_MESSAGE = "hay elementos con el mismo ID";
    private static final String INVALID_WIRE_MESSAGE = "hay un cable con elementos o entradas que no existen";
    
    // UN BLOQUE DEL ARCHIVO, SU ENCABEZADO Y SUS DATOS CLAVE=VALOR
    private record Block(String section, Map<String, String> values) { }
    
    private CircuitTextReader() { }
    
    // LEE TODO EL TEXTO, SI ALGO ESTA MAL LANZA IllegalArgumentException CON EL MOTIVO
    public static CircuitData read(List<String> lines) {
        List<Block> blocks = splitIntoBlocks(lines);
        
        if (blocks.isEmpty() || !blocks.get(0).section().equals(CircuitFormat.CANVAS_SECTION)) {
            throw new IllegalArgumentException(MISSING_HEADER_MESSAGE);
        }
        
        List<ElementData> elements = new ArrayList<>();
        List<WireData> wires = new ArrayList<>();
        
        for (Block block : blocks.subList(1, blocks.size())) {
            addBlock(block, elements, wires);
        }
        
        checkCounts(blocks.get(0), elements, wires);
        checkWires(elements, wires);
        
        return new CircuitData(elements, wires);
    }
    
    // AGRUPA LAS LINEAS EN BLOQUES, CADA ENCABEZADO ABRE UN BLOQUE NUEVO
    private static List<Block> splitIntoBlocks(List<String> lines) {
        List<Block> blocks = new ArrayList<>();
        
        for (String line : lines) {
            String text = line.trim();
            
            if (text.isEmpty()) continue;
            
            if (isSection(text)) {
                blocks.add(new Block(text, new HashMap<>()));
            }
            
            else {
                addData(blocks, text);
            }
        }
        
        return blocks;
    }
    
    // DICE SI LA LINEA ES UN ENCABEZADO DE BLOQUE, POR EJEMPLO [ELEMENTO]
    private static boolean isSection(String text) {
        return text.startsWith(SECTION_START) && text.endsWith(SECTION_END);
    }
    
    // GUARDA UNA LINEA CLAVE=VALOR EN EL ULTIMO BLOQUE
    private static void addData(List<Block> blocks, String text) {
        int separatorIndex = text.indexOf(CircuitFormat.KEY_VALUE_SEPARATOR);
        
        if (blocks.isEmpty() || separatorIndex < 0) {
            throw new IllegalArgumentException(INVALID_LINE_MESSAGE + text);
        }
        
        String key = text.substring(0, separatorIndex).trim();
        String value = text.substring(separatorIndex + CircuitFormat.KEY_VALUE_SEPARATOR.length()).trim();
        
        blocks.get(blocks.size() - 1).values().put(key, value);
    }
    
    // CONVIERTE UN BLOQUE EN UN ELEMENTO O EN UN CABLE SEGUN SU ENCABEZADO
    private static void addBlock(Block block, List<ElementData> elements, List<WireData> wires) {
        if (block.section().equals(CircuitFormat.ELEMENT_SECTION)) {
            elements.add(readElement(block.values()));
        }
        
        else if (block.section().equals(CircuitFormat.WIRE_SECTION)) {
            wires.add(readWire(block.values()));
        }
        
        else {
            throw new IllegalArgumentException(UNKNOWN_SECTION_MESSAGE + block.section());
        }
    }
    
    // LEE LOS DATOS DE UN ELEMENTO, EL VALOR ES OPCIONAL Y SOLO LO TIENEN LOS INTERRUPTORES
    private static ElementData readElement(Map<String, String> values) {
        int id = readInt(values, CircuitFormat.ID_KEY);
        ElementType type = ElementType.valueOf(readText(values, CircuitFormat.TYPE_KEY));
        int x = readInt(values, CircuitFormat.X_KEY);
        int y = readInt(values, CircuitFormat.Y_KEY);
        boolean value = CircuitFormat.TRUE_TEXT.equals(values.get(CircuitFormat.VALUE_KEY));
        
        return new ElementData(id, type, x, y, value);
    }
    
    // LEE LOS DATOS DE UN CABLE
    private static WireData readWire(Map<String, String> values) {
        int source = readInt(values, CircuitFormat.SOURCE_KEY);
        int target = readInt(values, CircuitFormat.TARGET_KEY);
        int input = readInt(values, CircuitFormat.INPUT_KEY);
        
        return new WireData(source, target, input);
    }
    
    // COMPRUEBA QUE EL ARCHIVO TRAIGA LA CANTIDAD DE ELEMENTOS Y CABLES QUE DICE EL ENCABEZADO
    private static void checkCounts(Block header, List<ElementData> elements, List<WireData> wires) {
        int elementCount = readInt(header.values(), CircuitFormat.ELEMENT_COUNT_KEY);
        int wireCount = readInt(header.values(), CircuitFormat.WIRE_COUNT_KEY);
        
        if (elementCount != elements.size() || wireCount != wires.size()) {
            throw new IllegalArgumentException(COUNT_MESSAGE);
        }
    }
    
    // COMPRUEBA QUE CADA CABLE VAYA DE UN ELEMENTO A UNA ENTRADA QUE EXISTE
    private static void checkWires(List<ElementData> elements, List<WireData> wires) {
        Map<Integer, ElementData> elementsById = mapById(elements);
        
        for (WireData wire : wires) {
            ElementData target = elementsById.get(wire.target());
            
            if (!elementsById.containsKey(wire.source()) || target == null) {
                throw new IllegalArgumentException(INVALID_WIRE_MESSAGE);
            }
            
            if (wire.source() == wire.target() || wire.input() < 0 || wire.input() >= target.type().getInputCount()) {
                throw new IllegalArgumentException(INVALID_WIRE_MESSAGE);
            }
        }
    }
    
    // ORGANIZA LOS ELEMENTOS POR SU NUMERO, NO PUEDE HABER DOS IGUALES
    private static Map<Integer, ElementData> mapById(List<ElementData> elements) {
        Map<Integer, ElementData> elementsById = new HashMap<>();
        
        for (ElementData element : elements) {
            if (elementsById.put(element.id(), element) != null) {
                throw new IllegalArgumentException(DUPLICATE_ID_MESSAGE);
            }
        }
        
        return elementsById;
    }
    
    // DEVUELVE UN DATO DEL BLOQUE COMO NUMERO
    private static int readInt(Map<String, String> values, String key) {
        return Integer.parseInt(readText(values, key));
    }
    
    // DEVUELVE UN DATO DEL BLOQUE, SI NO ESTA LANZA UN ERROR
    private static String readText(Map<String, String> values, String key) {
        String value = values.get(key);
        
        if (value == null) {
            throw new IllegalArgumentException(MISSING_KEY_MESSAGE + key);
        }
        
        return value;
    }
}