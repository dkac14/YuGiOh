import java.util.*;
public class Jugador {
    private String nombre;
    private int vida;
    private ArrayList<Carta> mazo;
    private ArrayList<Carta> mano;
    private Tablero tablero;
    private Scanner scanner = new Scanner(System.in);
    
    public Jugador(String nombre, ArrayList<Carta> mazo) {
        this.nombre = nombre;
        this.vida = 4000;
        this.mazo = mazo;
        this.mano = inicializar_Mano();
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

    public ArrayList<Carta> inicializar_Mano() {
        ArrayList<Carta> manoInicial = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (!mazo.isEmpty()) {
                manoInicial.add(mazo.remove(0));
            }
        }
        return manoInicial;
    }

    public void robar_Carta() {
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
            System.out.println("No tienes cartas en tu mano para jugar.");
            return;
        }

        StringBuilder resultado = new StringBuilder("Cartas en tu mano:\n");
        for (int i = 0; i < mano.size(); i++) {
            Carta carta = mano.get(i);
            if (carta instanceof CartaMonstruo) {
                CartaMonstruo monstruo = (CartaMonstruo) carta;
                resultado.append((i + 1) + ". " + monstruo.getNombre() + " (" + monstruo.getClass().getSimpleName() + ") - "+ monstruo.getDescripcion() + " (Ataque:" + monstruo.getAtaque() + " Defensa:" + monstruo.getDefensa() + ")\n");
            } else {
                resultado.append((i + 1) + ". " + carta.getNombre() + " (" + carta.getClass().getSimpleName() + ") - " + carta.getDescripcion() + "\n");
            }
        }

        int eleccion = -1;
        while (eleccion < 0 || eleccion >= mano.size()) {
            resultado.append("Selecciona el número de la carta que deseas jugar: ");
            if (scanner.hasNextInt()) {
                eleccion = scanner.nextInt() - 1;
            } else {
                resultado.append("Entrada no válida. Por favor, introduce un número.\n");
                scanner.next();
            }
        }

        Carta cartaSeleccionada = mano.get(eleccion);

        if (cartaSeleccionada instanceof CartaMonstruo) {
            CartaMonstruo monstruo = (CartaMonstruo) cartaSeleccionada;
            String modo = "";
            while (!(modo.equals("ataque") || modo.equals("defensa"))) {
                resultado.append("¿En qué modo quieres jugar la carta? (ataque/defensa): ");
                modo = scanner.next().toLowerCase();
                if (!(modo.equals("ataque") || modo.equals("defensa"))) {
                    resultado.append("Opción inválida. Por favor, ingresa 'ataque' o 'defensa'.\n");
                }
            }

            if (modo.equals("defensa")) {
                monstruo.cambiarModo();
            }

            if (tablero.agregarCartaMonstruo(monstruo)) {
                resultado.append("Jugaste " + monstruo.getNombre() + " en modo " + (monstruo.isEnAtaque() ? "ataque" : "defensa") + ".\n");
            }

        } else if (cartaSeleccionada instanceof CartaMagica) {
            CartaMagica magica = (CartaMagica) cartaSeleccionada;
            if (tablero.obtenerCartasMonstruo().isEmpty()) {
                System.out.println("No hay monstruos en el campo para aplicar esta carta mágica.");
                return;
            }

            boolean encontrada = false;
            for (CartaMonstruo monstruo : tablero.obtenerCartasMonstruo()) {
                if (monstruo.getTipoMonstruo().equals(magica.getTipo_monstruo())) {
                    if (magica.getTipo_incremento().equals("ataque")) {
                        magica.activar_carta(monstruo);
                    } else if (magica.getTipo_incremento().equals("defensa")) {
                        magica.activar_carta(monstruo);
                    }
                    tablero.agregarCartaMagicaOTrampa(magica);
                    resultado.append(monstruo.getNombre() + " equipado con " + magica.getNombre() + ".\n");
                    encontrada = true;
                }
            }

            if (!encontrada) {
                resultado.append("No hay monstruos de este tipo en el campo para aplicar esta carta mágica.\n");
            }

        } else if (cartaSeleccionada instanceof CartaTrampa) {
            tablero.agregarCartaMagicaOTrampa(cartaSeleccionada);
            resultado.append("Jugaste la carta trampa: " + cartaSeleccionada.getNombre() + " (boca abajo).\n");
        }

