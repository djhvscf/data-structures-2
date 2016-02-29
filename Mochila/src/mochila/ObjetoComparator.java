/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mochila;

import java.util.Comparator;

/**
 *
 * @author Dennis
 */
public class ObjetoComparator implements Comparator<Objeto>{

	@Override
	/*
	 * Devuelve un número negativo, cero o un número positivo si el primer argumento es menor, igual
	 * que, o mayor que el segundo.
	 * 
	 */
	public int compare(Objeto o1, Objeto o2) {
		return Float.compare(o2.getValorPeso(), o1.getValorPeso());
	}

}
