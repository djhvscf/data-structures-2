package pkg4enraya;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;


public class Interfaz extends JFrame
{
	//Atributos
	Conecta4 con= new Conecta4(8,8);
	PanelTablero tab = new PanelTablero(Color.RED, Color.YELLOW, Color.BLUE, con);
	
	//Etiquetas
	JLabel title = new JLabel("CONECTA4");
	JLabel mov = new JLabel("Movimientos: ");
	
	//Boton
	JButton reiniciar = new JButton("Reiniciar");
	JButton configuracion = new JButton("Configuración");
	
	//Lista
	JList listaMov = new JList(new DefaultListModel());
	
	DialogoConfiguracion dc=new DialogoConfiguracion(this);
	boolean ganador=false;
	LinkedList<Movimiento> moves = new LinkedList<Movimiento>();
	int jugadasRealizadas=0;
	int partidaComenzada=0;
	
	
		
	//contrustor de la interfaz
	public Interfaz()
	{
		super("Conecta4");
					
		//Panel para el titulo
		JPanel titulo = new JPanel(new FlowLayout());
		titulo.add(title);
		//Panel para el boton
		GridLayout b = new GridLayout(0,2);
		JPanel boton = new JPanel(b);
		b.setHgap(10);
		JPanel boton1 = new JPanel(new FlowLayout());
		boton.add(reiniciar);
		boton.add(configuracion);
		boton1.add(boton);
		
		JPanel movimiento = new JPanel(new BorderLayout());////////<-
		PanelAux aux = new PanelAux(60);
		
		
		movimiento.add(aux, BorderLayout.NORTH);
		movimiento.add(mov, BorderLayout.CENTER);
				

		JScrollPane sl = new JScrollPane(listaMov,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		//Añadimos al conjunto
		add(sl, BorderLayout.EAST);
		add(movimiento, BorderLayout.CENTER);
		add(tab, BorderLayout.WEST);
		add(titulo, BorderLayout.NORTH);
		add(boton1, BorderLayout.SOUTH);
				
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		
		//Manejadores
		reiniciar.addActionListener(new Reiniciar());
		configuracion.addActionListener(new Configuracion());
		tab.addMouseListener(new SeleccionaColumna());
		listaMov.addMouseListener(new DobleClick());
		
	}
	
	
////////////////////////////////Metodos para el manejo de ls lista\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	/**Metodo que actuliza la lista que se muestra al usuario*/
	public void actualizaLista(LinkedList<Movimiento> l)
	{
		//DefaultListModel es la clase que gestiona la lista
		DefaultListModel lista=(DefaultListModel)listaMov.getModel();
		lista.clear();//La dejamos en blanco
		for(int i=0;i<moves.size();i++)
		{
			Movimiento p=moves.get(i);
			lista.addElement(p.toString());
		}
	}
	
	/**Metodo que reinicia la lista de elementos al completo*/
	public void borraLista()
	{
		for(int i=0; i<jugadasRealizadas; i++)
			moves.remove();
			
		jugadasRealizadas=0;	
		actualizaLista(moves);
		
	}
	
	/**Manejador del doble click sobre el historico
	 * de movimientos
	 * @author usuario
	 *
	 */
	public class DobleClick extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			if(e.getClickCount()==2)// si han sido dos pulsaciones...
			{
				int	pos=listaMov.locationToIndex(e.getPoint());//Coge la posicion sobre la que s eha pulsado
				int copia=pos;//Copiamos el indice
				//Como se supone que sobre la que se ha pulsado es la ultima que queremos dejar sobre
				//el tablero, incrementamos en uno la posicion
				pos++;
				Movimiento eliminar=moves.get(pos);//Cogemos el movimiento
				con.jugador=eliminar.jugador;//El turno del siguiente jugador despues de la restauracion
				
				
				while(pos!=moves.size())//Mientras queden fichas
				{
					eliminar=moves.get(pos);//Eliminamos
					con.restaura(eliminar.columna-1, eliminar.fila);//Borramos del conecta4
					moves.remove(pos);//La eliminamos del historico de movimientos
					
				}
				
				jugadasRealizadas=copia;//Resturamos el numero correcto de jugadas realizadas
				actualizaLista(moves);//Actulizamos el historico de movimientos
				repaint();//Actulizamos el tablero
				ganador=false;
				jugadasRealizadas++;
				
				
			}
		}
	}
	

	
