package cuatroraya;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;

public class CuatroRaya extends Frame implements MouseListener, ActionListener {

    JButton Comienza = new JButton("Nuevo Juego");
    JButton Modo = new JButton("Dos Jugadores");
    JButton Salir = new JButton("Salir");
    JLabel Quien = new JLabel("Jugador 1");
    JLabel Victorias = new JLabel("Victorias: 0");
    JLabel Derrotas = new JLabel("Derrotas: 0");
    JLabel Jugadas = new JLabel("Jugadas: 0");
    int[][] tablero = new int[7][7];
    int Turno;
    int victorias;
    int derrotas;
    boolean jugando;
    boolean unjugador;
    Panel PanelPrincipal = new Panel();

    CuatroRaya() {
        super("4 en Raya");
        setLayout(new GridLayout(20, 5));
        addMouseListener(this);
        Comienza.addMouseListener(this);
        Salir.addMouseListener(this);
        Modo.addActionListener(this);
        PanelPrincipal.add(Quien);
        PanelPrincipal.add(Victorias);
        PanelPrincipal.add(Derrotas);
        PanelPrincipal.add(Jugadas);
        PanelPrincipal.add(Comienza);
        PanelPrincipal.add(Modo);
        PanelPrincipal.add(Salir);
        add(PanelPrincipal);
        victorias = 0;
        derrotas = 0;
        jugando = false;
        unjugador = true;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }

    public void PintaCasilla(int x) {
        Graphics g = getGraphics();
        int Resultados[][] = new int[3][3];
        int result;
        int z = 0;
        int y;
        
        for (z = 0; z < 7; z++) {
            if (tablero[x][z] != 0) {
                break;
            }
        }
        
        y = z == 0 ? 7 : (z - 1);

        if ((y == 6 && tablero[x][y] == 0) || ((y < 6) && (tablero[x][y + 1] != 0) && (tablero[x][y] == 0))) {
            if (Turno % 2 == 0) {
                g.setColor(Color.red);
                tablero[x][y] = -1;
                Quien.setText(unjugador ? "PC" : "Jugador 2");
            } else {
                g.setColor(Color.YELLOW);
                tablero[x][y] = 1;
                Quien.setText("Jugador 1");
            }
            g.fillOval((x * 90) + 20, (y * 80) + 100, 70, 70);
            Turno++;
            Jugadas.setText("Jugadas: " + Integer.toString(Turno));

            if (CompruebaCasilla(x, y, Resultados)) {
                Ganaste(tablero[x][y]);
            } else {
                if (Turno % 2 != 0) {
                    if (unjugador) {
                        result = JuegaIA(0);
                        if (result >= 10) {
                            PintaCasilla((result / 10) - 1);
                        } else {
                            PintaCasilla(result);
                        }
                    }
                }
            }
        }
    }
    
