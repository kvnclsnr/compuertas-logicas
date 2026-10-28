package com.simulator.model;

public class LogicInput implements LogicSource {
    private String name;
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
}