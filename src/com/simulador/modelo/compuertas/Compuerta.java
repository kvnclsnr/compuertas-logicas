package com.simulador.modelo.compuertas;

public abstract class Compuerta {
    protected  String nombre;

    public Compuerta(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return nombre;
    }

    public abstract boolean evaluar();

}
