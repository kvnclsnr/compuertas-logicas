package com.engine.circuit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.engine.LogicInput;
import com.engine.LogicSource;
import com.engine.gates.Gate;

// CONSTRUYE LA TABLA DE VERDAD DE UN GRUPO DE COMPUERTAS SIN DEJARLAS MODIFICADAS
public final class TruthTableBuilder {
    private static final int MAX_INPUTS = 10;
    private static final int BINARY_BASE = 2;
    private static final int HEADER_ROW_COUNT = 1;
    private static final char FIRST_INPUT_LETTER = 'A';
    private static final String TRUE_TEXT = "1";
    private static final String FALSE_TEXT = "0";
    private static final String TOO_MANY_INPUTS_MESSAGE = "Demasiadas entradas (máximo " + MAX_INPUTS + ")";
    
    private TruthTableBuilder() { }
    
    // CREA LA TABLA CON VARIABLES TEMPORALES EN LAS ENTRADAS LIBRES Y DEJA LAS COMPUERTAS COMO ESTABAN
    public static String[][] build(List<Gate> gates) {
        Map<Gate, LogicSource[]> originalInputs = new IdentityHashMap<>();
        
        try {
            for (Gate gate : gates) {
                connectFreeInputs(gate, originalInputs);
            }
            
            return createTable(gates);
        }
        
        finally {
            restoreInputs(originalInputs);
        }
    }
    
    // PONE UNA VARIABLE EN CADA ENTRADA LIBRE Y GUARDA LAS ENTRADAS ORIGINALES DE CADA COMPUERTA
    private static void connectFreeInputs(Gate gate, Map<Gate, LogicSource[]> originalInputs) {
        if (originalInputs.containsKey(gate)) return;
        
        LogicSource[] inputs = gate.getInputs();
        originalInputs.put(gate, inputs);
        
        for (int index = 0; index < inputs.length; index++) {
            if (inputs[index] == null) {
                gate.setInput(index, new LogicInput(createVariableName(gate, index)));
            }
            
            else if (inputs[index] instanceof Gate inputGate) {
                connectFreeInputs(inputGate, originalInputs);
            }
        }
    }
    
    // CREA EL NOMBRE DE UNA VARIABLE, POR EJEMPLO AND-0.A
    private static String createVariableName(Gate gate, int inputIndex) {
        char letter = (char) (FIRST_INPUT_LETTER + inputIndex);
        
        return gate.getName() + "." + letter;
    }
    
    // DEVUELVE A CADA COMPUERTA SUS ENTRADAS ORIGINALES
    private static void restoreInputs(Map<Gate, LogicSource[]> originalInputs) {
        for (Map.Entry<Gate, LogicSource[]> entry : originalInputs.entrySet()) {
            Gate gate = entry.getKey();
            LogicSource[] inputs = entry.getValue();
            
            for (int index = 0; index < inputs.length; index++) {
                gate.setInput(index, inputs[index]);
            }
        }
    }
    
    // REUNE LAS ENTRADAS Y EL ORDEN DE EVALUACION, Y AVISA SI HAY DEMASIADAS ENTRADAS
    private static String[][] createTable(List<Gate> gates) {
        Set<LogicInput> inputSet = new LinkedHashSet<>();
        List<Gate> evaluationOrder = new ArrayList<>();
        Set<Gate> visitedGates = new HashSet<>();
        
        for (Gate gate : gates) {
            gate.collectInputs(inputSet, evaluationOrder, visitedGates);
        }
        
        List<LogicInput> inputs = new ArrayList<>(inputSet);
        
        if (inputs.size() > MAX_INPUTS) {
            return new String[][] { { TOO_MANY_INPUTS_MESSAGE } };
        }
        
        return createRows(inputs, evaluationOrder);
    }
    
    // ARMA EL ENCABEZADO Y UNA FILA POR CADA COMBINACION DE ENTRADAS
    private static String[][] createRows(List<LogicInput> inputs, List<Gate> evaluationOrder) {
        int combinationCount = (int) Math.pow(BINARY_BASE, inputs.size());
        
        String[][] table = new String[HEADER_ROW_COUNT + combinationCount][];
        boolean[] originalValues = saveInputValues(inputs);
        
        table[0] = createHeaders(inputs, evaluationOrder);
        
        for (int combination = 0; combination < combinationCount; combination++) {
            table[HEADER_ROW_COUNT + combination] = createRow(combination, inputs, evaluationOrder);
        }
        
        restoreInputValues(inputs, originalValues);
        
        return table;
    }
    
    // CREA LOS ENCABEZADOS, PRIMERO LAS ENTRADAS Y DESPUES LA EXPRESION DE CADA COMPUERTA
    private static String[] createHeaders(List<LogicInput> inputs, List<Gate> evaluationOrder) {
        String[] headers = new String[inputs.size() + evaluationOrder.size()];
        
        for (int i = 0; i < inputs.size(); i++) {
            headers[i] = inputs.get(i).getName();
        }
        
        for (int i = 0; i < evaluationOrder.size(); i++) {
            headers[inputs.size() + i] = evaluationOrder.get(i).getExpression();
        }
        
        return headers;
    }
    
    // CREA UNA FILA CON EL VALOR DE CADA ENTRADA Y EL RESULTADO DE CADA COMPUERTA
    private static String[] createRow(int combination, List<LogicInput> inputs, List<Gate> evaluationOrder) {
        String[] row = new String[inputs.size() + evaluationOrder.size()];
        
        assignInputValues(combination, inputs, row);
        evaluateGates(inputs.size(), evaluationOrder, row);
        
        return row;
    }
    
    // ASIGNA A CADA ENTRADA UN DIGITO BINARIO DE LA COMBINACION, INVERTIDO PARA QUE LA TABLA EMPIECE EN 1
    private static void assignInputValues(int combination, List<LogicInput> inputs, String[] row) {
        int remaining = combination;
        
        for (int i = inputs.size() - 1; i >= 0; i--) {
            boolean value = remaining % BINARY_BASE == 0;
            remaining = remaining / BINARY_BASE;
            
            inputs.get(i).setValue(value);
            row[i] = toText(value);
        }
    }
    
    // EVALUA CADA COMPUERTA EN ORDEN Y GUARDA SU RESULTADO DESPUES DE LAS COLUMNAS DE ENTRADA
    private static void evaluateGates(int inputCount, List<Gate> evaluationOrder, String[] row) {
        for (int i = 0; i < evaluationOrder.size(); i++) {
            row[inputCount + i] = toText(evaluationOrder.get(i).getValue());
        }
    }
    
    // CONVIERTE UN VALOR LOGICO EN 1 O 0
    private static String toText(boolean value) {
        return value ? TRUE_TEXT : FALSE_TEXT;
    }
    
    // GUARDA LOS VALORES QUE TENIAN LAS ENTRADAS ANTES DE CREAR LA TABLA
    private static boolean[] saveInputValues(List<LogicInput> inputs) {
        boolean[] values = new boolean[inputs.size()];
        
        for (int i = 0; i < inputs.size(); i++) {
            values[i] = inputs.get(i).getValue();
        }
        
        return values;
    }
    
    // DEVUELVE A LAS ENTRADAS LOS VALORES QUE TENIAN
    private static void restoreInputValues(List<LogicInput> inputs, boolean[] values) {
        for (int i = 0; i < inputs.size(); i++) {
            inputs.get(i).setValue(values[i]);
        }
    }
}