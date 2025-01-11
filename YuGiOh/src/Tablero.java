import java.util.ArrayList;

public class Tablero {
    private CartaMonstruo[] zonaMonstruos; // Zona de monstruos con un tamaño fijo de 3.
    private Object[] zonaMagicaTrampa;    // Uso de Object para permitir CartaMagica o CartaTrampa.
    private int turno;

    public Tablero() {
        this.zonaMonstruos = new CartaMonstruo[3];
        this.zonaMagicaTrampa = new Object[3];
        this.turno = 1;
    }

    public CartaMonstruo[] getZonaMonstruos() {
        return zonaMonstruos;
    }

    public void setZonaMonstruos(CartaMonstruo[] zonaMonstruos) {
        this.zonaMonstruos = zonaMonstruos;
    }

    public Object[] getZonaMagicaTrampa() {
        return zonaMagicaTrampa;
    }

    public void setZonaMagicaTrampa(Object[] zonaMagicaTrampa) {
        this.zonaMagicaTrampa = zonaMagicaTrampa;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public boolean agregarCartaMonstruo(CartaMonstruo carta) {
        for (int i = 0; i < zonaMonstruos.length; i++) {
            if (zonaMonstruos[i] == null) {
                zonaMonstruos[i] = carta;
                String modo = carta.isEnAtaque() ? "ataque" : "defensa";
                System.out.println("Carta " + carta.getNombre() + " colocada en la zona de monstruos en modo " + modo + ".");
                return true;
            }
        }
        System.out.println("No hay espacio disponible en la zona de monstruos.");
        return false;
    }

    public boolean agregarCartaMagicaOTrampa(Object carta) {
        if (carta instanceof CartaMagica || carta instanceof CartaTrampa) {
            for (int i = 0; i < zonaMagicaTrampa.length; i++) {
                if (zonaMagicaTrampa[i] == null) {
                    zonaMagicaTrampa[i] = carta;
                    String estado = (carta instanceof CartaTrampa && ((CartaTrampa) carta).isBocaAbajo())? "boca abajo": "boca arriba";
                    System.out.println("Carta " + ((Carta) carta).getNombre() + " colocada en la zona de mágicas/trampa (" + estado + ").");
                    return true;
                }
            }
        }
        System.out.println("No hay espacio disponible en la zona de mágicas/trampa.");
        return false;
    }

    public ArrayList<CartaMonstruo> obtenerCartasMonstruo() {
        ArrayList<CartaMonstruo> cartas = new ArrayList<>();
        for (CartaMonstruo carta : zonaMonstruos) {
            if (carta != null) {
                cartas.add(carta);
            }
        }
        return cartas;
    }

    public ArrayList<CartaMagica> obtenerCartasMagicas() {
        ArrayList<CartaMagica> cartas = new ArrayList<>();
        for (Object carta : zonaMagicaTrampa) {
            if (carta instanceof CartaMagica) {
                cartas.add((CartaMagica) carta);
            }
        }
        return cartas;
    }

    public ArrayList<CartaTrampa> obtenerCartasTrampa() {
        ArrayList<CartaTrampa> cartas = new ArrayList<>();
        for (Object carta : zonaMagicaTrampa) {
            if (carta instanceof CartaTrampa) {
                cartas.add((CartaTrampa) carta);
            }
        }
        return cartas;
    }

    public void mostrarTablero1(Jugador jugador, JugadorMaquina enemigo) {
        System.out.println("**Zona Monstruos**");
        for (int i = 0; i < zonaMonstruos.length; i++) {
            if (zonaMonstruos[i] != null) {
                String descripcion = zonaMonstruos[i].isEnAtaque()
                        ? zonaMonstruos[i].toString()
                        : "Carta en modo defensa (boca abajo)";
                System.out.println("[" + (i + 1) + "] " + descripcion);
            } else {
                System.out.println("[" + (i + 1) + "] (vacío)");
            }
        }

        System.out.println("\n**Zona Mágica y Trampa**");
        for (int i = 0; i < zonaMagicaTrampa.length; i++) {
            if (zonaMagicaTrampa[i] != null) {
                String estado = zonaMagicaTrampa[i] instanceof CartaTrampa
                        ? (((CartaTrampa) zonaMagicaTrampa[i]).isBocaAbajo() ? "boca abajo" : "boca arriba")
                        : "boca arriba";
                System.out.println("[" + (i + 1) + "] " + ((Carta) zonaMagicaTrampa[i]).toString() + " - (" + estado + ")");
            } else {
                System.out.println("[" + (i + 1) + "] (vacío)");
            }
        }

        System.out.println("\nHealth Jugador 1: " + jugador.getVida());
        System.out.println("Health Jugador Máquina: " + enemigo.getVida());
    }

    public void mostrarTablero2() {
        System.out.println("**Zona Mágica y Trampa**");
        for (int i = 0; i < zonaMagicaTrampa.length; i++) {
            if (zonaMagicaTrampa[i] != null) {
                String estado = zonaMagicaTrampa[i] instanceof CartaTrampa
                        ? (((CartaTrampa) zonaMagicaTrampa[i]).isBocaAbajo() ? "boca abajo" : "boca arriba")
                        : "boca arriba";
                System.out.println("[" + (i + 1) + "] " + ((Carta) zonaMagicaTrampa[i]).toString() + " - (" + estado + ")");
            } else {
                System.out.println("[" + (i + 1) + "] (vacío)");
            }
        }

        System.out.println("\n**Zona Monstruos**");
        for (int i = 0; i < zonaMonstruos.length; i++) {
            if (zonaMonstruos[i] != null) {
                String descripcion = zonaMonstruos[i].isEnAtaque()
                        ? zonaMonstruos[i].toString()
                        : "Carta en modo defensa (boca abajo)";
                System.out.println("[" + (i + 1) + "] " + descripcion);
            } else {
                System.out.println("[" + (i + 1) + "] (vacío)");
            }
        }
    }

    public CartaMonstruo eliminarCartaMonstruo(int indice) {
        if (indice >= 0 && indice < zonaMonstruos.length && zonaMonstruos[indice] != null) {
            CartaMonstruo cartaEliminada = zonaMonstruos[indice];
            zonaMonstruos[indice] = null;
            System.out.println("Carta " + cartaEliminada.getNombre() + " eliminada de la zona de monstruos.");
            return cartaEliminada;
        }
        System.out.println("No hay carta en la posición indicada.");
        return null;
    }

    public Object eliminarCartaMagicaTrampa(int indice) {
        if (indice >= 0 && indice < zonaMagicaTrampa.length && zonaMagicaTrampa[indice] != null) {
            Object cartaEliminada = zonaMagicaTrampa[indice];
            zonaMagicaTrampa[indice] = null;
            System.out.println("Carta " + ((Carta) cartaEliminada).getNombre() + " eliminada de la zona de mágicas/trampa.");
            return cartaEliminada;
        }
        System.out.println("No hay carta en la posición indicada.");
        return null;
    }
}
