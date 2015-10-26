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

    public void PintarCelda(int x) {
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

            if (ComprobarCelda(x, y, Resultados)) {
                Ganador(tablero[x][y]);
            } else {
                if (Turno % 2 != 0) {
                    if (unjugador) {
                        result = JugarPC(0);
                        if (result >= 10) {
                            PintarCelda((result / 10) - 1);
                        } else {
                            PintarCelda(result);
                        }
                    }
                }
            }
        }
    }
    
    public boolean ComprobarCelda(int posx, int posy, int cuantas[][]) {
        int x, y;
        for (x = (-1); x < 2; x++) {
            for (y = (-1); y < 2; y++) {
                if (x != 0 || y != 0) {
                    cuantas[x + 1][y + 1] = ComprobarLinea(posx, posy, x, y);
                    if (cuantas[x + 1][y + 1] >= 4) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int ComprobarLinea(int posx, int posy, int x, int y) {
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
    public int JugarPC(int cantVeces) {
        int valoresEl[][] = new int[3][3];
        int valoresYo[][] = new int[3][3];
        int puntosPorCelda[] = new int[7];
        int x, y, celda;
        int z;
        boolean mayor;
        
        for (celda = 0; celda < 7; celda++) {
            z = 0;
            while (tablero[celda][z] == 0) {
                z++;
                if (z == 7) {
                    break;
                }
            }
            if (z == 0) {
                y = 6;
            } else {
                y = z - 1;
                
                puntosPorCelda[celda] += y;
                puntosPorCelda[celda] += cantVeces % (celda + 1);

                tablero[celda][y] = 1;
                ComprobarCelda(celda, y, valoresYo);

                tablero[celda][y] = -1;

                ComprobarCelda(celda, y, valoresEl);
                tablero[celda][y] = 0;

                for (x = 0; x < 3; x++) {
                    for (z = 0; z < 3; z++) {
                        if (valoresYo[x][z] >= 4) {
                            puntosPorCelda[celda] += 1000;
                        }
                        if (valoresEl[x][z] >= 4) {
                            puntosPorCelda[celda] += 100;
                        }
                        if (valoresYo[x][z] == 3 && cantVeces < 3) {
                            tablero[celda][y] = 1;

                            if (JugarPC(cantVeces + 1) > 10) {
                                puntosPorCelda[celda] += 90;
                            } else {
                                puntosPorCelda[celda] += 5;
                            }

                            tablero[celda][y] = 0;
                        }
                        if (valoresEl[x][z] == 3 && cantVeces < 3) {
                            tablero[celda][y] = (-1);
                            
                            if (JugarPC(cantVeces + 1) > 10) {
                                puntosPorCelda[celda] += 100;
                            } else {
                                puntosPorCelda[celda] += 10;
                            }
                            tablero[celda][y] = 0;
                        }
                        if (valoresYo[x][z] == 2) {
                            puntosPorCelda[celda] += 5;
                        }
                    }
                }
                
                if (y > 0) {
                    tablero[celda][y - 1] = (1);
                    ComprobarCelda(celda, y - 1, valoresYo);

                    tablero[celda][y - 1] = (-1);
                    ComprobarCelda(celda, y - 1, valoresEl);
                    tablero[celda][y - 1] = 0;
                }
                
                for (x = 0; x < 3; x++) {
                    for (z = 0; z < 3; z++) {
                        if (valoresYo[x][z] >= 4) {
                            puntosPorCelda[celda] -= 10;
                        }

                        if (valoresEl[x][z] >= 4) {
                            puntosPorCelda[celda] -= 100;
                        }
                    }
                }
            }
        }

        z = 0;
        for (y = 0; y < 7; y++) {
            mayor = true;
            for (x = 0; x < 7; x++) {
                if (puntosPorCelda[y] < puntosPorCelda[x]) {
                    mayor = false;
                }
            }
            if (mayor) {
                if (z != 0) {
                    z = y * 10;
                } else {
                    z = y;
                }
            }
        }
        System.out.println("" + Arrays.deepToString(valoresEl));
        return (z);
    }
    //* *************************LÓGICA PC**************************
    
    public void Ganador(int jugador) {
        String quienGana;
        if (jugador == (-1)) {
            victorias++;
            if (unjugador) {
                quienGana = "GANÓ JUGADOR 1";
                Victorias.setText("Victorias: " + Integer.toString(victorias));
                Derrotas.setText("Derrotas: " + Integer.toString(derrotas));
            } else {
                quienGana = "GANÓ JUGADOR 1";
                Victorias.setText("Victorias J1: " + Integer.toString(victorias));
                Derrotas.setText("Victorias J2: " + Integer.toString(derrotas));
            }
        } else {
            derrotas++;
            if (unjugador) {
                quienGana = "GANÓ LA PC";
                Victorias.setText("Victorias: " + Integer.toString(victorias));
                Derrotas.setText("Derrotas: " + Integer.toString(derrotas));
            } else {
                quienGana = "GANÓ JUGADOR 2";
                Victorias.setText("Victorias J1: " + Integer.toString(victorias));
                Derrotas.setText("Victorias J2: " + Integer.toString(derrotas));
            }
        }

        jugando = false;
        
        Ganador winner;
        winner = new Ganador(quienGana);
        
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

    public void Reiniciar() {
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
        cuatroRaya.Reiniciar();
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
            this.Reiniciar();
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        int z;
        z = me.getX();
        
        if (jugando) {
            z = (z - 10) / 90;
            PintarCelda(z);
        }
        if (me.getSource() == Salir) {
            System.exit(0);
        }

        if (me.getSource() == Comienza) {
            Reiniciar();
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