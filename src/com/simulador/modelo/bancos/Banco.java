package com.simulador.modelo.bancos;

import java.util.ArrayList;

import com.simulador.modelo.compuertas.Compuerta;

public class Banco {
    private ArrayList<Compuerta> compuertas;
    
    public Banco(ArrayList<Compuerta> compuertas) {
        this.compuertas = compuertas;
    }
    
    public void agregar(Compuerta compuerta) {
        compuertas.add(compuerta);
    }
    
    public void evaluarTodas() {
        for (Compuerta compuerta : compuertas) {
            compuerta.evaluar();
        }
    }
    
    public int getCantidad() {
        return compuertas.size();
    }
    
    public void eliminar(String nombre){
        for (int i = 0; i < compuertas.size(); i++) {
            if (compuertas.get(i).getNombre().equals(nombre)) {
                compuertas.remove(i);
                return;
            }
        }
    }
    
    public Compuerta buscar(String nombre){
        for (Compuerta compuerta : compuertas) {
            if (compuerta.getNombre().equals(nombre)) {
                return compuerta;
            }
        }
        
        return null;
    }
}