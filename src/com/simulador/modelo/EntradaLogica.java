package com.simulador.modelo;

public class EntradaLogica {
    private String nombre;
    private boolean valor;
    
    public EntradaLogica(String nombre, boolean valor){
        this.nombre = nombre;
        this.valor = valor;
    }
    
    public boolean getValor(){
        return valor;
    }
    
    public String getNombre(){
        return  nombre;
    }
}