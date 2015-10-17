package pkg4enfila;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Panel;

// ==========================================================================
// Esta clase es el applet encargado de interactuar con el usuario.
// Todas las cosas importantes del mismo son del applet de Sven Wiebus
// ==========================================================================
public class CuatroEnRaya extends Applet {

    public final static int ESPERANDO_QUE_MUEVAS = 0;
    public final static int PENSANDO = 1;
    public final static int VOLVER_A_EMPEZAR = 2;
    public final static int ANCHURA_BORDE = 5;
    Busqueda c4k;
    int filaActual;
    int valor[] = new int[1];
    int res = Busqueda.JUEGO_CONTINUA;
    int ultimaColumnaFlechaComp = -1;
    int ultimaColumnaFlechaJugador = -1;
    boolean ultimaFlechaJugadorVacia = false;
    int currFontSize = 0;
    Font font;
    StringBuffer estadoActual;
    Graphics g = null;
    Panel panel = null;
    int modo = -1;

    // ------------------------------------------------------------------------
    // Inicializa la clase, creando una instancia de Busqueda y poniendo a
    // su valor inicial diversas variables
    // ------------------------------------------------------------------------
    public void init() {
        c4k = new Busqueda();
        ultimaColumnaFlechaJugador = -1;
        ultimaColumnaFlechaComp  = -1;
        ultimaFlechaJugadorVacia  = true;
        estadoActual = new StringBuffer("Empieza a jugar");
        if(g == null) {
            g = getGraphics();   
        }
        PonerModo(ESPERANDO_QUE_MUEVAS);
    }

    // ------------------------------------------------------------------------
    // Especifica el momento que esta pasando en el juego
    // ------------------------------------------------------------------------
    public void PonerModo(int m) { 
        modo = m; 
    }

    // ------------------------------------------------------------------------
    // Consulta que esta pasando en el juego
    // ------------------------------------------------------------------------
    public boolean ConsultarModo(int m) { 
        return modo == m; 
    }

    // ------------------------------------------------------------------------
    // Escribe el texto de estado del juego
    // ------------------------------------------------------------------------
    public void EscribirEstado(String str) {
        Dimension d = size();
        int dx = (d.width - 2 * ANCHURA_BORDE) / 7;
        int dy = d.height / 8;
        int i;
        Font f;

        // clear status display area
        g.setColor(Color.white);
        g.fillRect(ANCHURA_BORDE, 7 * dy + 1, 7 * dx, dy - ANCHURA_BORDE - 1);

        if(currFontSize != dy) {
            currFontSize = dy;
            font = new Font("Dialog", 0, (dy * 5) / 10);
        }
        
        g.setFont(font);
        estadoActual = new StringBuffer(str);
        g.setColor(Color.black);
        g.drawString(estadoActual.toString(), ANCHURA_BORDE + 10, 7*dy+(dy*5)/8);
    }

    // ------------------------------------------------------------------------
    // Dibuja una ficha
    // ------------------------------------------------------------------------
    public void DibujarFicha(int c, int r) {
        Color color;
        Dimension d = size();
        int dx = (d.width - 2 * ANCHURA_BORDE) / 7;
        int dy = d.height / 8;
        int xk = ANCHURA_BORDE + dx * c + dx / 8;
        int yk = d.height - (r+2) * dy + dy / 8;
        int xs = (dx * 3) / 4;
        int ys = (dy * 3) / 4;

        switch(c4k.Casilla(c, r)) {
            case Busqueda.COMP:
                color = Color.green;
                break;
            case Busqueda.JUGADOR:
                color = Color.red;
                break;
            case Busqueda.VACIO:
            default:
                color = Color.white;
                break;
        }
        g.setColor(color);
        g.fillOval(xk, yk, xs, ys);
    }

