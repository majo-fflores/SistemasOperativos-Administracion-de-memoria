import java.util.ArrayList;
import java.util.List;

public class UnificadorDeParticiones {
    
    // Método que unifica particiones libres adyacentes en la lista de particiones
    public ArrayList<Particion> unificarParticiones(List<Particion> listaParticiones) {
        int i = 0;
        // Iteramos a través de la lista, verificando particiones adyacentes
        while (i < listaParticiones.size() - 1) {
            Particion particionActual = listaParticiones.get(i);          // Partición en la posición actual
            Particion particionSiguiente = listaParticiones.get(i + 1);   // Partición en la siguiente posición

            // Verificamos si la partición actual y la siguiente están libres (estado = true)
            if (particionActual.getEstado() && particionSiguiente.getEstado()) {
                int nuevoTamanio = particionActual.getTamanioParticion() + particionSiguiente.getTamanioParticion(); // Suma tamaños

                // Crear una nueva partición combinada con el nuevo tamaño
                Particion nuevaParticion = new Particion(true, -1, -1, nuevoTamanio,-1);
                listaParticiones.set(i, nuevaParticion);  // Reemplaza la partición actual con la nueva partición

                // Elimina la partición siguiente, ya que fue combinada
                listaParticiones.remove(i + 1);
            } else {
                i++;  // Avanzamos al siguiente par si no se unificaron las particiones
            }
        }
        return (ArrayList<Particion>) listaParticiones; // Devolvemos la lista modificada como un ArrayList
    }
}
