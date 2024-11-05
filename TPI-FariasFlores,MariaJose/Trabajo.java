public class Trabajo {
    private int id;
    private String nombre;
    private int tamanio;
    private int duracion;
    private int instanteArribo;

    public Trabajo(int id, String nombre, int tamanio, int duracion, int instanteArribo) {
        this.id = id;
        this.nombre = nombre;
        this.tamanio = tamanio;
        this.duracion = duracion;
        this.instanteArribo = instanteArribo;
    }

    public int getId(){ 
        return id; 
    }

    
    public String getNombre(){ 
        return nombre; 
    }

    public int getTamanio(){ 
        return tamanio; 
    }

    public int getDuracion(){ 
        return duracion; 
    }

    public int getInstanteArribo(){ 
        return instanteArribo; 
    }

    @Override
    public String toString() {
        return "Trabajo{ " + id +
                ", nombre del proceso = '" + nombre + '\'' +
                ", tamanio del proceso = " + tamanio +
                ", duracion = " + duracion +
                '}';
    }
}