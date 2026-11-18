package com.engine.persistence;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Contrato de persistencia para el guardado y carga de circuitos.
 * Basado en la Diapositiva 54 de la Clase 14 de POO.
 */
public interface RepositorioCircuitos {
    
    /**
     * Guarda el circuito especificado en la ruta dada.
     *
     * @param c Circuito a persistir
     * @param ruta Ruta del archivo de destino
     * @throws IOException Si ocurre un error de entrada/salida
     */
    void guardar(Circuito c, Path ruta) throws IOException;

    /**
     * Carga un circuito desde la ruta especificada.
     *
     * @param ruta Ruta del archivo a leer
     * @return El circuito reconstruido a partir del archivo
     * @throws IOException Si ocurre un error de entrada/salida
     * @throws FormatoCircuitoException Si el formato del archivo es inválido
     */
    Circuito cargar(Path ruta) throws IOException, FormatoCircuitoException;
}
