package pkg4enraya;

enum Casilla {LIBRE,JUGADOR1,JUGADOR2};
public class Conecta4 {


        
    // tablero
    // las celda (0,0) es la del vértice superior izquierdo)
    public Casilla[][] tablero;  // [fila][columna]
    private int numFil, numCol;
    public int jugador; //controla el turno del jugador siguiente

    /**
     * Construye un tablero con el número de filas y columnas indicado
     * @param numCol número de columnas
     * @param numFil número de filas
     */
    public Conecta4(int numCol, int numFil) {
        this.numFil = numFil;
        this.numCol = numCol;
        this.jugador=1;
        tablero = new Casilla[numFil][numCol];
        // crea las celdas
        for (int f = 0; f < numFil; f++)
            for (int c = 0; c < numCol; c++)
                tablero[f][c] = Casilla.LIBRE;
    }

    /**
     * Número del filas del tablero
     * @return número de filas del tablero
     */
    public int getNumFilas() {
        return numFil;
    }

    /**
     * Número de columnas del tablero
     * @return número de columnas del tablero
     */
    public int getNumColumnas() {
        return numCol;
    }
    
    /**
     * Estado de la casilla seleccionada
     * @return Estado de la casilla seleccionada (LIBRE, JUGADOR1, JUGADOR2)
     */
    public Casilla getCasilla(int c, int f) {
        return tablero[f][c];
    }
        

    /**
     * pone la ficha en la columna seleccinada por el usuario
     * @param c columna
     * @param j jugador
     * @return Retona -1 si es imposible poner la icha en la columna indicada. En caso de que se haya introducido
     * correctamente, devuelve la fila que ha sido ocupada.
     */
    public int ponFicha(int c) {
        
        int f=hayEspacio(c);
        if(f!=-1)
        {
            if(jugador==1)
            {
                tablero[f][c]=Casilla.JUGADOR1;
                jugador=2;
            }
            else
            {
                tablero[f][c]=Casilla.JUGADOR2;
                jugador=1;
            }
        }
        return f;
       
    }
    
    /**
     * Vacia completamente el tablero de juego, dejando todas las casillas libres
     */
    public void vacia() {
           for (int f = 0; f < numFil; f++)
            for (int c = 0; c < numCol; c++)
                tablero[f][c] = Casilla.LIBRE;
    }
    
    public int victoria(int c, int f){
        if (!columnaValida(c))
            return -1;
        if (!filaValida(f))
            return -1;
 
        Casilla aBuscar=tablero[f][c];
        
        //comprobamos si en la columna hay 4 en linea
        int fIni=f+1;
        int enLinea=1;
        while(filaValida(fIni)&&(tablero[fIni][c]==aBuscar))
        {
            enLinea++;
            fIni++;
        }
        fIni=f-1;
        while(filaValida(fIni)&&(tablero[fIni][c]==aBuscar))
        {
            enLinea++;
            fIni--;
        }
        
        
        
        if(enLinea>=4)	
            return 1;
       
        //comprobamos que en la fila hay 4 en linea
        int cIni=c+1;
        enLinea=1;
        
        while(columnaValida(cIni)&&(tablero[f][cIni]==aBuscar))
        {
            enLinea++;
            cIni++;
        }
        cIni=c-1;
        while(columnaValida(cIni)&&(tablero[f][cIni]==aBuscar))
        {
            enLinea++;
            cIni--;
        }
        
         if(enLinea>=4)	
            return 1;
       
        //comprobamos que en diagonal hay 4 en linea
        cIni=c+1;
        fIni=f+1;
        enLinea=1;
        while(filaValida(fIni)&&columnaValida(cIni)&&(tablero[fIni][cIni]==aBuscar))
        {
            enLinea++;
            cIni++;
            fIni++;
        }
        fIni=f-1;
        cIni=c-1;
        while(filaValida(fIni)&&columnaValida(cIni)&&(tablero[fIni][cIni]==aBuscar))
        {
            enLinea++;
            cIni--;
            fIni--;
        }
       
        if(enLinea>=4)	
            return 1;
       
        //comprobamos que en diagonal hay 4 en linea
        cIni=c+1;
        fIni=f-1;
        enLinea=1;
        while(filaValida(fIni)&&columnaValida(cIni)&&(tablero[fIni][cIni]==aBuscar))
        {
            enLinea++;
            cIni++;
            fIni--;
        }
        fIni=f+1;
        cIni=c-1;
        while(filaValida(fIni)&&columnaValida(cIni)&&(tablero[fIni][cIni]==aBuscar))
        {
            enLinea++;
            cIni--;
            fIni++;
        }
        
        if(enLinea>=4)	
            return 1;
            
        return 0;
    }
    
    /**
     * Indica si hay espacio para insertar una ficha en la columna seleccionada
     * @param c columna
     * @return Retona -1 si es imposible poner la icha en la columna indicada. En caso de que se haya espacio,
     * devuelve la fila disponible.
     */
    public int hayEspacio(int c){
        if (!columnaValida(c))
            return -1;
        else
        {
            int f=0;
            while(filaValida(f)&&(tablero[f][c]==Casilla.LIBRE))
                 f++;
            if(filaValida(f-1))
                return (f-1);
            else
                return -1;
        }
    }
    
    /**
     * Indica si la fila es válida (mayor o igual que 0 y menor el que número de filas)
     * @param f fila
     * @return true si es válida y false en caso contrario
     */
    private boolean filaValida(int f) {
        return 0 <= f && f < numFil;
    }

    /**
     * Indica si la columna es válida (mayor o igual que 0 y menor el que número de columnas)
     * @param c columna
     * @return true si es válida y false en caso contrario
     */
    private boolean columnaValida(int c) {
        return 0 <= c && c < numCol;
    }
  
    
    /**Metodo para saber si el tablero esta lleno*/
    public boolean estaLleno()
    {
    	boolean estaLleno=true;
    	
    	for(int i=0; i<getNumColumnas(); i++)
    	{
    		if(hayEspacio(i)!=-1)
    		{
    			estaLleno=false;
    		}
    	}
    	
    	return estaLleno;
    		
    	
    }
    
    
    public void restaura(int columnas, int filas)
    {
    
    	tablero[filas][columnas]=Casilla.LIBRE;
    }
    
            
}	

  