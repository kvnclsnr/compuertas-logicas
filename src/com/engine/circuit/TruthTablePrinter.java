package com.engine.circuit;

import com.engine.gates.Gate;

public class TruthTablePrinter {
    
    public static void show(Circuit circuit) {
        System.out.println("< Circuito: " + circuit.getName() + " >\n");
        
        for (Gate gate : circuit.getGates()) {
            printTable(circuit, gate);
        }
    }
    
    public static void show(Circuit circuit, Gate outputGate) {
        System.out.println("< Circuito: " + circuit.getName() + " >\n");
        
        printTable(circuit, outputGate);
    }
    
    private static void printTable(Circuit circuit, Gate outputGate) {
        String[][] table = circuit.getTruthTableOf(outputGate);
        
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
    
    private static int[] getColumnWidths(String[][] table) {
        int[] columnWidths = new int[table[0].length];
        
        for (String[] row : table) {
            for (int i = 0; i < row.length; i++) {
                columnWidths[i] = Math.max(columnWidths[i], row[i].length());
            }
        }
        
        return columnWidths;
    }
    
    private static void printTableBorder(int[] columnWidths) {
        StringBuilder border = new StringBuilder();
        
        for (int width : columnWidths) {
            border.append('+');
            border.append("-".repeat(width + 2));
        }
        
        border.append('+');
        
        System.out.println(border);
    }
    
    private static void printTableRow(String[] values, int[] columnWidths) {
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