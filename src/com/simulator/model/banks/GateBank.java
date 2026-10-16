package com.simulator.model.banks;

import java.util.ArrayList;

import com.simulator.model.gates.Gate;

public class GateBank {
    private ArrayList<Gate> gates;
    
    public GateBank(ArrayList<Gate> gates) {
        this.gates = gates;
    }
    
    public void add(Gate gate) {
        gates.add(gate);
    }
    
    public void evaluateAll() {
        for (Gate gate : gates) {
            gate.evaluate();
        }
    }
    
    public int getCount() {
        return gates.size();
    }
    
    public void removeByName(String name) {
        for (int i = 0; i < gates.size(); i++) {
            if (gates.get(i).getName().equals(name)) {
                gates.remove(i);
                return;
            }
        }
    }
    
    public Gate findByName(String name) {
        for (Gate gate : gates) {
            if (gate.getName().equals(name)) {
                return gate;
            }
        }
        
        return null;
    }
}