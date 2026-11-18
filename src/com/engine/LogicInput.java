package com.engine;

import java.util.List;
import java.util.Set;

import com.engine.gates.Gate;

// ENTRADA LOGICA CON NOMBRE Y UN VALOR QUE SE PUEDE CAMBIAR
public class LogicInput implements LogicSource {
    private String name;
    private boolean value;
    
    // CREA LA ENTRADA APAGADA
    public LogicInput(String name) {
        this(name, false);
    }
    
    // CREA LA ENTRADA CON UN VALOR INICIAL
    public LogicInput(String name, boolean value) {
        this.name = name;
        this.value = value;
    }
    
    // DEVUELVE EL VALOR ACTUAL
    @Override
    public boolean getValue() {
        return value;
    }
    
    // CAMBIA EL VALOR ACTUAL
    public void setValue(boolean value) {
        this.value = value;
    }
    
    // DEVUELVE EL NOMBRE DE LA ENTRADA
    public String getName() {
        return name;
    }
    
    // CAMBIA EL NOMBRE DE LA ENTRADA
    public void setName(String name) {
        this.name = name;
    }
    
    // LA EXPRESION DE UNA ENTRADA ES SU NOMBRE
    @Override
    public String getExpression() {
        return name;
    }
    
    // SE AGREGA A SI MISMA A LAS ENTRADAS RECOLECTADAS
    @Override
    public void collectInputs(Set<LogicInput> inputs, List<Gate> evaluationOrder, Set<Gate> visitedGates) {
        inputs.add(this);
    }
}