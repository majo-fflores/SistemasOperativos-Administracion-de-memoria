import java.util.List;

public class Resultado {
    private Long longitudX;
    private List<Particion> listaParticiones;
    private int fragmentacion;

    public Resultado(Long longitudX, List<Particion> listaParticiones, int fragmentacion) {
        this.longitudX = longitudX;
        this.listaParticiones = listaParticiones;
        this.fragmentacion = fragmentacion;
    }

    public Long getLongitudX() {
        return longitudX;
    }

    public List<Particion> getListaParticiones() {
        return listaParticiones;
    }

    public int getFragmentacion() {
        return fragmentacion;
    }

    @Override
    public String toString() {
        return "Resultado{" +
                ", listaParticiones=" + listaParticiones +
                ", fragmentacion=" + fragmentacion +
                '}';
    }
}