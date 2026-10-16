package com.simulador.modelo.compuertas;

import com.simulador.modelo.EntradaLogica;

public class CompuertaNOT extends Compuerta{
    
    public CompuertaNOT(EntradaLogica A, String ID) {
        super(ID, A);
    }
    
    @Override
    public boolean evaluar() {
        return !getEntradaA().getValor();
    }
}