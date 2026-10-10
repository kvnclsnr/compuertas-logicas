package com.simulador.modelo.bancos;

import java.util.ArrayList;

import com.simulador.modelo.compuertas.Compuerta;

public class Banco {
    private ArrayList<Compuerta> compuertas;

    public Banco(){
        ArrayList<Compuerta> compuertas = new ArrayList<>();
    }

    public void agregar(Compuerta compuerta){
        compuertas.add(compuerta);
    }

    public void evaluarTodas() {
        for (Compuerta i : compuertas ){
            i.evaluar();
        }
    }

    public int getCantidad() {
        return compuertas.size();
    }

    public void eliminar(String nombre){
        for (Compuerta i : compuertas){
            if (i.getNombre().equals(nombre)){
                compuertas.remove(i);
            }
        }
    }

    public Compuerta buscar(String nombre){
        for (int i = 0; i < compuertas.size(); i++){
            if (compuertas.get(i).getNombre().equals(nombre)){
                return compuertas.get(i);
            }
        }
        return null;
    }
}