    // ------------------------------------------------------------------------
    // paint es un metodo de applet que se encarga de poner en pantalla todo lo
    // que haya que poner
    // ------------------------------------------------------------------------
    public void paint(Graphics g)  {
        Dimension d  = size();
        int dx = (d.width - 2 * ANCHURA_BORDE) / 7;
        int dy = d.height / 8;
        int r, c;

        // limpiar
        g.setColor(Color.white);
        g.fillRect(0, 0, 7 * dx + 2 * ANCHURA_BORDE - 1, dy * 8 - 1);

        // dibujar
        g.setColor(Color.blue.darker().darker());
        g.fillRect(ANCHURA_BORDE, dy, dx * 7, dy * 6);

        for(r = 0 ; r < 6 ; r++) {
          for(c = 0 ; c < 7 ; c++) {
            DibujarFicha(c, r);
          }
        }

        EscribirEstado(estadoActual.toString());
        g.setColor(Color.gray);

        for(r=0 ; r < ANCHURA_BORDE ; r++) {
          g.drawLine(r, r, r, dy * 8 - r - 1);
          g.drawLine(r+1, r, dx * 7 + 2 * ANCHURA_BORDE - 1, r);
        }

        g.setColor(Color.darkGray);

        for(r=0 ; r < ANCHURA_BORDE; r++) {
          g.drawLine(dx * 7 - r + 2 * ANCHURA_BORDE - 1, r + 1, dx * 7 - r + 2 * ANCHURA_BORDE - 1, dy * 8 - r - 1);
          g.drawLine(r, dy * 8 - r - 1, dx * 7 + 2 * ANCHURA_BORDE - 1, dy * 8 - r - 1);
        }
    }

    // ------------------------------------------------------------------------
    // Llama al metodo Mover de Busqueda y dibuja la ficha y la flecha
    // correspondientes.
    // ------------------------------------------------------------------------
    public int Mover(int jugador, int columna) {
        int fila = c4k.Mover(jugador, columna);
        DibujarFicha(columna, fila);

        if(jugador == Busqueda.JUGADOR) {
          ultimaColumnaFlechaComp  = -1;
        } else {
          ultimaColumnaFlechaJugador = -1;
        }

        DibujarFlechas(columna, jugador);
        return fila;
    }

    // ------------------------------------------------------------------------
    // La funcion mas importante. Decide lo que sucede cuando el usuario deja
    // de pulsar el boton del raton. Contiene la secuencia de juego.
    // ------------------------------------------------------------------------
    public boolean mouseUp(Event evt, int x, int y) {
        int c;

        if(ConsultarModo(VOLVER_A_EMPEZAR)) {
          init();
          repaint();
          return true;
        }

        PonerModo(PENSANDO);
        juego:
        if(c4k.EstadoJuego() == Busqueda.JUEGO_CONTINUA) {
            // get columna by position of mouse click
            Dimension d = size();
            c = (( x - ANCHURA_BORDE) * 7) / d.width;
            if(c<0 || c>6) {
                break juego;
            }
            if(c4k.EstaVacio(c)) {
                filaActual = Mover(Busqueda.JUGADOR, c);
                switch(c4k.EstadoJuego()) {
                    case Busqueda.TABLERO_LLENO:     // Caso imposible si comienza el
                        EscribirEstado("Hemos empatado");  // jugador, incluido por si lo
                        PonerModo(VOLVER_A_EMPEZAR);              // cambio
                        break;
                    case Busqueda.ALGUIEN_GANO:
                        EscribirEstado("Ganaste!");
                        PonerModo(VOLVER_A_EMPEZAR);
                        break juego;
                    case Busqueda.JUEGO_CONTINUA:
                        res = c4k.MuevoYo(valor);
                        filaActual = Mover(Busqueda.COMP, res);
                        switch (c4k.EstadoJuego()) {
                            case Busqueda.TABLERO_LLENO:
                                EscribirEstado("Hemos empatado");
                                PonerModo(VOLVER_A_EMPEZAR);
                                break;
                            case Busqueda.ALGUIEN_GANO:
                                EscribirEstado("Gane!");
                                PonerModo(VOLVER_A_EMPEZAR);
                                break;
                            case Busqueda.JUEGO_CONTINUA:
                                switch(valor[0]) {
                                    case 100000:
                                        EscribirEstado("Te voy a aplastar");
                                        break;
                                    case -100000:
                                        EscribirEstado("Eres una mala persona");
                                        break;
                                    default:
                                        EscribirEstado("Tu mueves");
                                }
                        }

                }

            }
        }

        if(ConsultarModo(PENSANDO)) {
          PonerModo(ESPERANDO_QUE_MUEVAS);
        }
        return true;
    }