        mano.remove(cartaSeleccionada);
        System.out.println(resultado.toString());
    }
    public void declararBatalla(JugadorMaquina gamer) {
        if (this.tablero.getTurno() < 2) {
            System.out.println("No puedes declarar batalla hasta el segundo turno.");
            this.tablero.setTurno(this.tablero.getTurno() + 1);
            return;
        }
    
        ArrayList<CartaMonstruo> monstruosAtacantes = this.tablero.obtenerCartasMonstruo();
        ArrayList<CartaMonstruo> monstruosDefensores = gamer.getTablero().obtenerCartasMonstruo();
    
        if (monstruosAtacantes.isEmpty()) {
            System.out.println("No tienes monstruos en tu campo para declarar una batalla.");
            return;
        }
    
        Set<CartaMonstruo> monstruosYaUsados = new HashSet<>();
        Scanner scanner = new Scanner(System.in);
    
        while (monstruosAtacantes.size() > monstruosYaUsados.size()) {
            System.out.println("\nMonstruos disponibles para atacar:");
            for (int i = 0; i < monstruosAtacantes.size(); i++) {
                CartaMonstruo monstruo = monstruosAtacantes.get(i);
                if (!monstruosYaUsados.contains(monstruo)) {
                    System.out.println((i + 1) + ". " + monstruo);
                }
            }
    
            System.out.print("Selecciona el número del monstruo atacante (o 0 para salir): ");
            int eleccionAtacante = scanner.nextInt() - 1;
    
            if (eleccionAtacante == -1) {
                System.out.println("Terminaste la fase de batalla.");
                break;
            }
    
            if (eleccionAtacante < 0 || eleccionAtacante >= monstruosAtacantes.size()) {
                System.out.println("Selección inválida. Intenta nuevamente.");
                continue;
            }
    
            CartaMonstruo cartaAtacante = monstruosAtacantes.get(eleccionAtacante);
            if (monstruosYaUsados.contains(cartaAtacante)) {
                System.out.println(cartaAtacante.getNombre() + " ya atacó este turno.");
                continue;
            }
    
            System.out.println("\nOpciones de ataque:");
            System.out.println("1. Ataque directo al jugador.");
            System.out.println("2. Ataque a un monstruo defensor.");
            System.out.print("Elige el tipo de ataque: ");
            String tipoAtaque = scanner.next().trim();
    
            if (tipoAtaque.equals("1")) {  // Ataque directo
                if (!monstruosDefensores.isEmpty()) {
                    System.out.println("El oponente tiene monstruos en el campo.");
                    continue;
                } else {
                    System.out.println(cartaAtacante.getNombre() + " realiza un ataque directo.");
                    gamer.setVida(cartaAtacante.recibirAtaqueDirecto(gamer.getVida(), cartaAtacante.getAtaque(), this.getTablero()));
                }
    
            } else if (tipoAtaque.equals("2")) {  // Ataque a un monstruo defensor
                if (monstruosDefensores.isEmpty()) {
                    System.out.println("El oponente no tiene monstruos en el campo. Realiza un ataque directo.");
                    continue;
                }
    
                System.out.println("Monstruos defensores disponibles:");
                for (int i = 0; i < monstruosDefensores.size(); i++) {
                    System.out.println((i + 1) + ". " + monstruosDefensores.get(i));
                }
    
                System.out.print("Selecciona el número del monstruo defensor: ");
                int eleccionDefensor = scanner.nextInt() - 1;
    
                if (eleccionDefensor < 0 || eleccionDefensor >= monstruosDefensores.size()) {
                    System.out.println("Selección inválida. Intenta nuevamente.");
                    continue;
                }
    
                CartaMonstruo cartaDefensora = monstruosDefensores.get(eleccionDefensor);
                gamer.setVida(cartaAtacante.atacar(cartaDefensora, gamer.getVida(), this.getVida(), eleccionAtacante, eleccionDefensor, this.getTablero(), gamer.getTablero()));
            } else {
                System.out.println("Opción inválida. Intenta nuevamente.");
                continue;
            }
    
            monstruosYaUsados.add(cartaAtacante);
    
            System.out.print("¿Deseas continuar atacando? (si/no): ");
            String continuar = scanner.next().trim().toLowerCase();
            while (!continuar.equals("si") && !continuar.equals("no")) {
                System.out.println("Respuesta inválida. Por favor, responde con 'si' o 'no'.");
                continuar = scanner.next().trim().toLowerCase();
            }
    
            if (continuar.equals("no")) {
                System.out.println("Terminaste la fase de batalla.");
                break;
            }
        }
    }    
    
}
