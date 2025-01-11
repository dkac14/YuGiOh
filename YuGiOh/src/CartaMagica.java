import java.util.ArrayList;

public class CartaMagica extends Carta {
    public String tipo_monstruo;
    private int incremento;
    public String tipo_incremento;
    private CartaMonstruo equipada_a; 

    public CartaMagica(String nombre, String descripcion, String tipo_monstruo, 
                       int incremento, String tipo_incremento) {
        super(nombre, descripcion); 
        this.tipo_monstruo = tipo_monstruo;
        this.incremento = incremento;
        this.tipo_incremento = tipo_incremento;
        this.equipada_a = null; 
    }

    public String getTipo_monstruo() {
        return tipo_monstruo;
    }

    public void setTipo_monstruo(String tipo_monstruo) {
        this.tipo_monstruo = tipo_monstruo;
    }

    public int getIncremento() {
        return incremento;
    }

    public void setIncremento(int incremento) {
        this.incremento = incremento;
    }

    public String getTipo_incremento() {
        return tipo_incremento;
    }

    public void setTipo_incremento(String tipo_incremento) {
        this.tipo_incremento = tipo_incremento;
    }

    public CartaMonstruo getEquipada_a() {
        return equipada_a;
    }

    public void setEquipada_a(CartaMonstruo equipada_a) {
        this.equipada_a = equipada_a;
    }

    public int activar_carta(CartaMonstruo monstruo) {
        if (monstruo.getTipoMonstruo().equals(this.tipo_monstruo)) { // Usamos el getter para tipo_monstruo
            equipada_a = monstruo;
            if (this.tipo_incremento.equals("ataque")) {
                int aumento = this.incremento;
                System.out.println(this.getNombre() + " equipada a " + monstruo.getNombre() + 
                                   ". Incrementa su " + this.tipo_incremento + " en " + this.incremento + ".");
                return aumento; // Retorna el aumento en ataque.
            } else if (this.tipo_incremento.equals("defensa")) {
                int aumento = this.incremento;
                System.out.println(this.getNombre() + " equipada a " + monstruo.getNombre() + 
                                   ". Incrementa su " + this.tipo_incremento + " en " + this.incremento + ".");
                return aumento; // Retorna el aumento en defensa.
            }
        } else {
            System.out.println(this.getNombre() + " no puede equiparse a " + monstruo.getNombre() + 
                               " porque no es del tipo " + this.tipo_monstruo + ".");
            return 0;
        }
        return 0;
    }

    public void destruir(ArrayList<CartaMagica> listaCartas, ArrayList<CartaMonstruo> listaMonstruos) {
        if (this.equipada_a != null) {
            if (!listaMonstruos.contains(this.equipada_a)) {
                System.out.println(this.getNombre() + " ha sido destruida porque " + this.equipada_a.getNombre() + 
                                   " ya no está en el campo.");
                listaCartas.remove(this);
                this.equipada_a = null;
            }
        }
    }
}
