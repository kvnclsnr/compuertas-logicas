package com.engine.circuit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.LinkedHashSet;
import java.util.IdentityHashMap;
import com.engine.LogicInput;
import com.engine.LogicSource;
import com.engine.gates.Gate;

public class Circuit {
    private static final int MAX_INPUTS = 10;
    
    private final String name;
    private final List<Gate> gates;
    
    public Circuit(String name) {
        this.name = name;
        this.gates = new ArrayList<>();
    }
    
    public void addGate(Gate gate) {
        gates.add(gate);
    }
    
    public boolean removeGateByName(String name) {
        for (int i = 0; i < gates.size(); i++) {
            if (gates.get(i).getName().equals(name)) {
                gates.remove(i);
                return true;
            }
        }
        
        return false;
    }
    
    public String getName() {
        return name;
    }
    
    public List<Gate> getGates() {
        return new ArrayList<>(gates);
    }
    
    // TRUTH TABLE - GENERAL
    
    public String[][] getTruthTable() {
        return buildTruthTable(gates);
    }
    
    public String[][] getTruthTableOf(Gate outputGate) {
        if (outputGate == null) return new String[0][0];
        
        return buildTruthTable(List.of(outputGate));
    }
    
    private String[][] buildTruthTable(List<Gate> tableGates) {
        Map<Gate, LogicSource[]> originals = new IdentityHashMap<>();
        
        try {
            for (Gate gate : tableGates) {
                fillUnconnectedInputs(gate, originals);
            }
            
            return createTruthTable(tableGates);
        }
        
        finally {
            restoreInputs(originals);
        }
    }
    
    private void fillUnconnectedInputs(Gate gate, Map<Gate, LogicSource[]> originals) {
        if (originals.containsKey(gate)) return;
        
        LogicSource[] inputs = gate.getInputs();
        originals.put(gate, inputs);
        
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] == null) {
                String name = gate.getName() + "." + (char) ('A' + i);
                gate.setInput(i, new LogicInput(name));
            }
            
            else if (inputs[i] instanceof Gate child) {
                fillUnconnectedInputs(child, originals);
            }
        }
    }
    
    private void restoreInputs(Map<Gate, LogicSource[]> originals) {
        originals.forEach((gate, inputs) -> {
            for (int i = 0; i < inputs.length; i++) {
                gate.setInput(i, inputs[i]);
            }
        });
    }
    
    private String[][] createTruthTable(List<Gate> tableGates) {
        Set<LogicInput> inputSet = new LinkedHashSet<>();
        List<Gate> evaluationOrder = new ArrayList<>();
        Set<Gate> visitedGates = new HashSet<>();
        
        for (Gate gate : tableGates) {
            gate.collectInputs(inputSet, evaluationOrder, visitedGates);
        }
        
        List<LogicInput> inputs = new ArrayList<>(inputSet);
        
        if (inputs.size() > MAX_INPUTS) {
            return new String[][] { { "Demasiadas entradas (máximo " + MAX_INPUTS + ")" } };
        }
        
        int rowCount = (int) Math.pow(2, inputs.size());
        
        String[][] table = new String[rowCount + 1][];
        boolean[] originalInputValues = saveInputValues(inputs);
        
        table[0] = createColumnHeaders(inputs, evaluationOrder);
        
        for (int row = 0; row < rowCount; row++) {
            table[row + 1] = createRowValues(row, inputs, evaluationOrder);
        }
        
        restoreInputValues(inputs, originalInputValues);
        
        return table;
    }
    
    private String[] createColumnHeaders(List<LogicInput> inputs, List<Gate> evaluationOrder) {
        String[] headers = new String[inputs.size() + evaluationOrder.size()];
        
        for (int i = 0; i < inputs.size(); i++) {
            headers[i] = inputs.get(i).getName();
        }
        
        for (int i = 0; i < evaluationOrder.size(); i++) {
            headers[inputs.size() + i] = evaluationOrder.get(i).getExpression();
        }
        
        return headers;
    }
    
    private String[] createRowValues(int row, List<LogicInput> inputs, List<Gate> evaluationOrder) {
        String[] rowValues = new String[inputs.size() + evaluationOrder.size()];
        
        setInputValues(row, inputs, rowValues);
        setGateValues(inputs.size(), evaluationOrder, rowValues);
        
        return rowValues;
    }
    
    private void setGateValues(int inputCount, List<Gate> evaluationOrder, String[] rowValues) {
        for (int i = 0; i < evaluationOrder.size(); i++) {
            int columnIndex = inputCount + i;
            
            rowValues[columnIndex] = evaluationOrder.get(i).evaluate() ? "1" : "0";
        }
    }
    
    private void setInputValues(int row, List<LogicInput> inputs, String[] rowValues) {
        int remaining = row;
        
        for (int i = inputs.size() - 1; i >= 0; i--) {
            boolean inputValue = remaining % 2 == 0;
            remaining = remaining / 2;
            
            inputs.get(i).setValue(inputValue);
            rowValues[i] = inputValue ? "1" : "0";
        }
    }
    
    private boolean[] saveInputValues(List<LogicInput> inputs) {
        boolean[] originalValues = new boolean[inputs.size()];
        
        for (int i = 0; i < inputs.size(); i++) {
            originalValues[i] = inputs.get(i).getValue();
        }
        
        return originalValues;
    }
    
    private void restoreInputValues(List<LogicInput> inputs, boolean[] originalValues) {
        for (int i = 0; i < inputs.size(); i++) {
            inputs.get(i).setValue(originalValues[i]);
        }
    }
}