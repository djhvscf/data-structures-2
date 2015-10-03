package invertirLista;

import java.util.ArrayList;

public class InvertirLista {

    public static void main(String[] args) {
        ArrayList<Integer> lista = crearLista(3);
        
        lista.stream().forEach((i) -> {
            System.out.println(i);
        });

        System.out.println("--------");
        
        invertirLista(lista).stream().forEach((i) -> {
            System.out.println(i);
        });
    }
    
    public static ArrayList<Integer> crearLista(int n) {
        ArrayList<Integer> nuevaLista = null;
        if (n <= 0) {
            nuevaLista = new ArrayList<>();
            return nuevaLista;
        } else {
            nuevaLista = crearLista(n-1);
            nuevaLista.add(n);
        }
        return nuevaLista;
    }
    
    public static ArrayList<Integer> invertirLista(ArrayList<Integer> lista) {
        ArrayList<Integer> listaInvertida = clonarLista(lista);
        if (listaInvertida.size() <= 1) {
            return listaInvertida;
        } else {
            Integer num = listaInvertida.remove(0);
            listaInvertida = invertirLista(listaInvertida); 
            listaInvertida.add(num);
        }
        return listaInvertida;
    }
    
    public static ArrayList<Integer> clonarLista(ArrayList<Integer> lista) {
        ArrayList<Integer> nuevaLista = new ArrayList<>();
        lista.stream().forEach(nuevaLista::add);
        return nuevaLista;
    }
}
