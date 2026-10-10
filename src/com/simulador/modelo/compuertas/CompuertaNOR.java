package com.simulador.modelo.compuertas;

import com.simulador.modelo.EntradaLogica;

public class CompuertaNOR extends Compuerta {
    private EntradaLogica A;
    private EntradaLogica B;
    
    public CompuertaNOR(EntradaLogica A, EntradaLogica B, String ID) {
        super(ID);
        this.A = A;
        this.B = B;
    }
    
    public boolean evaluar() {
        return !(A.getValor() || B.getValor());
    }
}