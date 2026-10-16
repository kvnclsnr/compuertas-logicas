package com.simulator.model.gates;

import com.simulator.model.LogicInput;

public abstract class Gate {
    private String name;
    
    private LogicInput inputA;
    private LogicInput inputB;
    
    public Gate(String name, LogicInput inputA, LogicInput inputB) {
        this.name = name;
        this.inputA = inputA;
        this.inputB = inputB;
    }
    
    public Gate(String name, LogicInput inputA) {
        this.name = name;
        this.inputA = inputA;
        this.inputB = null;
    }
    
    public String getName() {
        return name;
    }
    
    protected LogicInput getInputA() {
        return inputA;
    }
    
    protected LogicInput getInputB() {
        return inputB;
    }
    
    public abstract boolean evaluate();
    
    public void showTruthTable() {
        boolean[] inputValues = {true, false};
        
        boolean hasTwoInputs = inputB != null;
        
        boolean originalInputA = inputA.getValue();
        boolean originalInputB = hasTwoInputs ? inputB.getValue() : false;
        
        System.out.println("Truth Table: " + name + "\n");
        
        if (hasTwoInputs) {
            String rowDivider = "+-----+-----+---------+";
            
            System.out.println(rowDivider);
            System.out.println("|  A  |  B  | Result  |");
            System.out.println(rowDivider);
            
            for (boolean valueA : inputValues) {
                for (boolean valueB : inputValues) {
                    inputA.setValue(valueA);
                    inputB.setValue(valueB);
                    
                    boolean result = evaluate();
                    
                    System.out.printf(
                        "|  %d  |  %d  |    %d    |%n",
                        valueA ? 1 : 0,
                        valueB ? 1 : 0,
                        result ? 1 : 0
                    );
                }
            }
            System.out.println(rowDivider);
        }
        
        else {
            String rowDivider = "+-----+---------+";
            
            System.out.println(rowDivider);
            System.out.println("|  A  | Result  |");
            System.out.println(rowDivider);
            
            for (boolean valueA : inputValues) {
                inputA.setValue(valueA);
                
                boolean result = evaluate();
                
                System.out.printf(
                    "|  %d  |    %d    |%n",
                    valueA ? 1 : 0,
                    result ? 1 : 0
                );
            }
            System.out.println(rowDivider);
        }
        
        System.out.println();
        
        inputA.setValue(originalInputA);
        
        if (hasTwoInputs) {
            inputB.setValue(originalInputB);
        }
    }
}