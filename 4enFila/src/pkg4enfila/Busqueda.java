package pkg4enfila;

import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.applet.*;

// ==========================================================================
// Esta clase es la que se encarga de jugar
// ==========================================================================
class Busqueda {

  public final static int VACIO          =  0;

  public final static int COMP           =  1;

  public final static int JUGADOR        =  2;



  public final static int JUEGO_CONTINUA =  0;

  public final static int ALGUIEN_GANO   =  1;

  public final static int TABLERO_LLENO  = -1;



  // Guarda el tablero actual

  private int             tablero[]       = new int[7];



  // Profundidad maxima. Cambialo si quieres, pero que sea de 2 en 2

  private int             profundidadMaxima = 6;



  // Estado actual del juego (si ha ganado alguien, etc..)

  private int             estadoJuego;

  public  int             EstadoJuego() { return estadoJuego; }



  // ------------------------------------------------------------------------

  // Comprueba si una columna de un tablero cualquiera admite mas fichas

  // ------------------------------------------------------------------------

  private boolean _EstaVacio(int columna, int tab[]) {

    return (tab[columna]<=1023);

  }



  // ------------------------------------------------------------------------

  // Comprueba si una columna del tablero de juego admite mas fichas

  // ------------------------------------------------------------------------

    public boolean EstaVacio(int columna) {

    if(columna>=0 && columna<7)

      return _EstaVacio(columna,tablero);

    else

      return false;

  }



  // ------------------------------------------------------------------------

  // Devuelve el estado de ocupacion (ordenador, jugador o vacio) de una

  // casilla de un tablero

  // ------------------------------------------------------------------------

  private int _Casilla(int columna, int fila, int tab[]) {

    return (tab[columna]>>(fila*2))%4;

  }



  // ------------------------------------------------------------------------

  // Devuelve el estado de ocupacion de una casilla del tablero de juego

  // ------------------------------------------------------------------------

  public int Casilla(int columna, int fila) {

    return _Casilla(columna,fila,tablero);

  }



  // ------------------------------------------------------------------------

  // Mete una ficha del jugador dado en la casilla mas baja disponible en

  // la columna dada de un tablero

  // ------------------------------------------------------------------------

  private int _Mover(int jugador, int columna, int tab[])  {

    int fila;

    for (fila=0; fila<6; fila++)

      if (_Casilla(columna,fila,tab)==VACIO)

        break;

    tab[columna] += jugador<<(2*fila);

    return fila;

  }



  // ------------------------------------------------------------------------

  // Mete una ficha del jugador dado en la casilla mas baja disponible en

  // la columna dada del tablero de juego

  // ------------------------------------------------------------------------

  public int Mover(int jugador, int columna) {

    int fila = -1;

    if(EstaVacio(columna))

       fila = _Mover(jugador, columna, tablero);

    ExaminarJuego();

    return fila;

  }



  // ------------------------------------------------------------------------

  // Funcion de evaluacion. Basicamente se puede decir que suma 10 por cada

  // posible proyecto en el que coincidan ya 3 fichas y uno por cada uno de

  // 2 fichas. No esta muy depurado y por eso suma 20 (por ejemplo), en este

  // caso:

  //                 XXX <-Esa casilla esta libre, luego sumamos

  //                 OXO

  //                 XOO

  // cuando seria mas correcto que sumara 10. Sigue el mismo criterio para

  // restar puntos por los proyectos del adversario.

  // No cuenta ni los 4 en raya ni los posibles empates porque de eso se

  // encarga el Negamax con ayuda de una funcion especifica.

  // ------------------------------------------------------------------------

