import java.util.ArrayList;
import java.util.List;

public abstract class Politicas {
    protected UnificadorDeParticiones unificoParticiones;
    protected List<Particion> particiones;
    protected int fragmentacionExterna;

    public Politicas() {
        this.unificoParticiones = new UnificadorDeParticiones();
        this.particiones = new ArrayList<>();
        this.fragmentacionExterna =0;
    }

    // metodo para inicializar la primera partición
    protected List<Particion> inicializarParticiones(int tamanioMp) {
        List<Particion> listaParticiones = new ArrayList<>();
        listaParticiones.add(new Particion(true, -1, -1, tamanioMp, -1));
        return listaParticiones;
    }

    // metodo para actualizar el estado de las particiones según el tiempo actual
    protected void actualizarParticiones(List<Particion> listaParticiones, int tiempoActual) {
        for (Particion particion : listaParticiones) {
            if (particion.getTiempoFinalizacion() == tiempoActual) {
                particion.setEstado(true);
            }
        }
    }

    //metodo para calcular la fragmentacion externa
    protected void calcularFragmentacionExterna(List<Particion> listaParticiones, List<Trabajo> listaTrabajos) {
        for (Particion particion : listaParticiones) {
            if (particion.getEstado() && !listaTrabajos.isEmpty()) {
                fragmentacionExterna += particion.getTamanioParticion();
                System.out.println("Fragmentación externa actual: " + fragmentacionExterna);
            }
        }
    }

    // Método abstracto para ejecutar la política específica
    public abstract Resultado ejecutar(List<Trabajo> listaTrabajos, int tamanioMp, int tiempoCargaMp, int tiempoSelec, int tiempoLiberacion) throws InterruptedException;
}