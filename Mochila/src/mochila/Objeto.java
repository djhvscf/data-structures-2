/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mochila;

/**
 *
 * @author Dennis
 */
public class Objeto {

    private float peso;
    private int valor;
    private float valorPeso;

    public Objeto(float p, int v) {
        peso = p;
        valor = v;
        valorPeso = (float) valor / peso;
    }

    public float getPeso() {
        return peso;
    }

    public int getValor() {
        return valor;
    }

    public float getValorPeso() {
        return valorPeso;
    }

}
