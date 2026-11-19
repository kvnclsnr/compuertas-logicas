package com.ui.importer;

import java.util.List;

import com.ui.icons.ElementType;

// DATOS LEIDOS DEL ARCHIVO, LOS ELEMENTOS Y LOS CABLES DEL LIENZO
public record CircuitData(List<ElementData> elements, List<WireData> wires) {
    
    // UN ELEMENTO DEL ARCHIVO, SU NUMERO, SU TIPO, SU POSICION Y SU VALOR
    public record ElementData(int id, ElementType type, int x, int y, boolean value) { }
    
    // UN CABLE DEL ARCHIVO, LOS NUMEROS DE SUS DOS ELEMENTOS Y LA ENTRADA DONDE TERMINA
    public record WireData(int source, int target, int input) { }
}