    // ------------------------------------------------------------------------
    // Dibuja una flecha.
    // ------------------------------------------------------------------------
    void DibujarFlecha(Graphics g, int dx, int dy, int c, Color color, boolean fill) {
        int xa[] = new int[8];
        int ya[] = new int[8];

        g.setColor(color);
        xa[0] = ANCHURA_BORDE + c * dx + dx / 2;
        ya[0] = (dy * 3) / 4;
        xa[1] = xa[0] + dx / 5;
        ya[1] = ya[0] - dy / 4;
        xa[2] = xa[0] + dx / 10;
        ya[2] = ya[1];
        xa[3] = xa[2]; 
        ya[3] = ya[0] - dy / 2;
        xa[4] = xa[0] - dx / 10;  
        ya[4] = ya[3];
        xa[5] = xa[4];          
        ya[5] = ya[1];
        xa[6] = xa[0] - dx / 5;
        ya[6] = ya[1];
        xa[7] = xa[0]; 
        ya[7] = ya[0];

        if(fill) {
            g.fillPolygon(xa, ya, 8);
        } else {
            g.drawPolygon(xa, ya, 8);
        }
    }

    // ------------------------------------------------------------------------
    // Dibuja la flecha correspondiente a una columna y un jugador.
    // ------------------------------------------------------------------------
    void DibujarFlechas(int columna, int jugador) {
        if(columna >= 0 && columna < 7) {
            boolean empty = c4k.EstaVacio(columna);
            Color color;
            Dimension d = size(); 
            int dx = (d.width - 2 * ANCHURA_BORDE) / 7;
            int dy = d.height / 8;

            // limpiar el area donde se va a dibujar la flecha
            g.setColor(Color.white);
            g.fillRect(ANCHURA_BORDE, ANCHURA_BORDE, 7*dx, dy-ANCHURA_BORDE);

            if (jugador==Busqueda.JUGADOR) {
                ultimaColumnaFlechaJugador = columna;
                ultimaFlechaJugadorVacia  = empty;
            } else if(jugador==Busqueda.COMP) {
                ultimaColumnaFlechaComp = columna;
            } else {
                ultimaColumnaFlechaJugador = -1;
                ultimaColumnaFlechaComp  = -1;
            }

            if(ultimaColumnaFlechaJugador != -1)  {
                if(ultimaFlechaJugadorVacia) {
                    color = Color.red;
                } else {
                    color = Color.gray;
                }
                DibujarFlecha(g, dx, dy, ultimaColumnaFlechaJugador, color, true);
            }

            if(ultimaColumnaFlechaComp != -1) {
                color = Color.green;
                DibujarFlecha(g, dx, dy, ultimaColumnaFlechaComp, color, ultimaColumnaFlechaComp != ultimaColumnaFlechaJugador);
            }
        }
    }
    // ------------------------------------------------------------------------
    // Evento que se dispara cuando el usuario mueve el raton para comprobar
    // si tiene que volver a dibujar las flechas.
    // ------------------------------------------------------------------------
    @Override
    public boolean mouseMove(Event evt, int x, int y) {
        if(ConsultarModo(ESPERANDO_QUE_MUEVAS)) {
            Dimension d = size();
            int       c;

            // examinar columna
            c = ((x-ANCHURA_BORDE) * 7) / d.width;
            if(c != ultimaColumnaFlechaJugador) {
                DibujarFlechas(c, Busqueda.JUGADOR);
            }
        }

        return true;
    }

    // ------------------------------------------------------------------------
    // Informacion del applet.
    // ------------------------------------------------------------------------
    @Override
    public String getAppletInfo() {
        return "Basado en un applet de Sven Wiebus";
    }
}