  private int Evaluar (int tab[]) {

    int columna,fila,i;

    int casilla, parcialComp, parcialJug, total=0;

    boolean salComp=false, salJug=false;

    for (columna=0 ; columna<4 ; columna++)

      for (fila=0 ; fila<6 ; fila++) {

        salComp = salJug = false;

        parcialComp = parcialJug = 0;

        for (i=0 ; i<4 ; i++) {

          casilla = _Casilla(columna+i,fila,tab);

          if (casilla==COMP) {

            parcialComp++;

            salJug=true; }

          if (casilla==JUGADOR) {

            parcialJug++;

            salComp=true; }

        }

        total += (!salComp && parcialComp>1) ? Math.pow(10,parcialComp-2) : 0;

        total -= (!salJug && parcialJug>1) ? Math.pow(10,parcialJug-2) : 0;

        if (fila<3) {

          salComp = salJug = false;

          parcialComp = parcialJug = 0;

          for (i=0 ; i<4 ; i++) {

            casilla = _Casilla(columna+i,fila+i,tab);

            if (casilla==COMP) {

              parcialComp++;

              salJug=true; }

            if (casilla==JUGADOR) {

              parcialJug++;

              salComp=true; }

          }

          total += (!salComp && parcialComp>1) ? Math.pow(10,parcialComp-2) : 0;

          total -= (!salJug && parcialJug>1) ? Math.pow(10,parcialJug-2) : 0;

          salComp = salJug = false;

          parcialComp = parcialJug = 0;

          for (i=0 ; i<4 ; i++) {

            casilla = _Casilla(columna,fila+i,tab);

            if (casilla==COMP) {

              parcialComp++;

              salJug=true; }

            if (casilla==JUGADOR) {

              parcialJug++;

              salComp=true; }

          }

          total += (!salComp && parcialComp>1) ? Math.pow(10,parcialComp-2) : 0;

          total -= (!salJug && parcialJug>1) ? Math.pow(10,parcialJug-2) : 0;

        }

        else {

          salComp = salJug = false;

          parcialComp = parcialJug = 0;

          for (i=0 ; i<4 ; i++) {

            casilla = _Casilla(columna+i,fila-i,tab);

            if (casilla==COMP) {

              parcialComp++;

              salJug=true; }

            if (casilla==JUGADOR) {

              parcialJug++;

              salComp=true; }

          }

          total += (!salComp && parcialComp>1) ? Math.pow(10,parcialComp-2) : 0;

          total -= (!salJug && parcialJug>1) ? Math.pow(10,parcialJug-2) : 0;

        }

      }

    return total;

  }



  // ------------------------------------------------------------------------

  // Negamax con poda alfabeta: Espero que funcione ;-). Aqui se incluye solo

  // el algoritmo a partir del segundo nivel, para asi ocultar su

  // funcionamiento interno a la clase externa y no tener ifs por todos lados

  // debido a que, mientras normalmente el negamax debe devolver la

  // maxima puntuacion obtenida, el nodo raiz debe devolver el numero del

  // nodo que le da la maxima puntuacion.

  // A cada nodo que genera lo primero que comprueba es si corresponde a un

  // final de la partida, devolviendo el valor que corresponda (-100.000 en

  // caso de que gane el jugador, 100.000 en caso que gane la maquina y

  // 0 en caso de empate). Para averiguarlo llama al metodo ExaminarJuego.

  // ------------------------------------------------------------------------

  private int Negamax(int tab[], int profundidad, int alfa, int beta) {

    int nodo, valor_escogido, posible_valor, i, jugador;

    int tab_aux[] = new int[7];

    jugador = (profundidad%2==0) ? COMP: JUGADOR;

    if (profundidad==1)

      return -Evaluar(tab);

    valor_escogido = alfa;

    for (nodo=0; nodo<7; nodo++) {

      for (i=0 ; i<7 ; i++)

        tab_aux[i]=tab[i];

      if (_EstaVacio(nodo,tab_aux)) {

        _Mover(jugador,nodo,tab_aux);

        switch (_ExaminarJuego(tab_aux)) {

          case TABLERO_LLENO:

              posible_valor=0;

              break;

          case COMP:

              if (jugador==COMP)

                posible_valor=100000;

              else

                posible_valor=-100000;

              break;

          case JUGADOR:

              if (jugador==JUGADOR)

                posible_valor=100000;

              else

                posible_valor=-100000;

              break;

          case JUEGO_CONTINUA:

          default:

            posible_valor = -Negamax(tab_aux,profundidad-1,-beta,-valor_escogido);

        }

        if (posible_valor>valor_escogido)

           valor_escogido=posible_valor;

        if (valor_escogido>=beta)

           return valor_escogido;

      }

    }

    return valor_escogido;

  }



  // ------------------------------------------------------------------------

