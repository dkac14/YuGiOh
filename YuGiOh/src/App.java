import java.util.ArrayList;

public class App {

    public static void main(String[] args) {
        // Crear una instancia de MazoCartas
        MazoCartas mazoCartas = new MazoCartas();

        // Cargar y seleccionar cartas desde el archivo
        ArrayList<Carta> mazoJugador1 = mazoCartas.seleccionarMazo("Cartas.txt");
        ArrayList<Carta> mazoJugador2 = mazoCartas.seleccionarMazo("Cartas.txt");

        // Crear jugadores
        Jugador jugador1 = new Jugador("Jugador 1", mazoJugador1);
        JugadorMaquina jugador2 = new JugadorMaquina("Jugador Máquina", mazoJugador2);

        // Inicializar turno
        int turno = 1;

        // Simular el juego
        while (jugador1.getVida() > 0 && jugador2.getVida() > 0 && !jugador1.getMazo().isEmpty() && !jugador2.getMazo().isEmpty()) {
            // Turno del Jugador 1
            mostrarSeparadorTurno(turno, jugador1.getNombre());
            faseTomarCarta(jugador1);
            fasePrincipal(jugador1);
            jugador1.getTablero().mostrarTablero2();
            jugador2.getTablero().mostrarTablero1(jugador1, jugador2);
            faseBatalla(jugador1, jugador2, turno);

            int vidaJugador1= jugador1.getTablero().getTurno();
            jugador1.setVida(vidaJugador1);
            int vidaJugador2=jugador2.getTablero().getTurno();
            jugador2.setVida(vidaJugador2);

            if (jugador2.getVida() <= 0) {
                break; // Termina el juego si el Jugador 2 pierde
            }

            // Turno del Jugador 2
            mostrarSeparadorTurno(turno + 1, jugador2.getNombre());
            faseTomarCarta(jugador2);
            fasePrincipal(jugador2);
            jugador1.getTablero().mostrarTablero2();
            jugador2.getTablero().mostrarTablero1(jugador1, jugador2);
            faseBatalla(jugador2, jugador1, turno + 1);

            vidaJugador1= jugador1.getTablero().getTurno();
            jugador1.setVida(vidaJugador1);
            vidaJugador2=jugador2.getTablero().getTurno();
            jugador2.setVida(vidaJugador2);

            if (jugador1.getVida() <= 0) {
                break; // Termina el juego si el Jugador 1 pierde
            }

            // Incrementar turno
            turno += 2;
        }

        // Mostrar el ganador
        System.out.println("\n" + "=".repeat(40));
        if (jugador1.getVida() > jugador2.getVida()) {
            System.out.println("¡" + jugador1.getNombre() + " ha ganado el duelo!");
        } else if (jugador2.getVida() > jugador1.getVida()) {
            System.out.println("¡" + jugador2.getNombre() + " ha ganado el duelo!");
        } else {
            System.out.println("¡El duelo ha terminado en empate!");
        }
        System.out.println("=".repeat(40));
    }

    private static void mostrarSeparadorTurno(int turno, String jugador) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("              TURNO " + turno + ": " + jugador);
        System.out.println("=".repeat(40) + "\n");
    }

    private static void faseTomarCarta(Jugador jugador) {
        System.out.println("[FASE: TOMAR CARTA]");
        jugador.robar_Carta();
        System.out.println(jugador.getNombre() + " roba una carta del mazo.\n");
    }

    private static void faseTomarCarta(JugadorMaquina jugador) {
        System.out.println("[FASE: TOMAR CARTA]");
        jugador.robarCarta();
        System.out.println(jugador.getNombre() + " roba una carta del mazo.\n");
    }

    private static void fasePrincipal(Jugador jugador) {
        System.out.println("[FASE: PRINCIPAL]");
        jugador.jugarCarta();
        System.out.println(jugador.getNombre() + " ha jugado una carta en su tablero.\n");
    }

    private static void fasePrincipal(JugadorMaquina jugador) {
        System.out.println("[FASE: PRINCIPAL]");
        jugador.jugarCarta();
        System.out.println(jugador.getNombre() + " ha jugado una carta en su tablero.\n");
    }

    private static void faseBatalla(JugadorMaquina jugadorAtacante, Jugador jugadorDefensor, int turno) {
        System.out.println("[FASE: BATALLA]");
        if (turno == 1) {
            System.out.println("No se puede declarar batalla en el primer turno.\n");
        } else {
            jugadorAtacante.declararBatalla(jugadorDefensor);
            System.out.println(jugadorAtacante.getNombre() + " declara batalla.\n");
        }
    }

    private static void faseBatalla(Jugador jugadorAtacante, JugadorMaquina jugadorDefensor, int turno) {
        System.out.println("[FASE: BATALLA]");
        if (turno == 1) {
            System.out.println("No se puede declarar batalla en el primer turno.\n");
        } else {
            jugadorAtacante.declararBatalla(jugadorDefensor);
            System.out.println(jugadorAtacante.getNombre() + " declara batalla.\n");
        }
    }
}
