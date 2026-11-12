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
    
    // GENERAL METHODS
    
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
    
    // TRUTH TABLE - GENERAL
    
    /* 
     *  Example: A AND B
     *  
     *  {
     *    { "A", "B", "A AND B" },
     *    { "0", "0",    "0"    },
     *    { "0", "1",    "0"    },
     *    { "1", "0",    "0"    },
     *    { "1", "1",    "1"    }
     *  }
     */
    
    public String[][] getTruthTable(Gate outputGate) {
        if (outputGate == null) return new String[0][0];
        
        List<LogicInput> inputs = new ArrayList<>();
        List<Gate> evaluationOrder = new ArrayList<>();
        
        collectGatesInEvaluationOrder(
            evaluationOrder, outputGate, new HashSet<>(), inputs, new HashSet<>()
        );
        
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
    
    private void collectGatesInEvaluationOrder(List<Gate> evaluationOrder, Gate gate, Set<Gate> visitedGates, List<LogicInput> inputs, Set<LogicInput> visitedInputs) {
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
    
    private String[] createColumnHeaders(List<LogicInput> inputs, List<Gate> evaluationOrder) {
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
    
    private String[] createRowValues(int row, List<LogicInput> inputs, List<Gate> evaluationOrder) {
        String[] rowValues = new String[inputs.size() + evaluationOrder.size()];
        
        setInputValues(row, inputs, rowValues);
        setGateValues(inputs.size(), evaluationOrder, rowValues);
        
        return rowValues;
    }
    
    /*  
     *  EXAMPLE: 3 INPUTS (A, B, C):
     *  
     *     ROW | A  B  C
     *    -----+---------
     *      0  | 0  0  0
     *      1  | 0  0  1
     *      2  | 0  1  0
     *      3  | 0  1  1
     *      4  | 1  0  0
     *      5  | 1  0  1  <-  [5] EXAMPLE ROW
     *      6  | 1  1  0
     *      7  | 1  1  1
     *    
     *    'A' CHANGES EVERY 4 ROWS
     *    'B' CHANGES EVERY 2 ROWS
     *    'C' CHANGES EVERY 1 ROW
     *  
     *  ROW 5:
     *    A -> groupSize = 4 -> (5 / 4) % 2 = 1 % 2 = 1
     *    B -> groupSize = 2 -> (5 / 2) % 2 = 2 % 2 = 0
     *    C -> groupSize = 1 -> (5 / 1) % 2 = 5 % 2 = 1
     *  
     *  RESULT: A = 1, B = 0, C = 1
     */ 
    private void setInputValues(int row, List<LogicInput> inputs, String[] rowValues) {
        int inputCount = inputs.size();
        
        for (int i = 0; i < inputCount; i++) {
            int exponent = inputCount - i - 1;
            int groupSize = (int) Math.pow(2, exponent);
            
            boolean inputValue = (row / groupSize) % 2 == 1;
            
            inputs.get(i).setValue(inputValue);
            rowValues[i] = inputValue ? "1" : "0";
        }
    }
    
    private void setGateValues(int inputCount, List<Gate> evaluationOrder, String[] rowValues) {
        for (int i = 0; i < evaluationOrder.size(); i++) {
            int columnIndex = inputCount + i;
            
            rowValues[columnIndex] = evaluationOrder.get(i).evaluate() ? "1" : "0";
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
    
    // TRUTH TABLE - CONSOLE
    
    public void showTruthTable() {
        System.out.println("< Circuito: " + this.name + " >\n");
        
        for (Gate gate : gates) {
            printTable(gate);
        }
    }
    
    public void showTruthTable(Gate outputGate) {
        System.out.println("< Circuito: " + this.name + " >\n");
        
        printTable(outputGate);
    }
    
    private void printTable(Gate outputGate) {
        String[][] table = getTruthTable(outputGate);
        
        if (table.length == 0) return;
        
        int[] columnWidths = getColumnWidths(table);
        
        System.out.println("# Tabla de Verdad: " + outputGate.getName() + "\n");
        
        printTableBorder(columnWidths);
        printTableRow(table[0], columnWidths);
        printTableBorder(columnWidths);
        
        for (int row = 1; row < table.length; row++) {
            printTableRow(table[row], columnWidths);
        }
        
        printTableBorder(columnWidths);
        
        System.out.println();
    }
    
    private int[] getColumnWidths(String[][] table) {
        int[] columnWidths = new int[table[0].length];
        
        for (String[] row : table) {
            for (int i = 0; i < row.length; i++) {
                columnWidths[i] = Math.max(columnWidths[i], row[i].length());
            }
        }
        
        return columnWidths;
    }
    
    private void printTableBorder(int[] columnWidths) {
        StringBuilder border = new StringBuilder();
        
        for (int width : columnWidths) {
            border.append('+');
            border.append("-".repeat(width + 2));
        }
        
        border.append('+');
        
        System.out.println(border);
    }
    
    private void printTableRow(String[] values, int[] columnWidths) {
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