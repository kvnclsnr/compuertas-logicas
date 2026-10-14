package com.simulador.modelo.compuertas;

import com.simulador.modelo.EntradaLogica;

public class CompuertaNOT extends Compuerta{
    private EntradaLogica A;
    
    public CompuertaNOT(EntradaLogica A, String ID) {
        super(ID, A);
        this.A = A;
    }
    
    public boolean evaluar() {
        return !A.getValor();
    }
}