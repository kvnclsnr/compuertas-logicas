package com.ui.export;

// PALABRAS Y SEPARADORES DEL ARCHIVO DE TEXTO DEL LIENZO, LOS COMPARTEN EL EXPORTADOR Y EL IMPORTADOR
public final class CircuitFormat {
    public static final String CANVAS_SECTION = "[LIENZO]";
    public static final String ELEMENT_SECTION = "[ELEMENTO]";
    public static final String WIRE_SECTION = "[CABLE]";
    
    public static final String KEY_VALUE_SEPARATOR = "=";
    
    public static final String ELEMENT_COUNT_KEY = "ELEMENTOS";
    public static final String WIRE_COUNT_KEY = "CABLES";
    
    public static final String ID_KEY = "ID";
    public static final String TYPE_KEY = "TIPO";
    public static final String X_KEY = "X";
    public static final String Y_KEY = "Y";
    public static final String VALUE_KEY = "VALOR";
    
    public static final String SOURCE_KEY = "ORIGEN";
    public static final String TARGET_KEY = "DESTINO";
    public static final String INPUT_KEY = "ENTRADA";
    
    public static final String TRUE_TEXT = "1";
    public static final String FALSE_TEXT = "0";
    
    private CircuitFormat() { }
}