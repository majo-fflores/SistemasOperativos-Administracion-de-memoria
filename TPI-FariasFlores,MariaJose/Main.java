import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        ArrayList<Trabajo> trabajos = leerArchivo("tanda1.txt"); //cambiar numero para usar otra tanda de trabajos

        // Mostrar trabajos leídos
        for (Trabajo trabajo : trabajos) {
            System.out.println(trabajo);
        }

        Scanner leer = new Scanner(System.in);

        // Ingresar tamaño de memoria disponible
        System.out.print("Ingrese el tamaño de la memoria física disponible: ");
        int tamanoMemoria = leer.nextInt();
        System.out.println("Memoria física disponible para usuarios: " + tamanoMemoria + " unidades.");

        // Pedir al usuario que elija la estrategia de asignación
        System.out.println("Seleccione la estrategia de asignación de particiones:");
        System.out.println("1. First-fit");
        System.out.println("2. Next-fit");
        System.out.println("3. Best-fit");
        System.out.println("4. Worst-fit");
        int estrategia = leer.nextInt();

        Politicas politica;
        switch (estrategia) {
            case 1:
                politica = new FirstFit();
                break;
            case 2:
                politica = new NextFit();
                break;
            case 3:
                politica = new BestFit();
                break;
            case 4:
                politica = new WorstFit();
                break;
            default:
                throw new IllegalArgumentException("La politica seleccionada no es válida: " + estrategia);
        }

        System.out.println("Ingrese el tiempo de carga del trabajo a la memoria principal: ");
        int tiempoCargaMp = leer.nextInt();
        System.out.println("Ingrese el tiempo de seleccion de la particion: ");
        int tiempoSelec = leer.nextInt();
        System.out.println("Ingrese el tiempo de liberacion de la particion:");
        int tiempoLiberacion = leer.nextInt();

        try {
            Resultado resultado = politica.ejecutar(trabajos, tamanoMemoria, tiempoCargaMp, tiempoSelec, tiempoLiberacion);
        
            // Imprimir resultados de los tiempos de retorno
            for (Particion p : resultado.getListaParticiones()) {
                System.out.println("Tiempo retorno del trabajo " + p.getIdTrabajo() + ": " + p.getTiempoFinalizacion());
            }
            System.out.println("Tiempo de retorno de la tanda de trabajos: " + resultado.getLongitudX());
        
            // Calculo el tiempo medio de retorno
            double total = resultado.getListaParticiones().stream().mapToDouble(Particion::getTiempoFinalizacion).sum();
            System.out.println("Tiempo medio de retorno: " + (total / resultado.getListaParticiones().size()));
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        
}

    private static ArrayList<Trabajo> leerArchivo (String archivo) {
        ArrayList<Trabajo> trabajos = new ArrayList<>();
        try (BufferedReader bufferLector = new BufferedReader(new FileReader(archivo))) {
            String puntero;
            while ((puntero = bufferLector.readLine()) != null) {
                puntero = puntero.trim();
                if (puntero.isEmpty()) continue;

                String[] datos = puntero.split(",");
                if (datos.length != 5) {
                    System.out.println("Línea en el archivo no válida: " + puntero);
                    continue;
                }

                try {
                    int id = Integer.parseInt(datos[0].trim());
                    String nombre = datos[1].trim();
                    int memoriaRequerida = Integer.parseInt(datos[2].trim());
                    int duracion = Integer.parseInt(datos[3].trim());
                    int instanteArribo = Integer.parseInt(datos[4].trim());

                    Trabajo trabajo = new Trabajo(id, nombre, memoriaRequerida, duracion, instanteArribo);
                    trabajos.add(trabajo);
                } catch (NumberFormatException e) {
                    System.out.println("Error de formato en la línea: " + puntero + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        return trabajos;
        
    }
    
}