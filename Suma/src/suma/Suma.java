package suma;

public class Suma {

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
