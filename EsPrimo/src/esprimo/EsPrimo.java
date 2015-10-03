package esprimo;

public class EsPrimo {

    public static void main(String[] args) {
        System.out.println(EsPrimo(15));
    }
    
    private static boolean esPrimo = true;
    
    private static boolean EsPrimoRecursivo(int n, int divisor, boolean resul) {    
        divisor = divisor - 1;
        if(divisor > 1) {
            if(n % (divisor) != 0) {
                EsPrimoRecursivo(n, divisor, resul);
            } else {
                esPrimo = false;
            }
        }
        return esPrimo;
    }
    
    private static boolean EsPrimo(int num){
        return EsPrimoRecursivo(num, num, true);
    }
    
}
