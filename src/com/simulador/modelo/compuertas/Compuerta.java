package com.simulador.modelo.compuertas;

import com.simulador.modelo.EntradaLogica;

public abstract class Compuerta {
    private String nombre;
    
    private EntradaLogica entradaA;
    private EntradaLogica entradaB;
    
    public Compuerta(String nombre, EntradaLogica entradaA, EntradaLogica entradaB){
        this.nombre = nombre;
        this.entradaA = entradaA;
        this.entradaB = entradaB;
    }
    
    public Compuerta(String nombre, EntradaLogica entradaA){
        this.nombre = nombre;
        this.entradaA = entradaA;
        this.entradaB = null;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    protected final EntradaLogica getEntradaA(){
        return entradaA;
    }
    
    protected final EntradaLogica getEntradaB(){
        return entradaB;
    }
    
    public abstract boolean evaluar();
}