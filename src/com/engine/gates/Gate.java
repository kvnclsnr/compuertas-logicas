package com.engine.gates;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.engine.LogicInput;
import com.engine.LogicSource;

// BASE DE TODAS LAS COMPUERTAS, SABE SU NOMBRE, SU EXPRESION Y COMO RECOLECTAR SUS ENTRADAS
public abstract class Gate implements LogicSource {
    private static final String MISSING_INPUT_EXPRESSION = "0";
    
    // GUARDAS CONTRA CICLOS EN EVALUACION Y FORMATEO DE EXPRESIONES
    private static final ThreadLocal<Set<Gate>> EVALUATING_GATES = ThreadLocal.withInitial(HashSet::new);
    private static final ThreadLocal<Set<Gate>> FORMATTING_GATES = ThreadLocal.withInitial(HashSet::new);
    
    private String name;
    
    // CREA LA COMPUERTA CON SU NOMBRE
    public Gate(String name) {
        this.name = name;
    }
    
    // DEVUELVE EL NOMBRE DE LA COMPUERTA
    public String getName() {
        return name;
    }
    
    // CAMBIA EL NOMBRE DE LA COMPUERTA
    public void setName(String name) {
        this.name = name;
    }
    
    // CAMBIA LA ENTRADA NUMERO "index"
    public abstract void setInput(int index, LogicSource input);
    
    // DEVUELVE UNA COPIA CON LAS ENTRADAS, LAS NO CONECTADAS SON: null
    public abstract LogicSource[] getInputs();
    
    // DEVUELVE EL TEXTO DE LA OPERACION, POR EJEMPLO "AND"
    public abstract String getOperator();
    
    // ARMA LA EXPRESION, PROTEGIENDOSE CONTRA BUCLES O RETROALIMENTACION
    @Override
    public String getExpression() {
        Set<Gate> formatting = FORMATTING_GATES.get();
        if (!formatting.add(this)) {
            return "[CICLO:" + getName() + "]";
        }
        
        try {
            LogicSource[] inputs = getInputs();
            
            if (inputs.length == 1) {
                return getOperator() + " " + formatInput(inputs[0]);
            }
            
            return formatInput(inputs[0]) + " " + getOperator() + " " + formatInput(inputs[1]);
        } finally {
            formatting.remove(this);
        }
    }
    
    // UNA EXPRESION ES COMPUESTA CUANDO LA COMPUERTA TIENE MAS DE UNA ENTRADA
    @Override
    public boolean isCompoundExpression() {
        return getInputs().length > 1;
    }
    
    // RECOLECTA PRIMERO LAS ENTRADAS Y DESPUES SE AGREGA A SI MISMA AL ORDEN DE EVALUACION
    @Override
    public void collectInputs(Set<LogicInput> inputs, List<Gate> evaluationOrder, Set<Gate> visitedGates) {
        if (visitedGates.contains(this)) return;
        
        visitedGates.add(this);
        
        for (LogicSource source : getInputs()) {
            if (source != null) {
                source.collectInputs(inputs, evaluationOrder, visitedGates);
            }
        }
        
        evaluationOrder.add(this);
    }
    
    // INDICA SI ESTA COMPUERTA SE ENCUENTRA EN PROCESO DE EVALUACION EN EL HILO ACTUAL
    public boolean isEvaluating() {
        return EVALUATING_GATES.get().contains(this);
    }
    
    // LEE EL VALOR DE UNA FUENTE, SI NO ESTA CONECTADA O SE DETECTA UN CICLO VALE FALSO
    protected static boolean readValue(LogicSource source) {
        if (source == null) return false;
        
        if (source instanceof Gate gate) {
            Set<Gate> evaluating = EVALUATING_GATES.get();
            if (!evaluating.add(gate)) {
                return false;
            }
            try {
                return gate.getValue();
            } finally {
                evaluating.remove(gate);
            }
        }
        
        return source.getValue();
    }
    
    // DEVUELVE LA EXPRESION DE UNA ENTRADA, CON PARENTESIS SI ES COMPUESTA O DETECCION DE CICLO
    private String formatInput(LogicSource input) {
        if (input == null) return MISSING_INPUT_EXPRESSION;
        
        if (input instanceof Gate gate && FORMATTING_GATES.get().contains(gate)) {
            return "[CICLO:" + gate.getName() + "]";
        }
        
        if (input.isCompoundExpression()) {
            return "(" + input.getExpression() + ")";
        }
        
        return input.getExpression();
    }
}