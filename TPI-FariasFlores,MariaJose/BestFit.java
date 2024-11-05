import java.util.*;

public class BestFit extends Politicas {

    @Override
    public Resultado ejecutar(List<Trabajo> listaTrabajos, int tamanioMp, int tiempoCargaMp, int tiempoSelec, int tiempoLiberacion) throws InterruptedException {
        System.out.println("Opción Best Fit seleccionada");

         // Inicializo las particiones y establezco el tiempo actual
        List<Particion> listaParticiones = inicializarParticiones(tamanioMp);
        int tiempoActual = 0;

        while (!listaTrabajos.isEmpty()) {
            Trabajo trabajoActual = listaTrabajos.get(0);

            // Verifico si el trabajo es mayor que la memoria principal, si es asi detengo ejecución
            if (trabajoActual.getTamanio() > tamanioMp) {
                System.out.println("Error: El trabajo " + trabajoActual.getNombre() + " excede el tamaño de la memoria principal. Finalizando ejecución.");
                break; // Interrumpimos la ejecución del sistema batch
            }
            
            System.out.println("Trabajo actual: " + trabajoActual.getNombre() + " esperando partición.");
            System.out.println("Tiempo actual: " + tiempoActual);

            // Actualizamos el estado de las particiones según el tiempo actual
            actualizarParticiones(listaParticiones, tiempoActual);

            // Unificamos particiones después de que las particiones finalizaron
            listaParticiones = unificoParticiones.unificarParticiones(listaParticiones);

            System.out.println("Particiones disponibles:");
            for (Particion particion : listaParticiones) {
                System.out.println(" - " + particion);
            }

            //busco las particiones libres
            List<Particion> listaParticionesLibres = new ArrayList<>();
            for (Particion particion : listaParticiones) {
                if (particion.getEstado()) {
                    listaParticionesLibres.add(particion);
                }
            }

            // Ordeno particiones libres de menor a mayor para luego poder encontrar la que mejor que mejor se adapte
            listaParticionesLibres.sort(Comparator.comparingInt(Particion::getTamanioParticion));
            
            boolean buscoLugar = true;
            int i = 0;

            while (buscoLugar && i < listaParticionesLibres.size()) {
                Particion particion = listaParticionesLibres.get(i);
                
                if (particion.getTamanioParticion() >= trabajoActual.getTamanio()) {
                    //Se encontro el lugar entonces pasa a ser falso porque deja de buscar lugar
                    buscoLugar = false;

                    int tiempoInicio = tiempoCargaMp + tiempoSelec + tiempoActual;
                    int tiempoFinalizacion = tiempoInicio + trabajoActual.getDuracion() + tiempoLiberacion;

                    if (particion.getTamanioParticion() == trabajoActual.getTamanio()){// si la partición es exacta creo una nueva partcion ocupada y reemplaza la original

                        Particion particionNueva = new Particion(false, tiempoInicio, tiempoFinalizacion, trabajoActual.getTamanio(),  trabajoActual.getId());
                        System.out.println("El trabajo " + trabajoActual.getNombre() + " encontro particion");
                        System.out.println(particionNueva);

                        // Primero agregamos la nueva particion a la lista de particiones
                        particiones.add(particionNueva);
                        
                        // segundo obtendo el indice de la particion original para saber donde insertarla en la lista de particiones
                        int indiceOriginal = listaParticiones.indexOf(particion);

                        //elimino la particion original
                        listaParticiones.remove(particion);

                        //inserto la nueva particicion en el mismo indice
                        listaParticiones.add(indiceOriginal, particionNueva);
                    }
                    else  if (particion.getTamanioParticion() > trabajoActual.getTamanio()) {//si particion mas grande que la del trabajo la voy a dividir en 2 la 1ra asigno al trabajo                       
                       
                        // Creo la nueva partición para el trabajo actual
                       Particion particionNueva = new Particion(false, tiempoInicio,tiempoFinalizacion, trabajoActual.getTamanio(), trabajoActual.getId());
                        
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
                i++;//sigue otro trabajo
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
