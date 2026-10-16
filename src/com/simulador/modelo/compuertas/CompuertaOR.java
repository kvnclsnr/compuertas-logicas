package com.simulador.modelo.compuertas;

import com.simulador.modelo.EntradaLogica;

public class CompuertaOR extends Compuerta {
    
    public CompuertaOR(EntradaLogica A, EntradaLogica B, String ID) {
        super(ID, A, B);
    }
    
    @Override
    public boolean evaluar() {
        return getEntradaA().getValor() || getEntradaB().getValor();
    }
}