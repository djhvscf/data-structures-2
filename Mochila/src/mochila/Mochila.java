/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mochila;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Dennis
 */
public class Mochila {

    private float capacidad;

    public Mochila(float c) {
        capacidad = c;
    }

    private void llenarMochila(Objeto[] objetos) {

        Arrays.sort(objetos, new ObjetoComparator());

        float pesoMochila = 0;
        float valorMochila = 0;

        int i = 0;
        int[] solucion = new int[objetos.length];

        while ((i < objetos.length) && (pesoMochila + objetos[i].getPeso() <= capacidad)) {
            pesoMochila += (float) objetos[i].getPeso();
            valorMochila += (float) objetos[i].getValor();
            System.out.println("Valor: " + objetos[i].getValor() + " - Peso: " + objetos[i].getPeso());
            solucion[i] = i;
            i++;
        }
        if (i < objetos.length && pesoMochila < capacidad) {
            float capacidadRestante = capacidad - pesoMochila;

            valorMochila += (float) objetos[i].getValorPeso() * capacidadRestante;
            pesoMochila = capacidad;
        }

        /*System.out.println("Capacidad de la mochila: " + capacidad);
        System.out.println("Peso de la mochila después del proceso: " + pesoMochila);
        System.out.println("Valor de la mochila después del proceso: " + valorMochila);*/
        /*System.out.print("Objetos insertados en la mochila: ");*/
        /*for (int j = 0; j < i; j++) {
            System.out.print(solucion[j] + " ");
        }*/
    }

    public static void main(String[] args) {

        int num_objetos;
        int tamano_mochila;
        try (Scanner in = new Scanner(System.in)) {
            num_objetos = in.nextInt();
            tamano_mochila = in.nextInt();
        }  
        
        Objeto[] objetos = new Objeto[num_objetos];
        
        float[] pesos = new float[num_objetos];
        int[] valores = new int[num_objetos];

        Random randomGenerator = new Random();
        for (int i = 1; i < num_objetos; ++i) {
            pesos[i] = randomGenerator.nextInt(100);
            valores[i] = randomGenerator.nextInt(100);
        }
        
        for (int i = 0; i < num_objetos; i++) {
            objetos[i] = new Objeto(pesos[i], valores[i]);
        }

        Mochila mochila = new Mochila(tamano_mochila);

        mochila.llenarMochila(objetos);
    }
}
