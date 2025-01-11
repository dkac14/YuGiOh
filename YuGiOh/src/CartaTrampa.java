import java.util.ArrayList;
public class CartaTrampa extends Carta {
    private String tipoAtributo;
    private boolean bocaAbajo;

    public CartaTrampa(String nombre, String descripcion, String tipoAtributo) {
        super(nombre, descripcion);
        this.tipoAtributo = tipoAtributo;
        this.bocaAbajo = true;
    }
    
    public String getTipoAtributo() {
        return tipoAtributo;
    }

    public void setTipoAtributo(String tipoAtributo) {
        this.tipoAtributo = tipoAtributo;
    }

    public boolean isBocaAbajo() {
        return bocaAbajo;
    }

    public void setBocaAbajo(boolean bocaAbajo) {
        this.bocaAbajo = bocaAbajo;
    }


    //Me estas dañando el codigo <3
    //ñO uwu

    public boolean verificar(CartaMonstruo cartaAtacante) {
        return cartaAtacante.getElemento().equals(this.tipoAtributo);
    }

    public String activar(CartaMonstruo cartaAtacante, ArrayList<CartaTrampa> listaCartas, Tablero tablero, int indice) {
        if (cartaAtacante.getElemento().equals(this.tipoAtributo)) {
            this.bocaAbajo = false;
            String mensaje = this.getNombre() + " ha sido activada y detiene el ataque de " + cartaAtacante.getNombre() + ".";
            this.descartar(listaCartas);
            tablero.eliminarCartaMagicaTrampa(indice);
            return mensaje;
        } else {
            return this.getNombre() + " no puede activarse porque " + cartaAtacante.getNombre() + " no tiene el atributo " + this.tipoAtributo + ".";
        }
    }

    public String descartar(ArrayList<CartaTrampa> listaCartas) {
        if (listaCartas.contains(this)) {
            listaCartas.remove(this);
            return this.getNombre() + " ha sido descartada después de ser utilizada.";
        }
        return "La carta no se encuentra en la lista y no puede ser descartada.";
    }

    @Override
    public String toString() {
        return "Carta Trampa";
    }
}



