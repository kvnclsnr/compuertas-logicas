package com.simulador.modelo.compuertas;

import com.simulador.modelo.EntradaLogica;

public class CompuertaXOR extends Compuerta {
    
    public CompuertaXOR(EntradaLogica A, EntradaLogica B, String ID) {
        super(ID, A, B);
    }
    
    @Override 
    public boolean evaluar() {
        return getEntradaA().getValor() != getEntradaB().getValor();
    }
}