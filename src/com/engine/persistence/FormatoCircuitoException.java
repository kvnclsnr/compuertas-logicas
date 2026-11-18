package com.engine.persistence;

/**
 * Excepción checked lanzada cuando el formato de un archivo de circuito
 * es inválido, está corrupto o viola las reglas semánticas del dominio.
 * Cumple con el diseño presentado en la Clase 14 de POO (Diapositivas 50-58).
 */
public class FormatoCircuitoException extends Exception {
    private final int numeroLinea;

    public FormatoCircuitoException(String mensaje) {
        super(mensaje);
        this.numeroLinea = -1;
    }

    public FormatoCircuitoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.numeroLinea = -1;
    }

    public FormatoCircuitoException(int numeroLinea, String mensaje) {
        super("Línea " + numeroLinea + ": " + mensaje);
        this.numeroLinea = numeroLinea;
    }

    public FormatoCircuitoException(int numeroLinea, String mensaje, Throwable causa) {
        super("Línea " + numeroLinea + ": " + mensaje, causa);
        this.numeroLinea = numeroLinea;
    }

    public int getNumeroLinea() {
        return numeroLinea;
    }
}
