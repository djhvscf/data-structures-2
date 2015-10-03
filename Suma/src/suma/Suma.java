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
    
    private static int suma(int n) {
        if (n == 1) {
            return 1;
        } else {
            return n + suma(n-1);
        }
    }
}
