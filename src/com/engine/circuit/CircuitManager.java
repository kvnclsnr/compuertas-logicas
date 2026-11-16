package com.engine.circuit;

import java.util.ArrayList;
import java.util.List;

import com.engine.LogicSource;
import com.engine.gates.Gate;

public class CircuitManager {
    private final List<Circuit> circuits = new ArrayList<>();
    
    public void addGate(Gate gate) {
        circuits.add(createCircuit(List.of(gate)));
    }
    
    public void removeGateByName(String name) {
        for (Circuit circuit : circuits) {
            if (circuit.removeGateByName(name)) {
                reorganize();
                return;
            }
        }
    }
    
    public void reorganize() {
        List<Gate> gates = getAllGates();
        
        circuits.clear();
        
        for (List<Gate> group : getConnectedGateGroups(gates)) {
            circuits.add(createCircuit(group));
        }
    }
    
    public List<String[][]> getTruthTables() {
        List<String[][]> tables = new ArrayList<>();
        
        for (Circuit circuit : circuits) {
            tables.add(circuit.getTruthTable());
        }
        
        return tables;
    }
    
    private Circuit createCircuit(List<Gate> gates) {
        Circuit circuit = new Circuit("Circuito " + (circuits.size() + 1));
        
        for (Gate gate : gates) {
            circuit.addGate(gate);
        }
        
        return circuit;
    }
    
    private List<Gate> getAllGates() {
        List<Gate> gates = new ArrayList<>();
        
        for (Circuit circuit : circuits) {
            gates.addAll(circuit.getGates());
        }
        
        return gates;
    }
    
    private List<List<Gate>> getConnectedGateGroups(List<Gate> gates) {
        List<List<Gate>> groups = new ArrayList<>();
        
        List<Gate> remaining = new ArrayList<>(gates);
        
        while (!remaining.isEmpty()) {
            List<Gate> group = new ArrayList<>();
            group.add(remaining.remove(0));
            
            for (int i = 0; i < group.size(); i++) {
                Gate current = group.get(i);
                
                int j = 0;
                
                while (j < remaining.size()) {
                    if (areConnected(current, remaining.get(j))) {
                        group.add(remaining.remove(j));
                    }
                    
                    else {
                        j++;
                    }
                }
            }
            
            groups.add(group);
        }
        
        return groups;
    }
    
    private boolean areConnected(Gate first, Gate second) {
        if (hasInput(first, second) || hasInput(second, first)) {
            return true;
        }
        
        for (LogicSource input : first.getInputs()) {
            if (hasInput(second, input)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean hasInput(Gate gate, LogicSource source) {
        if (source == null) return false;
        
        for (LogicSource input : gate.getInputs()) {
            if (input == source) return true;
        }
        
        return false;
    }
}