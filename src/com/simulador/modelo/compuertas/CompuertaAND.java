package com.simulador.modelo.compuertas;

import com.simulador.modelo.EntradaLogica;

public class CompuertaAND extends Compuerta {
    private EntradaLogica A;
    private EntradaLogica B;
    
    
    public CompuertaAND (EntradaLogica A, EntradaLogica B, String ID) {
        super(ID);
        this.A = A;
        this.B = B;
    }
    
    @Override 
    public boolean evaluar() {
        return A.getValor() && B.getValor();
    }
}