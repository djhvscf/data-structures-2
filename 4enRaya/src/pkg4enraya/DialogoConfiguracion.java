package pkg4enraya;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class DialogoConfiguracion extends JDialog
{
	JLabel fila = new JLabel("Filas:");
	JLabel columna = new JLabel("Columnas:");
	JLabel colorTab = new JLabel("Color tablero:");
	JLabel colorJugador1 = new JLabel("Color jugador1:");
	JLabel colorJugador2 = new JLabel("Color jugador2:");
	
	//Espacios de texto
	JTextField f = new JTextField("8");
	JTextField c = new JTextField("8");
	
	//Cajas de opciones
	String [] coloresTab1 = {"Azul", "Rojo", "Verde", "Amarillo", "Negro"};
	String [] coloresj1={"Rojo", "Azul", "Verde", "Amarillo", "Negro"};//
	String [] coloresj2 = {"Amarillo", "Rojo", "Verde", "Azul", "Negro"};// 
	
	
	//Colores
	Color [] coloresTablero = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.BLACK};
	Color [] coloresJugador1 = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.BLACK};
	Color [] coloresJugador2 = {Color.YELLOW, Color.RED, Color.GREEN, Color.BLUE, Color.BLACK};
	
	
	//Selector de colores
	JComboBox coloresTab = new JComboBox(coloresTab1);
	JComboBox coloresp1 = new JComboBox(coloresj1);
	JComboBox coloresp2 = new JComboBox(coloresj2);
	
	
	//Botones
	JButton aplicar = new JButton("Aplicar");
	JButton cancelar = new JButton("Cancelar");
	
	//Otros atributos
	PanelAux aux= new PanelAux(110);
	Color[] todosLosColores = new Color[3];
	int filas;
	int columnas;
	
	
	
	public DialogoConfiguracion(JFrame owner)
	{
		
		super(owner, "Seleccione la configuracion deseada", true);
		
		//Panel para las opciones fila y columna
		JPanel tamaño = new JPanel(new GridLayout(2,0));
		JPanel tamaño1 = new JPanel(new FlowLayout());
		JPanel tamaño2 = new JPanel(new BorderLayout());////////<-
		
		
		tamaño.add(fila);
		tamaño.add(f);
		tamaño.add(columna);
		tamaño.add(c);
		tamaño1.add(tamaño);
		tamaño2.add(aux, BorderLayout.NORTH);
		tamaño2.add(tamaño1, BorderLayout.CENTER);
				
		
		//Panel para las opciones de color de tablero
		JPanel colorTablero = new JPanel(new GridLayout(2,0));
		JPanel colorTablero1 = new JPanel(new FlowLayout());/////////<-
		colorTablero.add(colorTab);
		colorTablero.add(coloresTab);
		colorTablero1.add(colorTablero);
		
		
		
		//Panel para las opciones de color de J1
		JPanel colorj1 = new JPanel(new GridLayout(2,0));
		JPanel colorj11 = new JPanel(new FlowLayout());/////////<-
		colorj1.add(colorJugador1);
		//coloresj112.enable(false);
		colorj1.add(coloresp1);
		colorj11.add(colorj1);
		
		//Panel para las opciones de color de J2
		JPanel colorj2 = new JPanel(new GridLayout(2,0));
		JPanel colorj21 = new JPanel(new FlowLayout());/////////<-
		colorj2.add(colorJugador2);
		//coloresj122.enable(false);
		colorj2.add(coloresp2);
		colorj21.add(colorj2);
		
		
		//Añadimos las configuraciones de colores
		GridLayout conf = new GridLayout(0,1);
		JPanel configuraciones = new JPanel(conf);
		JPanel configuraciones1 = new JPanel (new FlowLayout());/////<-
		configuraciones.add(colorTablero1);
		configuraciones.add(colorj11);
		configuraciones.add(colorj21);
		configuraciones1.add(configuraciones);
		conf.setVgap(30);
		
		JPanel todo = new JPanel(new GridLayout(0,2));
		todo.add(tamaño2);
		todo.add(configuraciones1);
		
		//añadimos los botones
		GridLayout b = new GridLayout(0,2);
		JPanel boton = new JPanel(b);
		b.setHgap(10);
		JPanel boton1 = new JPanel(new FlowLayout());
		boton.add(aplicar);
		boton.add(cancelar);
		boton1.add(boton);
		
		//Metemos todo en un border
		JPanel todo1 = new JPanel(new BorderLayout());
		todo1.add(todo, BorderLayout.CENTER);
		todo1.add(boton1, BorderLayout.SOUTH);
		
		add(todo1);
		setResizable(false);
		pack();
		setVisible(false);
		
		//ActionListener de los botones
		aplicar.addActionListener(new Aplicar());
		cancelar.addActionListener(new Cancelar());
		
				
	}
	
	
	
	
/////////////Metodos que devuelven las configuraciones seleccionadas\\\\\\\\\\\\\\\\\\\\\\\\\
	public void muestraOpciones()
	{
		setVisible(true);
	}
	
	public Color [] devuelveConfiguraciones( )
	{
		return todosLosColores;
	}
	
	public int devuelveFilas()
	{
		return filas;
	}
	
	public int devuelveColumnas()
	{
		return columnas;
	}
	
	
	/**Manejador que controla el boton jugar y la configuracion del tablero*/
	public class Aplicar implements ActionListener
	{
		
		
		
		public void actionPerformed(ActionEvent evt)
		{
			//Cogemos los datos
			Color tab1 = coloresTablero[coloresTab.getSelectedIndex()];
			Color j1 = coloresJugador1[coloresp1.getSelectedIndex()];
			Color j2 = coloresJugador2[coloresp2.getSelectedIndex()];
			filas=Integer.parseInt(f.getText());
			columnas=Integer.parseInt(c.getText());
			System.out.println(j1);
			System.out.println(j2);
			
			
			
			
			//Si hay algun color repetido, lanzamos aviso
			if(j1 == tab1 )
			{
				JOptionPane.showMessageDialog(coloresp1 , "Color seleccionado como color del tablero", "Error", JOptionPane.ERROR_MESSAGE);
			}else if(j2 == tab1 || j1 == j2)
			{
				JOptionPane.showMessageDialog(coloresp2 , "Color seleccionado como color del tablero o del jugador1", "Error", JOptionPane.ERROR_MESSAGE);
			}else{//Sino 
				todosLosColores[0]=tab1;
			    todosLosColores[1]=j1;
			    todosLosColores[2]=j2;
			    devuelveConfiguraciones();
			    devuelveFilas();
			    devuelveColumnas();
			    
			    setVisible(false);
				                
			}
			
		}
		
	}
	
	//Manejador boton cancelar
	public class Cancelar implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			setVisible(false);
			
		}
	}
	

}
