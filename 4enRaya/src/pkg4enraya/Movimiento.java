package pkg4enraya;


public class Movimiento 
{
	int jugador;
	int fila;
	int columna;
	
	public Movimiento(int j, int col, int fil)
	{
		jugador=j;
		fila=fil;
		columna=col;
	}
	
	public String toString()
	{
		return "Jugador"+jugador+"  ->  Columna Insertada: "+columna;
	}

}

