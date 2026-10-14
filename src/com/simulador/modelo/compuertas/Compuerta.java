package com.simulador.modelo.compuertas;

import com.simulador.modelo.EntradaLogica;

public abstract class Compuerta {
    protected  String nombre;
    protected  EntradaLogica A;
    protected  EntradaLogica B;

    public Compuerta(String nombre, EntradaLogica A, EntradaLogica B){
        this.nombre = nombre;
        this.A = A;
        this.B = B;
    }

    public Compuerta(String nombre, EntradaLogica A){
        this.nombre = nombre;
        this.A = A;
    }

    public String getNombre(){
        return nombre;
    }

    public abstract boolean evaluar();

}
