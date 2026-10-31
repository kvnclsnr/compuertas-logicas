package com.engine.gates;

import com.engine.LogicSource;

public abstract class Gate implements LogicSource {
    private String name;
    
    public Gate(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract boolean evaluate();
    
    public abstract LogicSource[] getInputs();
    
    public abstract String getOperator();
    
    @Override
    public String getExpression() {
        LogicSource[] inputs = getInputs();
        
        if (inputs.length == 1) {
            return getOperator() + " " + formatInputExpression(inputs[0]);
        }
        
        return formatInputExpression(inputs[0]) + " "
                + getOperator() + " " + formatInputExpression(inputs[1]);
    }
    
    @Override
    public boolean getValue() {
        return evaluate();
    }
    
    private String formatInputExpression(LogicSource input) {
        String expression = input.getExpression();
        
        if (input instanceof Gate gate && gate.getInputs().length > 1) {
            return "(" + expression + ")";
        }
        
        return expression;
    }
}