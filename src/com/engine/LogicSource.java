package com.engine;

import java.util.List;
import java.util.Set;

import com.engine.gates.Gate;

// FUENTE DE UNA SEÑAL LOGICA, PUEDE SER UNA ENTRADA O UNA COMPUERTA
public interface LogicSource {
    
    // DEVUELVE EL VALOR ACTUAL DE LA SEÑAL
    boolean getValue();
    
    // DEVUELVE LA EXPRESION LOGICA QUE PRODUCE LA SEÑAL
    String getExpression();
    
    // RECOLECTA LAS ENTRADAS Y EL ORDEN DE EVALUACION SIN REPETIR COMPUERTAS
    void collectInputs(Set<LogicInput> inputs, List<Gate> evaluationOrder, Set<Gate> visitedGates);
    
    // DICE SI LA EXPRESION NECESITA PARENTESIS AL USARSE DENTRO DE OTRA
    default boolean isCompoundExpression() {
        return false;
    }
}