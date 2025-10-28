import java.util.*;
import java.io.*;

public class Main {

    // Metodo principal que ordena directamente una Lista
    public static void ordenarRapido(List<Integer> lista) {
        if (lista.size() <= 1) return;

        List<Integer> listaOrdenada = ordenarRecursivo(lista);
        lista.clear();
        lista.addAll(listaOrdenada);
    }

    // Metodo recursivo que implementa la lógica de Ordenación Rápida
    private static List<Integer> ordenarRecursivo(List<Integer> lista) {
        if (lista.size() <= 1) return lista;

        Random generadorAleatorio = new Random();
        int indicePivote = generadorAleatorio.nextInt(lista.size());
        int pivote = lista.get(indicePivote);

        List<Integer> menores = new ArrayList<>();
        List<Integer> iguales = new ArrayList<>();
        List<Integer> mayores = new ArrayList<>();

        for (int numero : lista) {
            if (numero < pivote) menores.add(numero);
            else if (numero == pivote) iguales.add(numero);
            else mayores.add(numero);
        }

        return unirListas(ordenarRecursivo(menores), iguales, ordenarRecursivo(mayores));
    }

    // Metodo auxiliar para unir las tres listas
    private static List<Integer> unirListas(List<Integer> lista1, List<Integer> lista2, List<Integer> lista3) {
        List<Integer> resultado = new ArrayList<>();
        resultado.addAll(lista1);
        resultado.addAll(lista2);
        resultado.addAll(lista3);
        return resultado;
    }

    // Metodo para guardar una lista en un archivo TXT
    private static void guardarEnArchivo(List<Integer> lista, String nombreArchivo) {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(nombreArchivo))) {
            for (int numero : lista) {
                escritor.println(numero);
            }
            System.out.println("Archivo guardado: " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Integer> numeros = new ArrayList<>();

        System.out.println("=== INGRESO DE NÚMEROS DE PEDIDO ===");
        System.out.println("Ingresa los números de pedido (escribe 'fin' para terminar):");

        // Leer números hasta que el usuario escriba "fin"
        while (true) {
            System.out.print("Número de pedido: ");
            String entrada = scanner.nextLine();

            if (entrada.equalsIgnoreCase("fin")) {
                break;
            }

            try {
                int numero = Integer.parseInt(entrada);
                numeros.add(numero);
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingresa un número válido o 'fin' para terminar.");
            }
        }

        // Guardar los números desordenados en un archivo
        guardarEnArchivo(numeros, "pedidos_desordenados.txt");

        System.out.println("\nLista original (desordenada):");
        System.out.println(numeros);

        // Aplicar QuickSort
        ordenarRapido(numeros);

        System.out.println("Lista ordenada:");
        System.out.println(numeros);

        // Guardar los números ordenados en otro archivo
        guardarEnArchivo(numeros, "pedidos_ordenados.txt");

        System.out.println("\n¡Proceso completado!");
        System.out.println("- pedidos_desordenados.txt → Contiene los números en el orden ingresado");
        System.out.println("- pedidos_ordenados.txt → Contiene los números ordenados");

        scanner.close();
    }
}