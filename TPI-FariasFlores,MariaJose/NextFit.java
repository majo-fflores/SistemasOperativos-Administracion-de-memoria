import java.util.*;

public class NextFit extends Politicas {

    @Override
    public Resultado ejecutar(List<Trabajo> listaTrabajos, int tamanioMp, int tiempoCargaMp, int tiempoSelec, int tiempoLiberacion) throws InterruptedException {
        System.out.println("Opción Next Fit seleccionada");

        // Inicializo las particiones y establezco el tiempo actual
        List<Particion> listaParticiones = inicializarParticiones(tamanioMp);
        int tiempoActual = 0;
        int ultimaPosicionAsignada = 0; //Para guardar la ultima asignacion

        while (!listaTrabajos.isEmpty()) {
            Trabajo trabajoActual = listaTrabajos.get(0);

            // Verifico si el trabajo es mayor que la memoria principal, si es asi detengo ejecución
            if (trabajoActual.getTamanio() > tamanioMp) {
                System.out.println("Error: El trabajo " + trabajoActual.getNombre() + " excede el tamaño de la memoria principal. Finalizando ejecución.");
                break; // Interrumpimos la ejecución del sistema batch
            }
            
            System.out.println("Trabajo actual: " + trabajoActual.getNombre() + " esperando partición");
            System.out.println("Tiempo actual: " + tiempoActual);

            // Actualizo el estado de las particiones según el tiempo actual
            actualizarParticiones(listaParticiones, tiempoActual);

            // Unifico las particiones después de que las particiones finalizaron
            listaParticiones = unificoParticiones.unificarParticiones(listaParticiones);

            System.out.println("Particiones disponibles:");
            for (Particion particion : listaParticiones) {
                System.out.println(" - " + particion);
            }

            boolean buscoLugar = true;
            
            while (buscoLugar && ultimaPosicionAsignada < listaParticiones.size()) {
                Particion particion = listaParticiones.get(ultimaPosicionAsignada);

                if ((particion.getEstado()) && particion.getTamanioParticion() >= trabajoActual.getTamanio()) {
                    buscoLugar = false; //ya encontre lugar

                    int tiempoInicio = tiempoCargaMp + tiempoSelec + tiempoActual;
                    int tiempoFinalizacion = tiempoInicio + trabajoActual.getDuracion() + tiempoLiberacion;

                    if (particion.getTamanioParticion() == trabajoActual.getTamanio()) {// si la partición es exacta creo una nueva partcion ocupada y reemplaza la original

                        Particion particionNueva = new Particion(false, tiempoInicio, tiempoFinalizacion, trabajoActual.getTamanio(), trabajoActual.getId());
                        System.out.println("El trabajo " + trabajoActual.getNombre() + " encontro particion");
                        System.out.println(particionNueva);

                        // Primero agregamos la nueva particion a la lista de particiones
                        particiones.add(particionNueva);
                        
                        // segundo obtendo el indice de la particion original
                        int indiceOriginal = listaParticiones.indexOf(particion);

                        //elimino la particion original
                        listaParticiones.remove(particion);

                        //inserto la nueva particicion en el mismo indice
                        listaParticiones.add(indiceOriginal, particionNueva);
                    
                    }
                    else if (particion.getTamanioParticion() > trabajoActual.getTamanio()) {//si particion mas grande que la del trabajo la voy a dividir en 2 la 1ra asigno al trabajo                       

                       // Creo la nueva partición para el trabajo actual
                       Particion particionNueva = new Particion(false, tiempoInicio,tiempoFinalizacion, trabajoActual.getTamanio(),trabajoActual.getId());
                        
                       //obtengo el indice de la particion original
                       int indiceOriginal = listaParticiones.indexOf(particion);

                       // Inserto la nueva partición después de la actual
                       listaParticiones.add(indiceOriginal + 1, particionNueva);

                       // Agrego la nueva partición a la lista que será retornada
                       particiones.add(particionNueva);

                       // Creo e inserto la partición sobrante (libre)
                       Particion particionSobrante = new Particion(true,-1,-1,particion.getTamanioParticion() - trabajoActual.getTamanio(),-1);
                       listaParticiones.add(indiceOriginal + 2, particionSobrante);

                       // Eliminar la partición original
                       listaParticiones.remove(particion);

                       System.out.println("El trabajo " + trabajoActual.getNombre() + " encontró partición");
                   }

                   listaTrabajos.remove(trabajoActual);
                }
                ultimaPosicionAsignada++; //siguiente trabajo desde la ultima asignacion
            }
                
            if (buscoLugar) {
                System.out.println("No se pudo asignar el trabajo " + trabajoActual.getNombre() + ". Calculando fragmentación externa...");}
            
            calcularFragmentacionExterna(listaParticiones, listaTrabajos);

            System.out.println("Estado de particiones después de la asignación:");
            for (Particion particion : listaParticiones) {
                System.out.println(" - " + particion);
            }

            tiempoActual++;
            System.out.println("Tiempo incrementado a: " + tiempoActual);
            System.out.println("------------------------------");
            Thread.sleep(1000);

            // Añadimos el reinicio del índice dentro del bucle de búsqueda
            if (ultimaPosicionAsignada == listaParticiones.size()) {
                ultimaPosicionAsignada = 0;
            }
        }

         //Busco la particion que mas tarde termino para saber el tiempo de retorno de la tarda de trabajos
        Particion particionConMayorTiempo = listaParticiones.stream().max(Comparator.comparingLong(Particion::getTiempoFinalizacion)).orElse(null);

        //con el operador ternario lo que hago es verificar que no este vacio para pasar mi tiempo de finalizacion mas largo y ultimo en terminar 
        //o si es vacio poner el tiempo actual para seguir operando o actualizando la memoria en tiempo real
        long longitudX = particionConMayorTiempo != null ? particionConMayorTiempo.getTiempoFinalizacion() : tiempoActual;

        System.out.println("Índice de fragmentación externa final: " + fragmentacionExterna);

        return new Resultado(longitudX, particiones, fragmentacionExterna);
    }
}