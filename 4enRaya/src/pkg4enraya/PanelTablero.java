package pkg4enraya;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

	/**Clase que se encargara de dibujar el panel y asignar el color 
	 * correspondiente a las fichas*/
public class PanelTablero extends JPanel
{
	//Casillas con su estado, libre u ocupado
	Conecta4 con;
	
				
	//Los colores que indentificaran a cada jugador
	Color colorJugador1;
	Color colorJugador2;
	Color colorTablero;
	
	int filas;
	int columnas;
	
	
		
	//Asignamos los colores
	public  PanelTablero(Color j1, Color j2, Color tablero, Conecta4 con4)
	{
		//setBackground(Color.LIGHT_GRAY);
		colorJugador1=j1;
		colorJugador2=j2;
		colorTablero=tablero;
		con=con4;
		filas=con.getNumFilas();
		columnas=con.getNumColumnas();
		filas=(filas*48)+16;
		columnas=(columnas*48)+16;
		setPreferredSize(new Dimension(columnas,filas));
	}
	
	/**Metodo que actuliza un tablero ya creado con una nueva
	 * configuracion de juego
	 * @param conec
	 */
	public void ActualizaTablero(Conecta4 conec)
	{
		filas=conec.getNumFilas();
		columnas=conec.getNumColumnas();
		filas=(filas*48)+16;
		columnas=(columnas*48)+16;
		setPreferredSize(new Dimension(columnas,filas));
		con=conec;
	}
		
		
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(colorTablero);
		
		
		g.fillRect(0, 0, columnas, filas);
			
			
		//Margenes iniciales
		int medicionFilas=10;
		int medicionColumnas=10;
			
			
		//Pintamos o repintamos el tablero
		for (int f = 0; f < con.getNumFilas(); f++)
		{
							
			for (int c = 0; c < con.getNumColumnas(); c++)
	        {
				//Si esta libre
				if(con.getCasilla(c,f)==Casilla.LIBRE )
				{
					g.setColor(Color.white);
					g.fillOval(medicionFilas, medicionColumnas, 45, 45);//Se pinta de color blanco
					medicionFilas=medicionFilas+48;
						
				}else if(con.getCasilla(c, f)==Casilla.JUGADOR1)//Sino, comprobamos el jugador
				{
					g.setColor(colorJugador1);
					g.fillOval(medicionFilas, medicionColumnas, 45, 45);
					medicionFilas=medicionFilas+48;
					
				}else{
						
					g.setColor(colorJugador2);
					g.fillOval(medicionFilas, medicionColumnas, 45, 45);
					medicionFilas=medicionFilas+48;
				}
	        }
				
			medicionFilas=10;//Nos ponemos en la primera columna de nuevo
			medicionColumnas=medicionColumnas+48;//Pasamos de fila
	    			
		}
	}
	
	/**Metodo que calcula la columna pasandole la posicion en la 
	 * que se ha hecho el click
	 * @param columna
	 */
	public int calculaColumnaYInserta(int columna)
	{
		int puntero=10;//Posicion inicial
		int columnaAInsertar=0;//Contador de columnas recorridas
		while(puntero<columna)//Mientras no hayamos pasado o estemos en la columna deseada
		{
			puntero=puntero+48;//INcrementamos el puntero
			
			if(puntero<columna)//Solo si sigue estando por detras de la posicion
				columnaAInsertar++;//Incrementamos la columna
		}
		
		return columnaAInsertar;
		
	}

}