package com.engine.circuit;

import java.util.ArrayList;
import java.util.List;

import com.engine.LogicSource;
import com.engine.gates.Gate;

// ADMINISTRA LOS CIRCUITOS SEPARADOS, CADA GRUPO DE COMPUERTAS CONECTADAS ES UN CIRCUITO
public class CircuitManager {
    private static final String CIRCUIT_NAME_PREFIX = "Circuito ";
    
    private final List<Circuit> circuits = new ArrayList<>();
    
    // AGREGA UNA COMPUERTA NUEVA EN UN CIRCUITO PROPIO
    public void addGate(Gate gate) {
        circuits.add(createCircuit(List.of(gate)));
    }
    
    // QUITA UNA COMPUERTA Y REORGANIZA LOS CIRCUITOS QUE QUEDARON
    public void removeGate(Gate gate) {
        for (Circuit circuit : circuits) {
            if (circuit.removeGate(gate)) {
                reorganize();
                return;
            }
        }
    }
    
    // VUELVE A REPARTIR LAS COMPUERTAS, UNE LOS CIRCUITOS CONECTADOS Y SEPARA LOS DESCONECTADOS
    public void reorganize() {
        List<Gate> gates = getAllGates();
        
        circuits.clear();
        
        for (List<Gate> group : groupConnectedGates(gates)) {
            circuits.add(createCircuit(group));
        }
    }
    
    // DEVUELVE LA TABLA DE VERDAD DE CADA CIRCUITO
    public List<String[][]> getTruthTables() {
        List<String[][]> tables = new ArrayList<>();
        
        for (Circuit circuit : circuits) {
            tables.add(circuit.getTruthTable());
        }
        
        return tables;
    }
    
    // CREA UN CIRCUITO NUMERADO CON ESAS COMPUERTAS
    private Circuit createCircuit(List<Gate> gates) {
        Circuit circuit = new Circuit(CIRCUIT_NAME_PREFIX + (circuits.size() + 1));
        
        for (Gate gate : gates) {
            circuit.addGate(gate);
        }
        
        return circuit;
    }
    
    // JUNTA LAS COMPUERTAS DE TODOS LOS CIRCUITOS
    private List<Gate> getAllGates() {
        List<Gate> gates = new ArrayList<>();
        
        for (Circuit circuit : circuits) {
            gates.addAll(circuit.getGates());
        }
        
        return gates;
    }
    
    // AGRUPA LAS COMPUERTAS QUE ESTAN CONECTADAS, DIRECTA O INDIRECTAMENTE
    private List<List<Gate>> groupConnectedGates(List<Gate> gates) {
        List<List<Gate>> groups = new ArrayList<>();
        List<Gate> ungrouped = new ArrayList<>(gates);
        
        while (!ungrouped.isEmpty()) {
            List<Gate> group = new ArrayList<>();
            group.add(ungrouped.remove(0));
            
            for (int i = 0; i < group.size(); i++) {
                moveConnectedGates(group.get(i), ungrouped, group);
            }
            
            groups.add(group);
        }
        
        return groups;
    }
    
    // PASA DE UNA LISTA A OTRA LAS COMPUERTAS CONECTADAS A LA COMPUERTA DADA
    private void moveConnectedGates(Gate gate, List<Gate> from, List<Gate> to) {
        for (Gate other : new ArrayList<>(from)) {
            if (areConnected(gate, other)) {
                from.remove(other);
                to.add(other);
            }
        }
    }
    
    // DICE SI UNA ALIMENTA A LA OTRA O SI COMPARTEN UNA ENTRADA
    private boolean areConnected(Gate first, Gate second) {
        return usesAsInput(first, second)
            || usesAsInput(second, first)
            || shareInput(first, second);
    }
    
    // DICE SI LAS DOS COMPUERTAS TIENEN UNA ENTRADA EN COMUN
    private boolean shareInput(Gate first, Gate second) {
        for (LogicSource input : first.getInputs()) {
            if (usesAsInput(second, input)) return true;
        }
        
        return false;
    }
    
    // DICE SI LA FUENTE ES UNA DE LAS ENTRADAS DE LA COMPUERTA
    private boolean usesAsInput(Gate gate, LogicSource source) {
        if (source == null) return false;
        
        for (LogicSource input : gate.getInputs()) {
            if (input == source) return true;
        }
        
        return false;
    }
}