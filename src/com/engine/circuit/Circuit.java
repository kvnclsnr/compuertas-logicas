package com.engine.circuit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.engine.LogicInput;
import com.engine.LogicSource;
import com.engine.gates.Gate;

public class Circuit {
    private String name;
    private ArrayList<Gate> gates;
    
    public Circuit(String name) {
        this.name = name;
        this.gates = new ArrayList<>();
    }
    
    public void addGate(Gate gate) {
        gates.add(gate);
    }
    
    public void evaluateAll() {
        for (Gate gate : gates) {
            System.out.println(gate.getName() + " -> " + gate.evaluate());
        }
    }
    
    public int getGateCount() {
        return gates.size();
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
    
    public Gate findGateByName(String name) {
        for (Gate gate : gates) {
            if (gate.getName().equals(name)) {
                return gate;
            }
        }
        return null;
    }
    
    public String getName() {
        return name;
    }
    
    public void printTruthTable() {
        System.out.println("< Circuito: " + this.name + " >\n");
        
        for (Gate gate : gates) {
            TruthTable(gate);
        }
    }
    
    public void printTruthTable(Gate outputGate) {
        System.out.println("< Circuito: " + this.name + " >\n");
        
        TruthTable(outputGate);
    }
    
    private void TruthTable(Gate outputGate) {
        if (outputGate == null) return;
        
        List<LogicInput> inputs = new ArrayList<>();
        List<Gate> evaluationOrder = new ArrayList<>();
        
        collectGatesInEvaluationOrder(
            evaluationOrder, outputGate, new HashSet<>(), inputs, new HashSet<>()
        );
        
        int inputCount = inputs.size();
        int rowCount = (int) Math.pow(2, inputCount);
        
        String[] columnHeaders = createColumnHeaders(inputs, evaluationOrder);
        int[] columnWidths = getColumnWidths(columnHeaders);
        boolean[] originalInputValues = saveInputValues(inputs);
        
        System.out.println("# Tabla de Verdad: " + outputGate.getName() + "\n");
        
        printTableBorder(columnWidths);
        printTableRow(columnHeaders, columnWidths);
        printTableBorder(columnWidths);
        
        try {
            for (int row = 0; row < rowCount; row++) {
                String[] rowValues = createRowValues(
                    row, inputs, evaluationOrder
                );
                
                printTableRow(rowValues, columnWidths);
            }
        }
        
        finally {
            restoreInputValues(inputs, originalInputValues);
        }
        
        printTableBorder(columnWidths);
        
        System.out.println();
    }
    
    // PRIVATE METHODS (PRINT TRUTH TABLE)
    
    private String[] createColumnHeaders(List<LogicInput> inputs, List<Gate> evaluationOrder) { // OK
        
        String[] headers = new String[inputs.size() + evaluationOrder.size()];
        
        int columnIndex = 0;
        
        for (LogicInput input : inputs) {
            headers[columnIndex] = input.getName();
            columnIndex++;
        }
        
        for (Gate gate : evaluationOrder) {
            headers[columnIndex] = gate.getExpression();
            columnIndex++;
        }
        
        return headers;
    }
    
    private int[] getColumnWidths(String[] columnHeaders) { // OK
        int[] columnWidths = new int[columnHeaders.length];
        
        for (int i = 0; i < columnHeaders.length; i++) {
            columnWidths[i] = columnHeaders[i].length();
        }
        
        return columnWidths;
    }
    
    private boolean[] saveInputValues(List<LogicInput> inputs) { // OK
        boolean[] originalValues = new boolean[inputs.size()];
        
        for (int i = 0; i < inputs.size(); i++) {
            originalValues[i] = inputs.get(i).getValue();
        }
        
        return originalValues;
    }
    
    private void restoreInputValues(List<LogicInput> inputs, boolean[] originalValues) { // OK
        for (int i = 0; i < inputs.size(); i++) {
            inputs.get(i).setValue(originalValues[i]);
        }
    }
    
    private String[] createRowValues(int row, List<LogicInput> inputs, List<Gate> evaluationOrder) {
        String[] rowValues = new String[inputs.size() + evaluationOrder.size()];
        
        setInputValues(row, inputs, rowValues);
        setGateValues(inputs.size(), evaluationOrder, rowValues);
        
        return rowValues;
    }
    
    private void setInputValues(int row, List<LogicInput> inputs, String[] rowValues) {
        int inputCount = inputs.size();
        
        for (int i = 0; i < inputCount; i++) {
            int bitPosition = inputCount - i - 1;
            boolean inputValue = ((row >> bitPosition) & 1) == 1;
            
            inputs.get(i).setValue(inputValue);
            rowValues[i] = inputValue ? "1" : "0";
        }
    }
    
    private void setGateValues(int inputCount, List<Gate> evaluationOrder, String[] rowValues) { // OK
        for (int i = 0; i < evaluationOrder.size(); i++) {
            int columnIndex = inputCount + i;
            
            rowValues[columnIndex] = evaluationOrder.get(i).evaluate() ? "1" : "0";
        }
    }
    
    private void collectGatesInEvaluationOrder(List<Gate> evaluationOrder, Gate gate, Set<Gate> visitedGates, List<LogicInput> inputs, Set<LogicInput> visitedInputs) { // OK
        if (visitedGates.contains(gate)) return;
        
        visitedGates.add(gate);
        
        for (LogicSource input : gate.getInputs()) {
            
            if (input instanceof Gate) {
                Gate inputGate = (Gate) input;
                
                collectGatesInEvaluationOrder(
                    evaluationOrder, inputGate, visitedGates, inputs, visitedInputs
                );
            }
            
            else if (input instanceof LogicInput) {
                LogicInput logicInput = (LogicInput) input;
                
                if (!visitedInputs.contains(logicInput)) {
                    visitedInputs.add(logicInput);
                    inputs.add(logicInput);
                }
            }
        }
        
        evaluationOrder.add(gate);
    }
    
    private void printTableBorder(int[] columnWidths) { // OK
        StringBuilder border = new StringBuilder();
        
        for (int width : columnWidths) {
            border.append('+');
            border.append("-".repeat(width + 2));
        }
        
        border.append('+');
        
        System.out.println(border);
    }
    
    private void printTableRow(String[] values, int[] columnWidths) { // OK
        StringBuilder row = new StringBuilder();
        
        for (int i = 0; i < values.length; i++) {
            int remainingPadding = columnWidths[i] - values[i].length();
            int leftPadding = remainingPadding / 2;
            int rightPadding = remainingPadding - leftPadding;
            
            row.append("| ")
                    .append(" ".repeat(leftPadding))
                    .append(values[i])
                    .append(" ".repeat(rightPadding))
                    .append(" ");
        }
        
        row.append('|');
        
        System.out.println(row);
    }
}