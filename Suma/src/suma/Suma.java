/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suma;

/**
 *
 * @author Dennis
 */
public class Suma {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(suma(3));
    }
    
    private static double suma(double n) {
        if (n == 0) {
            return 0;
        } else {
            return 1 + suma(n-1);
        }
    }
}
