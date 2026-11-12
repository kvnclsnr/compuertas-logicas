package com.engine;

import java.util.List;
import java.util.Set;

import com.engine.gates.Gate;

public class LogicInput implements LogicSource {
    private final String name;
    private boolean value;
    
    public LogicInput(String name) {
        this(name, false); 
    }
    
    public LogicInput(String name, boolean value) {
        this.name = name;
        this.value = value;
    }
    
    @Override
    public boolean getValue() {
        return value;
    }
    
    @Override
    public String getExpression() {
        return name;
    }
    
    public void setValue(boolean value) {
        this.value = value;
    }
    
    public String getName() {
        return name;
    }
    
    //como eta es una entrada logica se agrega a si misma
    @Override
    public void collectInputs(Set<LogicInput> primaryInputs, List<Gate> evaluationOrder, Set<Gate> visitedGates) {
        primaryInputs.add(this);
    }
}