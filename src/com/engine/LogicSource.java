package com.engine;

import java.util.List;
import java.util.Set;

import com.engine.gates.Gate;

public interface LogicSource {
    boolean getValue();
    String getExpression();

    //este metodo recolecta las entradas y el orden de evaluacion
    //se usa set para no admitir repetidos a la hora de crear la tabla de verdad
    //se usa Set<gate> visitedGates para recordar qué compuertas ya revisamos y no entrar en un bucle infinito si una compuerta alimenta a varias otras.
    void collectInputs(Set<LogicInput> primaryInputs, List<Gate> evaluationOrder, Set<Gate> visitedGates);


    //este metodo indica si la fuente es una expresion compuesta
    default boolean isCompoundExpression(){
        return false;
    }
}