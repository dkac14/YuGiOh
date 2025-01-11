import java.io.*;
import java.util.*;

public class MazoCartas {
private ArrayList<Carta> cartas;

public MazoCartas(){
    this.cartas= new ArrayList<>();
}

public MazoCartas(ArrayList<Carta> cartas){
    this.cartas=cartas;
}

    public ArrayList<Carta> getCartas() {
    return cartas;
}

public void setCartas(ArrayList<Carta> cartas) {
        this.cartas = cartas;
    }

public static ArrayList<Carta> cargarCartas(String rutaArchivo) {
    ArrayList<Carta> mazo = new ArrayList<>();
    try (BufferedReader archivo = new BufferedReader(new FileReader(rutaArchivo))) {
        String linea;
        while ((linea = archivo.readLine()) != null) {
            String[] datos = linea.strip().split(",");
            String tipoCarta = datos[0];

            if (tipoCarta.equals("Monstruo")) {
                String nombre = datos[1];
                String descripcion = datos[2];
                int ataque = Integer.parseInt(datos[3]);
                int defensa = Integer.parseInt(datos[4]);
                String atributo = datos[5];
                String monstruo = datos[6];
                mazo.add(new CartaMonstruo(nombre, descripcion, ataque, defensa, atributo, monstruo, true));
            } else if (tipoCarta.equals("Magica")) {
                String nombre = datos[1];
                String descripcion = datos[2];
                String tipo_monstruo = datos[3];
                int incremento = Integer.parseInt(datos[4]);
                String tipo_incremento = datos[5];
                mazo.add(new CartaMagica(nombre, descripcion, tipo_monstruo, incremento, tipo_incremento));
            } else if (tipoCarta.equals("Trampa")) {
                String nombre = datos[1];
                String descripcion = datos[2];
                String atributo = datos[3];
                mazo.add(new CartaTrampa(nombre, descripcion, atributo));
            }
        }
    } catch (IOException e) {
        System.out.println(e.getMessage());
    }
    return mazo;
}

public ArrayList<Carta> seleccionarMazo(String archivo) {
    ArrayList<Carta> c = cargarCartas(archivo);
    Collections.shuffle(c);

    int cartasMonstruos = 0;
    int cartasMagicas = 0;
    int cartasTrampa = 0;
    ArrayList<Carta> mazo = new ArrayList<>();

    for (Carta carta : c) {
        if (carta instanceof CartaMonstruo && cartasMonstruos < 20) {
            mazo.add(carta);
            cartasMonstruos++;
        } else if (carta instanceof CartaMagica && cartasMagicas < 5) {
            mazo.add(carta);
            cartasMagicas++;
        } else if (carta instanceof CartaTrampa && cartasTrampa < 5) {
            mazo.add(carta);
            cartasTrampa++;
        }

        if (cartasMonstruos == 20 && cartasMagicas == 5 && cartasTrampa == 5) {
            break;
        }
    }

    this.cartas = mazo;
    return mazo;

}
    public void mostrarMazo() {
        for (Carta carta : cartas) {
            System.out.println(carta);//xd
        }
    }
}



