package com.simulador.modelo.compuertas;

import com.simulador.modelo.EntradaLogica;

public class CompuertaNAND extends Compuerta {
    
    public CompuertaNAND(EntradaLogica A, EntradaLogica B, String ID ) {
        super(ID, A, B);
    }
    
    public boolean evaluar() {
        return !(A.getValor() && B.getValor());
    }
}