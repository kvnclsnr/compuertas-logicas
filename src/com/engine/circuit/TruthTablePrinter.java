package com.engine.circuit;

import java.util.List;

import com.engine.gates.Gate;

// ARMA Y MUESTRA EN LA CONSOLA LAS TABLAS DE VERDAD DE UN CIRCUITO
public final class TruthTablePrinter {
    private static final int HEADER_ROW = 0;
    private static final int FIRST_DATA_ROW = 1;
    private static final int CELL_MARGIN = 1;
    private static final String CORNER = "+";
    private static final String HORIZONTAL_LINE = "-";
    private static final String VERTICAL_LINE = "|";
    private static final String SPACE = " ";
    private static final String LINE_BREAK = "\n";
    
    private TruthTablePrinter() { }
    
    // IMPRIME UNA TABLA POR CADA COMPUERTA DEL CIRCUITO
    public static void print(Circuit circuit) {
        System.out.print(format(circuit));
    }
    
    // IMPRIME SOLO LA TABLA DE UNA COMPUERTA
    public static void print(Circuit circuit, Gate gate) {
        System.out.print(formatTitle(circuit) + formatTable(gate));
    }
    
    // ARMA EL TEXTO CON EL TITULO DEL CIRCUITO Y LA TABLA DE CADA COMPUERTA, ESTE TEXTO ES EL QUE SE IMPRIME O SE EXPORTA
    public static String format(Circuit circuit) {
        StringBuilder text = new StringBuilder(formatTitle(circuit));
        
        for (Gate gate : circuit.getGates()) {
            text.append(formatTable(gate));
        }
        
        return text.toString();
    }
    
    // ARMA EL TITULO DEL CIRCUITO
    private static String formatTitle(Circuit circuit) {
        return "< Circuito: " + circuit.getName() + " >" + LINE_BREAK + LINE_BREAK;
    }
    
    // ARMA LA TABLA DE UNA COMPUERTA CON SUS BORDES
    private static String formatTable(Gate gate) {
        String[][] table = TruthTableBuilder.build(List.of(gate));
        int[] columnWidths = getColumnWidths(table);
        StringBuilder text = new StringBuilder();
        
        text.append("# Tabla de Verdad: ").append(gate.getName()).append(LINE_BREAK).append(LINE_BREAK);
        
        appendBorder(text, columnWidths);
        appendRow(text, table[HEADER_ROW], columnWidths);
        appendBorder(text, columnWidths);
        
        for (int row = FIRST_DATA_ROW; row < table.length; row++) {
            appendRow(text, table[row], columnWidths);
        }
        
        appendBorder(text, columnWidths);
        text.append(LINE_BREAK);
        
        return text.toString();
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
    
    // AGREGA UNA LINEA HORIZONTAL DEL ANCHO DE LA TABLA
    private static void appendBorder(StringBuilder text, int[] columnWidths) {
        for (int width : columnWidths) {
            text.append(CORNER);
            text.append(HORIZONTAL_LINE.repeat(width + 2 * CELL_MARGIN));
        }
        
        text.append(CORNER).append(LINE_BREAK);
    }
    
    // AGREGA UNA FILA CON SUS VALORES CENTRADOS
    private static void appendRow(StringBuilder text, String[] values, int[] columnWidths) {
        for (int column = 0; column < values.length; column++) {
            text.append(VERTICAL_LINE);
            text.append(centerText(values[column], columnWidths[column]));
        }
        
        text.append(VERTICAL_LINE).append(LINE_BREAK);
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