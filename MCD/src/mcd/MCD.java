package mcd;

public class MCD {

    public static void main(String[] args) {
        System.out.println(MCD(4, 12));
    }
    
    private static int MCD(int a, int b) {
       if(b == 0) {
           return a;   
       }
       else {
           return MCD(b, a % b);   
       }
   }
}
