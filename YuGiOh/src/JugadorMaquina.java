import java.util.*;

public class JugadorMaquina {
    private String nombre;
    private int vida;
    private ArrayList<Carta> mazo;
    private ArrayList<Carta> mano;
    private Tablero tablero;

    public JugadorMaquina(String nombre, ArrayList<Carta> mazo) {
        this.nombre = nombre;
        this.vida = 4000;
        this.mazo = mazo;
        this.mano = inicializarMano();
        this.tablero = new Tablero();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public ArrayList<Carta> getMazo() {
        return mazo;
    }

    public void setMazo(ArrayList<Carta> mazo) {
        this.mazo = mazo;
    }

    public ArrayList<Carta> getMano() {
        return mano;
    }

    public void setMano(ArrayList<Carta> mano) {
        this.mano = mano;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public ArrayList<Carta> inicializarMano() {
        ArrayList<Carta> manoInicial = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (!mazo.isEmpty()) {
                manoInicial.add(mazo.remove(0));
            }
        }
        return manoInicial;
    }

    public void robarCarta() {
        if (!mazo.isEmpty()) {
            Carta carta = mazo.remove(0);
            mano.add(carta);
            System.out.println(nombre + " robó la carta: " + carta.getNombre());
        } else {
            System.out.println(nombre + " no tiene más cartas en el mazo.");
        }
    }

    public void jugarCarta() {
        if (mano.isEmpty()) {
            System.out.println(nombre + " no tiene cartas en su mano para jugar.");
            return;
        }

        Random random = new Random();
        int index = random.nextInt(mano.size());
        Carta cartaSeleccionada = mano.get(index);

        if (cartaSeleccionada instanceof CartaMonstruo) {
            CartaMonstruo monstruo = (CartaMonstruo) cartaSeleccionada;
            if (random.nextBoolean()) {
                monstruo.cambiarModo();
            }

            if (tablero.agregarCartaMonstruo(monstruo)) {
                System.out.println(nombre + " jugó " + monstruo.getNombre() + " en modo " + (monstruo.isEnAtaque() ? "ataque" : "defensa") + ".");
            }

        } else if (cartaSeleccionada instanceof CartaMagica) {
            CartaMagica magica = (CartaMagica) cartaSeleccionada;

            if (tablero.obtenerCartasMonstruo().isEmpty()) {
                System.out.println("No hay monstruos en el campo para aplicar esta carta mágica.");
                return;
            }

            boolean aplicada = false;
            for (CartaMonstruo monstruo : tablero.obtenerCartasMonstruo()) {
                if (monstruo.getTipoMonstruo().equals(magica.getTipo_monstruo())) {
                    magica.activar_carta(monstruo);
                    tablero.agregarCartaMagicaOTrampa(magica);
                    System.out.println(monstruo.getNombre() + " equipado con " + magica.getNombre() + ".");
                    aplicada = true;
                    break;
                }
            }

            if (!aplicada) {
                System.out.println("No hay monstruos de este tipo en el campo para aplicar esta carta mágica.");
            }

        } else if (cartaSeleccionada instanceof CartaTrampa) {
            tablero.agregarCartaMagicaOTrampa(cartaSeleccionada);
            System.out.println(nombre + " jugó la carta trampa " + cartaSeleccionada.getNombre() + " (boca abajo).");
        }

        mano.remove(cartaSeleccionada);
    }

    public void declararBatalla(Jugador gamer) {
        if (this.tablero.getTurno() < 2) {
            System.out.println(this.nombre + " no puede declarar batalla hasta el segundo turno.");
            this.tablero.setTurno(this.tablero.getTurno() + 1);
            return;
        }

        ArrayList<CartaMonstruo> monstruosAtacantes = this.tablero.obtenerCartasMonstruo();
        ArrayList<CartaMonstruo> monstruosDefensores = gamer.getTablero().obtenerCartasMonstruo();

        if (monstruosAtacantes.isEmpty()) {
            System.out.println(this.nombre + " no tiene monstruos en su campo para declarar una batalla.");
            return;
        }

        Set<CartaMonstruo> monstruosYaUsados = new HashSet<>();
        Random random = new Random();

        while (monstruosAtacantes.size() > monstruosYaUsados.size()) {
            // Seleccionar monstruo atacante aleatoriamente que no haya atacado aún
            CartaMonstruo cartaAtacante = getMonstruoNoUsado(monstruosAtacantes, monstruosYaUsados, random);
            Integer gamer_vida=0;
            // Decidir tipo de ataque: directo (1) o a un defensor (2)
            if (!monstruosDefensores.isEmpty() && random.nextInt(2) == 1) {  // Ataque a defensor
                CartaMonstruo cartaDefensora = monstruosDefensores.get(random.nextInt(monstruosDefensores.size()));
                gamer_vida = cartaAtacante.atacar(cartaDefensora, gamer.getVida(), this.vida,
                        monstruosAtacantes.indexOf(cartaAtacante), monstruosDefensores.indexOf(cartaDefensora),
                        this.tablero, gamer.getTablero());
                        gamer.setVida(gamer_vida);
                System.out.println(this.nombre + " atacó a " + cartaDefensora.getNombre() + " con " + cartaAtacante.getNombre() + ".");
            } else {  // Ataque directo
                if (!monstruosDefensores.isEmpty()) {
                    System.out.println(this.nombre + " no puede realizar un ataque directo porque hay monstruos defensores.");
                } else {
                    gamer_vida = cartaAtacante.recibirAtaqueDirecto(gamer.getVida(), cartaAtacante.getAtaque(), gamer.getTablero());
                    gamer.setVida(gamer_vida);
                    System.out.println(this.nombre + " realizó un ataque directo con " + cartaAtacante.getNombre() + ".");
                }
            }

            // Marcar el monstruo como usado
            monstruosYaUsados.add(cartaAtacante);

            // Si todos los monstruos han atacado, termina la fase
            if (monstruosYaUsados.size() == monstruosAtacantes.size()) {
                System.out.println(this.nombre + " ha terminado la fase de batalla.");
                break;
            }
        }
    }

    private CartaMonstruo getMonstruoNoUsado(ArrayList<CartaMonstruo> monstruosAtacantes, Set<CartaMonstruo> monstruosYaUsados, Random random) {
        CartaMonstruo cartaAtacante;
        do {
            cartaAtacante = monstruosAtacantes.get(random.nextInt(monstruosAtacantes.size()));
        } while (monstruosYaUsados.contains(cartaAtacante));
        return cartaAtacante;
    }

}
