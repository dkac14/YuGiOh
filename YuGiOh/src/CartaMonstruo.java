import java.util.ArrayList;
import java.util.List;

public class CartaMonstruo extends Carta {
    private int ataque;
    private int defensa;
    private String tipoMonstruo;
    private String elemento;
    private boolean enAtaque;

    public CartaMonstruo(String nombre, String descripcion, int ataque, int defensa, String tipoMonstruo, String elemento, boolean modo) {
        super(nombre, descripcion);
        this.ataque = ataque;
        this.defensa = defensa;
        this.tipoMonstruo = tipoMonstruo;
        this.elemento = elemento;
        this.enAtaque = modo;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public String getTipoMonstruo() {
        return tipoMonstruo;
    }

    public void setTipoMonstruo(String tipoMonstruo) {
        this.tipoMonstruo = tipoMonstruo;
    }

    public String getElemento() {
        return elemento;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }

    public boolean isEnAtaque() {
        return enAtaque;
    }

    public void setEnAtaque(boolean enAtaque) {
        this.enAtaque = enAtaque;
    }

    // Métodos principales
    public int atacar(CartaMonstruo cartaEnemigo, int puntosVidaOponente, int puntosVidaAtacante, int eleccionAtacantes, int eleccionDefensor, Tablero tableroAtk, Tablero tableroDef) {
        // Ataque directo
        if (cartaEnemigo == null) {
            puntosVidaOponente -= this.ataque;
            System.out.println("Ataque directo: " + this.getNombre() + " inflige " + this.ataque + " puntos de daño.");
            return puntosVidaOponente;
        }

        // Verifica si la carta enemiga es válida
        if (!(cartaEnemigo instanceof CartaMonstruo)) {
            System.out.println(this.getNombre() + " no puede atacar a la carta, ya que no es un monstruo.");
            return puntosVidaOponente;
        }

        System.out.println(this.getNombre() + " declara un ataque a " + cartaEnemigo.getNombre() + ".");

        // Verificar cartas trampa
        ArrayList<CartaTrampa> trampas = tableroDef.obtenerCartasTrampa();
        for (int i = 0; i < trampas.size(); i++) {
            CartaTrampa cartaTrampa = trampas.get(i);
            if (cartaTrampa.verificar(this)) {
                cartaTrampa.activar(cartaEnemigo, trampas, tableroDef, i);
                return puntosVidaOponente;
            }
        }

        // Continuar con el ataque si no hay trampas activadas
        if (this.enAtaque) {
            if (cartaEnemigo.enAtaque) {
                // Ataque vs. Ataque
                if (this.ataque > cartaEnemigo.ataque) {
                    System.out.println(this.getNombre() + " destruye a " + cartaEnemigo.getNombre() + ".");
                    cartaEnemigo.destruir(tableroDef.obtenerCartasMonstruo());
                    tableroDef.eliminarCartaMonstruo(eleccionDefensor);
                    puntosVidaOponente -= (this.ataque - cartaEnemigo.ataque);

                    gestionarCartasMagicas(tableroDef, cartaEnemigo);

                } else if (this.ataque < cartaEnemigo.ataque) {
                    System.out.println(this.getNombre() + " es destruido por " + cartaEnemigo.getNombre() + ".");
                    this.destruir(tableroAtk.obtenerCartasMonstruo());
                    tableroAtk.eliminarCartaMonstruo(eleccionAtacantes);
                    puntosVidaAtacante -= (cartaEnemigo.ataque - this.ataque);

                    gestionarCartasMagicas(tableroAtk, this);
                    return puntosVidaAtacante;

                } else {
                    System.out.println("El ataque termina en empate. Los 2 monstruos son destruidos.");
                    cartaEnemigo.destruir(tableroDef.obtenerCartasMonstruo());
                    tableroDef.eliminarCartaMonstruo(eleccionDefensor);
                    this.destruir(tableroAtk.obtenerCartasMonstruo());
                    tableroAtk.eliminarCartaMonstruo(eleccionAtacantes);

                    gestionarCartasMagicas(tableroDef, cartaEnemigo);
                    gestionarCartasMagicas(tableroAtk, this);
                }

            } else {
                // Ataque vs. Defensa
                if (this.ataque > cartaEnemigo.defensa) {
                    System.out.println(this.getNombre() + " destruye a " + cartaEnemigo.getNombre() + " en modo defensa.");
                    cartaEnemigo.destruir(tableroDef.obtenerCartasMonstruo());
                    tableroDef.eliminarCartaMonstruo(eleccionDefensor);

                    gestionarCartasMagicas(tableroDef, cartaEnemigo);

                } else if (this.ataque < cartaEnemigo.defensa) {
                    int dano = cartaEnemigo.defensa - this.ataque;
                    puntosVidaAtacante -= dano;
                    System.out.println(this.getNombre() + " no logra superar la defensa de " + cartaEnemigo.getNombre() + ". " +
                            "El jugador pierde " + dano + " puntos de vida.");
                    return puntosVidaAtacante;
                }
            }
        } else {
            System.out.println(this.getNombre() + " no puede atacar porque está en modo defensa.");
        }

        return puntosVidaOponente;
    }

    public void destruir(List<CartaMonstruo> listaCartas) {
        if (listaCartas.contains(this)) {
            listaCartas.remove(this);
            System.out.println(this.getNombre() + " ha sido destruido y enviado al cementerio.");
        }
    }

    public void cambiarModo() {
        this.enAtaque = !this.enAtaque;
        String modal = this.enAtaque ? "ataque" : "defensa";
        System.out.println(this.getNombre() + " ahora está en modo " + modal + ".");
    }

    public Integer recibirAtaqueDirecto(int puntosVidaJugador, int ataque, Tablero tableroDef) {
        ArrayList<CartaTrampa> trampas = tableroDef.obtenerCartasTrampa();
        for (int i = 0; i < trampas.size(); i++) {
            CartaTrampa cartaTrampa = trampas.get(i);
            if (cartaTrampa.verificar(this)) {
                cartaTrampa.activar(this, trampas, tableroDef, i);
                return null;
            }
        }

        puntosVidaJugador -= ataque;
        System.out.println(this.getNombre() + " ataca de manera directa. El jugador pierde " + ataque + " puntos de vida.");
        return puntosVidaJugador;
    }

    private void gestionarCartasMagicas(Tablero tablero, CartaMonstruo carta) {
        ArrayList<CartaMagica> magicas = tablero.obtenerCartasMagicas();
        for (int i = 0; i < magicas.size(); i++) {
            CartaMagica cartaMagica = magicas.get(i);
            if (cartaMagica.getEquipada_a() == carta) {
                cartaMagica.destruir(magicas, tablero.obtenerCartasMonstruo());
                tablero.eliminarCartaMagicaTrampa(i);
            }
        }
    }

    @Override
    public String toString() {
        String modo = this.enAtaque ? "Ataque" : "Defensa";
        return this.getNombre() + " (" + this.tipoMonstruo + "/" + this.elemento + ") - ATK: " + this.ataque + ", DEF: " + this.defensa;
    }

    @Override
    public String getNombre() {
        return super.getNombre();
    }
}