///////////////////////////////Metodos para el manejo de los botones\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	/**Manejador del boton reiniciar, que pone todo a cero, movimientos y fichas*/
	public class Reiniciar implements ActionListener
	{
		
		public void actionPerformed(ActionEvent arg0) 
		{
			if(partidaComenzada == 1)//Si ya ha comenzado, se confirma que el usuario quiere
			{
				int respuestaReinicio = JOptionPane.showConfirmDialog(listaMov , " La partida ya ha comenzado,  ¿Deseas reiniciar la partida?", "ups!!!!!", JOptionPane.YES_NO_OPTION);
				//Se coge la respuesta
				
				if(respuestaReinicio == 0)//Si quiere
				{
					//Reiniciamos todo
					con.vacia();
					borraLista();
					con.jugador=1;
					repaint();
					respuestaReinicio=1;
					ganador=false;
					partidaComenzada=0;
					
					
				}
			}
			
		}
	}
	
	/**Manejador del boton configuracion*/
	public class Configuracion implements ActionListener
	{
		
		public void actionPerformed(ActionEvent arg0) 
		{
			dc.muestraOpciones();//Abrimos el dialogo
			Color [] colores=dc.devuelveConfiguraciones();//Cogemos los colores seleccionados
			int filas = dc.devuelveFilas();//cogemos las filas
			int columnas = dc.devuelveColumnas();//cogemos el numero de columnas


			if(filas!=0 && columnas!=0 && colores[0]!=null && colores[1]!=null && colores[2]!=null)
			{
				con = new Conecta4(columnas, filas);//Creamos un nuevo conecta4
				//Asignamos colores
				tab.colorJugador1=colores[1];
				tab.colorJugador2=colores[2];
				tab.colorTablero=colores[0];
				tab.ActualizaTablero(con);
				repaint();//Actulizamos el tablero
				borraLista();//Borramos el historico de movimientos
				
			}
					
			
		}
	}

	
////////////////////////////////////////Metodo para controlar el mouse\\\\\\\\\\\\\\\\\\\\\\\\\\\
	/**Clase manejadora de la pulsacion sobre el tablero 
	 * para insertar la ficha en la columna deseada
	 * @author usuario
	 *
	 */
	public class SeleccionaColumna extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			int respuesta=1;
			
			//Si se ha pulsado una vez y no hay ganador y tampoco hay empate
			if(e.getClickCount()==1 && ganador==false && con.estaLleno()==false)
			{
				Point p= e.getPoint();//cogemos el punto
				int columna=p.x;//cogemos la posicion de la columna en el panel
				int columnaCalculada=tab.calculaColumnaYInserta(columna);//calculamos la columna exacta
				jugadasRealizadas= jugadasRealizadas+1;//Aumentamos el numero de jugadas realizadas
				int turno=con.jugador;//cogemos el turno
				int filaInsertada =con.ponFicha(columnaCalculada);//ponemos la fihca
				Movimiento mov = new Movimiento(turno, columnaCalculada+1, filaInsertada);//Nuevo movimiento
				moves.addLast(mov);//añadimos al historico de movimientos
				partidaComenzada=1;//Partida comenzada
				actualizaLista(moves);//Actulizamos el historico con el ultimo movimiento
				repaint();//Repintamos el panel
				
								
				if (con.victoria(columnaCalculada, filaInsertada)==1)//Si despues del movimiento hya victoria
				{
					ganador=true;//HAY GANADOR
					try {
						Thread.sleep(1000);//paramos la ejecucion 1 segundo
					} catch (InterruptedException e1) {
						
						e1.printStackTrace();
					}
					respuesta = JOptionPane.showConfirmDialog(listaMov , " HAS GANADO!!!!!,  ¿Deseas empezar una nueva partida?", "HAS GANADO!!!!!", JOptionPane.YES_NO_OPTION);
					//Confirmamos si el jugador quiere reiniciar y probar suerte con otra partida
					
					
					if(respuesta==0)//Si quiere probar
					{
						
						//Reiniciamos todos
						con.vacia();
						con.jugador=1;
						repaint();
						respuesta=1;
						partidaComenzada=0;
						ganador=false;
						borraLista();
						
					}
				}
								
			}else{
				respuesta = JOptionPane.showConfirmDialog(listaMov , "Has ganado o habeis empatado,  ¿Deseas empezar una nueva partida?", "", JOptionPane.YES_NO_OPTION);
				//No se puede insertar mas, la partida ha acabado
				
				if(respuesta==0)
				{
					//Reiniciamos todo
					con.vacia();
					con.jugador=1;
					repaint();
					respuesta=1;
					borraLista();
					partidaComenzada=0;
					ganador=false;
				}
				
			}
			
		}
		
	}
	
	//Main
	public static void main (String[] args)
	{
		Interfaz inter = new Interfaz();
		
			
	}

}