package com.simulador.modelo.compuertas;

import com.simulador.modelo.EntradaLogica;

public class CompuertaXNOR extends Compuerta{
    
    public CompuertaXNOR(EntradaLogica A, EntradaLogica B, String ID) {
        super(ID, A, B);
    }
    
    public boolean evaluar() {
        return A.getValor() == B.getValor();
    }
}