  // Metodo encargado de inicializar el Negamax

  // ------------------------------------------------------------------------

  public int MuevoYo(int valor[]) {

    int nodo, valor_escogido, posible_valor, nodo_escogido, i, jugador;

    int beta;

    int tab_aux[] = new int[7];

    posible_valor = valor_escogido = Integer.MIN_VALUE+10;

    beta = Integer.MAX_VALUE-10;

    nodo_escogido = -1;

    for (nodo=0; nodo<7; nodo++) {

      for (i=0 ; i<7 ; i++)

        tab_aux[i]=tablero[i];

      if (_EstaVacio(nodo,tab_aux)) {

        _Mover(COMP,nodo,tab_aux);

        switch (_ExaminarJuego(tab_aux)) {

          case TABLERO_LLENO:

              posible_valor=0;

              break;

          case COMP:

              return nodo;

          case JUGADOR:

              posible_valor=-100000;

              break;

          case JUEGO_CONTINUA:

          default:

            posible_valor = -Negamax(tab_aux,profundidadMaxima-1,-beta,-valor_escogido);

        }

        if (posible_valor>valor_escogido) {

          nodo_escogido=nodo;

          valor_escogido=posible_valor;

        }

      }

    }

    valor[0]=valor_escogido;

    return nodo_escogido;

  }



  // ------------------------------------------------------------------------

  // Examina un tablero para ver si el mismo refleja ua partida ya terminada

  // y especifica como ha terminado.

  // ------------------------------------------------------------------------

  private int _ExaminarJuego(int tab[]) {

    int columna,fila,i,j,jugador,res;

    res=TABLERO_LLENO;



    // en este bucle comprobamos que el tablero no esta lleno

    for (columna=0 ; columna<7 ; columna++)

      if (_EstaVacio(columna,tab))

        res=JUEGO_CONTINUA;



    // ahora comprueba si alguien ha hecho un 4 en Raya vertical o

    // diagonal

    for (columna=0 ; (columna<7 && res==JUEGO_CONTINUA); columna++)

      for (fila=0 ; (fila<3 && res==JUEGO_CONTINUA) ; fila++) {

        jugador=_Casilla(columna,fila,tab);

        if (jugador!=VACIO) {

          res=jugador;

          for (i=1 ; i<4 ; i++)

            res = (_Casilla(columna,fila+i,tab)==jugador) ? res : JUEGO_CONTINUA;

        }

        if (jugador!=VACIO && res==JUEGO_CONTINUA && columna<4) {

          res=jugador;

          for (i=1 ; i<4 ; i++)

            res = (_Casilla(columna+i,fila+i,tab)==jugador) ? res : JUEGO_CONTINUA;

        }

        if (jugador!=VACIO && res==JUEGO_CONTINUA && columna>2) {

          res=jugador;

          for (i=1 ; i<4 ; i++)

            res = (_Casilla(columna-i,fila+i,tab)==jugador) ? res : JUEGO_CONTINUA;

        }

      }



    // ahora comprueba si alguien ha hecho un 4 en Raya horizontal

    for (fila=0 ; (fila<6 && res==JUEGO_CONTINUA); fila++)

      for (columna=0 ; (columna<4 && res==JUEGO_CONTINUA) ; columna++) {

        jugador=_Casilla(columna,fila,tab);

        if (jugador!=VACIO) {

          res=jugador;

          for (j=columna+1 ; j<columna+4 ; j++)

            res = (_Casilla(j,fila,tab)==jugador) ? res : JUEGO_CONTINUA;

        }

      }



    return res;

  }



  // ------------------------------------------------------------------------

  // Examina el tablero de juego para ver si este ha finalizado y como lo

  // ha hecho.

  // ------------------------------------------------------------------------

  public void ExaminarJuego() {

    int res=_ExaminarJuego(tablero);

    estadoJuego = (res==COMP || res==JUGADOR) ? ALGUIEN_GANO : res;

  }



  // ------------------------------------------------------------------------

  // Constructor de la clase, pone en blanco el tablero.

  // ------------------------------------------------------------------------

  public Busqueda() {

    int i;

    for(i=0 ; i<7 ; i++)

      tablero[i] = 0;

  }



};
