package com.engine.gates;

import java.util.List;
import java.util.Set;

import com.engine.LogicInput;
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

    @Override
    public boolean isCompoundExpression() {
        LogicSource[] inputs = getInputs();
        return inputs != null && inputs.length > 1;
    }

    //recorre recursivamente las entradas y las añade al orden de evaluacion
    //delega según el tipo real del objeto sin instanceof
    @Override
    public void collectInputs(Set<LogicInput> primaryInputs, List<Gate> evaluationOrder, Set<Gate> visitedGates) {
        if (visitedGates.contains(this)) {
            return;
        }
        visitedGates.add(this);
        
        LogicSource[] inputs = getInputs();
        if (inputs != null) {
            for (LogicSource input : inputs) {
                if (input != null) {
                    input.collectInputs(primaryInputs, evaluationOrder, visitedGates);
                }
            }
        }
        
        evaluationOrder.add(this);
    }
}