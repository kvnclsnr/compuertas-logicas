package com.simulador.modelo.compuertas;

import com.simulador.modelo.EntradaLogica;

public class CompuertaXOR extends Compuerta {
    private EntradaLogica A;
    private EntradaLogica B;
    
    public CompuertaXOR(EntradaLogica A, EntradaLogica B, String ID) {
        super(ID);
        this.A = A;
        this.B = B;
    }
    
    public boolean evaluar() {
        return A.getValor() != B.getValor();
    }
}