    public boolean CompruebaCasilla(int posx, int posy, int cuantas[][]) {
        int x, y;
        for (x = (-1); x < 2; x++) {
            for (y = (-1); y < 2; y++) {
                if (x != 0 || y != 0) {
                    cuantas[x + 1][y + 1] = CompruebaLinea(posx, posy, x, y);
                    if (cuantas[x + 1][y + 1] >= 4) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int CompruebaLinea(int posx, int posy, int x, int y) {
        int tempx, tempy;
        int cuantas = 1;
        int jugador = tablero[posx][posy];
        tempx = posx + x;
        tempy = posy + y;
        while ((tempy >= 0 && tempx >= 0) && (tempx < 7 && tempy < 7)) {
            if (tablero[tempx][tempy] == jugador) {
                cuantas++;
                tempx += x;
                tempy += y;
            } else {
                tempx = (-1);
            }
        }
        tempx = posx - x;
        tempy = posy - y;
        while ((tempy >= 0 && tempx >= 0) && (tempx < 7 && tempy < 7)) {
            if (tablero[tempx][tempy] == jugador) {
                cuantas++;
                tempx -= x;
                tempy -= y;
            } else {
                tempx = (-1);
            }
        }
        return cuantas;
    }

    //* *************************LÓGICA PC**************************
    public int JuegaIA(int Nveces) {
        int ValoresEl[][] = new int[3][3];
        int ValoresYo[][] = new int[3][3];
        int PuntosCasilla[] = new int[7];
        int x, y, casilla;
        int z;
        boolean mayor;
        
        //Recorreré cada casilla en busca de la mejor opción
        for (casilla = 0; casilla < 7; casilla++) {
            //calculo la y actual para cada casilla
            z = 0;
            while (tablero[casilla][z] == 0) {
                z++;
                if (z == 7) {
                    break;
                }
            }
            if (z == 0) {
                y = 6;
            } else {
                y = z - 1;
                
                //Una casilla recibe puntos por altura, un factor semialeatorio, para evitar
                //bucles infinitos
                PuntosCasilla[casilla] += y;
                //otro factor aleatorio	para evitar atascos
                PuntosCasilla[casilla] += Nveces % (casilla + 1);

                //Obtengo mis valores para esa jugada simulando la colocación de una ficha
                tablero[casilla][y] = 1;
                CompruebaCasilla(casilla, y, ValoresYo);

                tablero[casilla][y] = -1;
                //Obtengo los resultados que el tendría con esa jugada simulando
                CompruebaCasilla(casilla, y, ValoresEl);
                tablero[casilla][y] = 0;

                //valoro mucho si yo gano(1000) o si el gana(100)
                for (x = 0; x < 3; x++) {
                    for (z = 0; z < 3; z++) {
                        if (ValoresYo[x][z] >= 4) {
                            PuntosCasilla[casilla] += 1000;
                        }
                        if (ValoresEl[x][z] >= 4) {
                            PuntosCasilla[casilla] += 100;
                        }
                        if (ValoresYo[x][z] == 3 && Nveces < 3) {
                            tablero[casilla][y] = 1;

                            if (JuegaIA(Nveces + 1) > 10) {
                                PuntosCasilla[casilla] += 90;
                            } else {
                                PuntosCasilla[casilla] += 5;
                            }

                            tablero[casilla][y] = 0;
                        }
                        if (ValoresEl[x][z] == 3 && Nveces < 3) {
                            tablero[casilla][y] = (-1);
                            
                            if (JuegaIA(Nveces + 1) > 10) {
                                PuntosCasilla[casilla] += 100;
                            } else {
                                PuntosCasilla[casilla] += 10;
                            }
                            tablero[casilla][y] = 0;
                        }
                        if (ValoresYo[x][z] == 2) {
                            PuntosCasilla[casilla] += 5;
                        }
                    }
                }
                
                if (y > 0) {
                    tablero[casilla][y - 1] = (1);
                    CompruebaCasilla(casilla, y - 1, ValoresYo);

                    tablero[casilla][y - 1] = (-1);
                    CompruebaCasilla(casilla, y - 1, ValoresEl);
                    tablero[casilla][y - 1] = 0;
                }
                
                for (x = 0; x < 3; x++) {
                    for (z = 0; z < 3; z++) {
                        if (ValoresYo[x][z] >= 4) {
                            PuntosCasilla[casilla] -= 10;
                        }

                        if (ValoresEl[x][z] >= 4) {
                            PuntosCasilla[casilla] -= 100;
                        }
                    }
                }
            }
        }
	
        //Tenemos los tanteos de cada casilla
        //así que vamos a ver cual tiene mayor puntuación y colocar en ella
        z = 0;
        for (y = 0; y < 7; y++) {
            mayor = true;
            for (x = 0; x < 7; x++) {
                if (PuntosCasilla[y] < PuntosCasilla[x]) {
                    mayor = false;
                }
            }
            if (mayor == true) {
                if (z != 0) {
                    z = y * 10;
                } else {
                    z = y;
                }
            }
        }
        System.out.println("" + Arrays.deepToString(ValoresEl));
        return (z);
    }
    //* *************************LÓGICA PC**************************
    
    public void Ganaste(int jugador) {
        String s;
        if (jugador == (-1)) {
            victorias++;
            if (unjugador) {
                s = "GANASTE JUGADOR 1";
                Victorias.setText("Victorias: " + Integer.toString(victorias));
                Derrotas.setText("Derrotas: " + Integer.toString(derrotas));
            } else {
                s = "GANASTE JUGADOR 1";
                Victorias.setText("Victorias J1: " + Integer.toString(victorias));
                Derrotas.setText("Victorias J2: " + Integer.toString(derrotas));
            }
        } else {
            derrotas++;
            if (unjugador) {
                s = "GANÓ LA PC";
                Victorias.setText("Victorias: " + Integer.toString(victorias));
                Derrotas.setText("Derrotas: " + Integer.toString(derrotas));
            } else {
                s = "GANASTE JUGADOR 2";
                Victorias.setText("Victorias J1: " + Integer.toString(victorias));
                Derrotas.setText("Victorias J2: " + Integer.toString(derrotas));
            }
        }

        CuadroDialogo dig = new CuadroDialogo(this, s);
        //g.setColor(Color.white);
        //g.setFont (new Font ("Impact",10,60));
        //g.drawString(100,333,s);

        jugando = false;
        dig.setLocation(300, 10);
        dig.setVisible(true);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void paint(Graphics g) {
        int x;
        int y;
        g.setColor(Color.lightGray);
        g.fillRect(1, 1, 750, 690);

        g.setColor(Color.white);
        g.fillRect(1, 1, 750, 55);

        for (x = 0; x < 7; x++) {
            for (y = 0; y < 7; y++) {
                g.fillOval((x * 90) + 20, (y * 80) + 100, 70, 70);
                tablero[x][y] = 0;
            }
        }
    }

    public void ReStart() {
        Graphics g = getGraphics();
        int x;
        int y;
        Turno = 0;
        jugando = true;
        g.setColor(Color.lightGray);
        g.fillRect(1, 1, 750, 690);

        g.setColor(Color.white);
        g.fillRect(1, 1, 750, 50);

        for (x = 0; x < 7; x++) {
            for (y = 0; y < 7; y++) {
                g.fillOval((x * 90) + 20, (y * 80) + 100, 70, 70);
                tablero[x][y] = 0;
            }
        }
    }

    public static void main(String[] Args) {
        CuatroRaya cuatroRaya = new CuatroRaya();
        cuatroRaya.pack();
        cuatroRaya.setSize(750, 690);
        cuatroRaya.setResizable(false);
        cuatroRaya.setVisible(true);
        cuatroRaya.ReStart();
        
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == Modo) {
            if ("Un Jugador".equals(Modo.getText())) {
                Victorias.setText("Victorias: " + Integer.toString(victorias));
                Derrotas.setText("Derrotas: " + Integer.toString(derrotas));
                Modo.setText("Dos Jugadores");
                unjugador = true;
            } else {
                Victorias.setText("Victorias J1: " + Integer.toString(victorias));
                Derrotas.setText("Victorias J2: " + Integer.toString(derrotas));
                Modo.setText("Un Jugador");
                unjugador = false;
            }
            this.ReStart();
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        int z;
        z = me.getX();
        
        if (jugando) {
            z = (z - 10) / 90;
            PintaCasilla(z);
        }
        if (me.getSource() == Salir) {
            System.exit(0);
        }

        if (me.getSource() == Comienza) {
            ReStart();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //Logic here
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //Logic here
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //Logic here
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        //Logic here
    }
}

class CuadroDialogo extends Dialog {

    Button aceptar = new Button("OK");
    Label texto = new Label();

    CuadroDialogo(Frame f, String s) {
        super(f, "informacion");
        setSize(400, 100);
        setResizable(false);
        setBackground(Color.white);
        texto.setText(s);
        add("North", texto);
        add("South", aceptar);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.drawString("paint", 50, 50);
    }

    @Override
    public boolean action(Event evt, Object obj) {
        if (evt.target == aceptar) {
            dispose();
            return true;
        }
        return false;
    }
}
