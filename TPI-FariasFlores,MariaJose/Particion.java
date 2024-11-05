public class Particion {
    private static int ultimoId = 0;   // Contador estático para generar IDs únicos para cada partición
    private int id;                    // ID único de la partición
    private boolean estado;            // Estado de la partición (true = libre, false = ocupada)
    private long tiempoInicio;         // Tiempo de inicio del trabajo en la partición
    private long tiempoFinalizacion;   // Tiempo de finalización del trabajo en la partición
    private int tamanio;               // Tamaño de la partición
    private int idTrabajo;             // ID del trabajo asignado a esta partición (si tiene uno) sino -1

    // Constructor que inicializa los atributos
    public Particion(boolean estado, long tiempoInicio, long tiempoFinalizacion, int tamanio, int idTrabajo) {
        this.id = generarId();              // Genera un ID único automáticamente
        this.estado = estado;               // Define si la partición está libre o ocupada
        this.tiempoInicio = tiempoInicio;   // Define el tiempo de inicio del trabajo en esta partición
        this.tiempoFinalizacion = tiempoFinalizacion; // Define el tiempo de finalización del trabajo
        this.tamanio = tamanio;             // Tamaño de la partición
        this.idTrabajo = idTrabajo;         // ID del trabajo asignado
    }


    private static int generarId() {
        return ++ultimoId;
    }


    public int getId() {
        return id;
    }

    public boolean getEstado(){ 
        return estado; 
    }
    
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public long getTiempoInicio() {
        return tiempoInicio;
    }

    public void setTiempoInicio(int tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

    public long getTiempoFinalizacion() {
        return tiempoFinalizacion;
    }

    public void setTiempoFinalizacion(int tiempoFinalizacion) {
        this.tiempoFinalizacion = tiempoFinalizacion;
    }

    public int getTamanioParticion(){ 
        return tamanio;
    }

    public void setTamanio(int tamanio){ 
        this.tamanio = tamanio;
    }

    public int getIdTrabajo() {
        return idTrabajo;
    }

    public void setIdTrabajo(int idTrabajo) {
        this.idTrabajo = idTrabajo;
    }
    
    // Método toString para representar la partición como una cadena de texto, útil para depuración
    @Override
    public String toString() {
        return "id: " + id + 
               ", estado: " + estado + 
               ", Tiempo Inicio: " + tiempoInicio + 
               ", Tiempo Finalizacion: " + tiempoFinalizacion + 
               ", tamaño: " + tamanio + 
               ", idTrabajo: " + idTrabajo;
    }
}
