package com.engine.circuit;

import java.util.List;

import com.engine.gates.Gate;

// IMPRIME EN LA CONSOLA LAS TABLAS DE VERDAD DE UN CIRCUITO
public final class TruthTablePrinter {
    private static final int HEADER_ROW = 0;
    private static final int FIRST_DATA_ROW = 1;
    private static final int CELL_MARGIN = 1;
    private static final String CORNER = "+";
    private static final String HORIZONTAL_LINE = "-";
    private static final String VERTICAL_LINE = "|";
    private static final String SPACE = " ";
    
    private TruthTablePrinter() { }
    
    // IMPRIME UNA TABLA POR CADA COMPUERTA DEL CIRCUITO
    public static void print(Circuit circuit) {
        printCircuitTitle(circuit);
        
        for (Gate gate : circuit.getGates()) {
            printTable(gate);
        }
    }
    
    // IMPRIME SOLO LA TABLA DE UNA COMPUERTA
    public static void print(Circuit circuit, Gate gate) {
        printCircuitTitle(circuit);
        printTable(gate);
    }
    
    // IMPRIME EL NOMBRE DEL CIRCUITO
    private static void printCircuitTitle(Circuit circuit) {
        System.out.println("< Circuito: " + circuit.getName() + " >");
        System.out.println();
    }
    
    // IMPRIME LA TABLA DE UNA COMPUERTA CON SUS BORDES
    private static void printTable(Gate gate) {
        String[][] table = TruthTableBuilder.build(List.of(gate));
        int[] columnWidths = getColumnWidths(table);
        
        System.out.println("# Tabla de Verdad: " + gate.getName());
        System.out.println();
        
        printBorder(columnWidths);
        printRow(table[HEADER_ROW], columnWidths);
        printBorder(columnWidths);
        
        for (int row = FIRST_DATA_ROW; row < table.length; row++) {
            printRow(table[row], columnWidths);
        }
        
        printBorder(columnWidths);
        
        System.out.println();
    }
    
    // CALCULA EL ANCHO DE CADA COLUMNA SEGUN SU TEXTO MAS LARGO
    private static int[] getColumnWidths(String[][] table) {
        int[] widths = new int[table[HEADER_ROW].length];
        
        for (String[] row : table) {
            for (int column = 0; column < row.length; column++) {
                widths[column] = Math.max(widths[column], row[column].length());
            }
        }
        
        return widths;
    }
    
    // IMPRIME UNA LINEA HORIZONTAL DEL ANCHO DE LA TABLA
    private static void printBorder(int[] columnWidths) {
        StringBuilder border = new StringBuilder();
        
        for (int width : columnWidths) {
            border.append(CORNER);
            border.append(HORIZONTAL_LINE.repeat(width + 2 * CELL_MARGIN));
        }
        
        border.append(CORNER);
        
        System.out.println(border);
    }
    
    // IMPRIME UNA FILA CON SUS VALORES CENTRADOS
    private static void printRow(String[] values, int[] columnWidths) {
        StringBuilder row = new StringBuilder();
        
        for (int column = 0; column < values.length; column++) {
            row.append(VERTICAL_LINE);
            row.append(centerText(values[column], columnWidths[column]));
        }
        
        row.append(VERTICAL_LINE);
        
        System.out.println(row);
    }
    
    // CENTRA UN TEXTO DENTRO DE UNA CELDA, CON UN MARGEN A CADA LADO
    private static String centerText(String text, int width) {
        int totalSpaces = width - text.length();
        int leftSpaces = totalSpaces / 2;
        int rightSpaces = totalSpaces - leftSpaces;
        
        return SPACE.repeat(CELL_MARGIN + leftSpaces)
            + text
            + SPACE.repeat(CELL_MARGIN + rightSpaces);
    }
}