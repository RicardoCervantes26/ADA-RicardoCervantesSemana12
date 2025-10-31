package Clientes;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PedidoClienteBRIDGES {
    private String codigo;
    private String usuarioCliente;
    private String descripcion;
    private double peso;
    private String direccionEntrega;
    private String estado; 

    public PedidoClienteBRIDGES() {}

    public PedidoClienteBRIDGES(String codigo, String usuarioCliente, String descripcion,
                                double peso, String direccionEntrega) {
        this.codigo = codigo;
        this.usuarioCliente = usuarioCliente;
        this.descripcion = descripcion;
        this.peso = peso;
        this.direccionEntrega = direccionEntrega;
        this.estado = "Pendiente";
    }

    public String getCodigo() { return codigo; }
    public String getUsuarioCliente() { return usuarioCliente; }
    public String getDescripcion() { return descripcion; }
    public double getPeso() { return peso; }
    public String getDireccionEntrega() { return direccionEntrega; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // Gestor de pedidos con HashMap
    public static class GestorPedidos {
        private static HashMap<String, PedidoClienteBRIDGES> mapaPedidos = new HashMap<>();
        private static final String ARCHIVO = "pedidos_clientes.txt";

        // Cargar pedidos al iniciar
        static {
            cargarDesdeArchivo();
        }

        // Crear nuevo pedido con verificacion de codigo unico
        public static boolean crearPedido(PedidoClienteBRIDGES pedido) {
            if (mapaPedidos.containsKey(pedido.getCodigo())) {
                System.out.println("Error: El codigo de pedido ya existe");
                return false;
            }
            mapaPedidos.put(pedido.getCodigo(), pedido);
            guardarEnArchivo();
            return true;
        }

        // Obtener todos los pedidos de un usuario especifico
        public static ArrayList<PedidoClienteBRIDGES> obtenerPedidosUsuario(String usuario) {
            // Recargar datos del archivo para obtener estados actualizados
            cargarDesdeArchivo();

            ArrayList<PedidoClienteBRIDGES> pedidosUsuario = new ArrayList<>();
            for (PedidoClienteBRIDGES pedido : mapaPedidos.values()) {
                if (pedido.getUsuarioCliente().equals(usuario)) {
                    pedidosUsuario.add(pedido);
                }
            }
            return pedidosUsuario;
        }

        // MÉTODO NUEVO: Cargar todos los pedidos (para el sistema empresa)
        public static ArrayList<PedidoClienteBRIDGES> cargarPedidos() {
            // Recargar datos del archivo para obtener estados actualizados
            cargarDesdeArchivo();
            return new ArrayList<>(mapaPedidos.values());
        }

        // Obtener pedidos por estado
        public static ArrayList<PedidoClienteBRIDGES> obtenerPorEstado(String estado) {
            ArrayList<PedidoClienteBRIDGES> resultado = new ArrayList<>();
            for (PedidoClienteBRIDGES pedido : mapaPedidos.values()) {
                if (pedido.getEstado().equals(estado)) {
                    resultado.add(pedido);
                }
            }
            return resultado;
        }

        // Actualizar estado de pedido (USADO POR EL SISTEMA EMPRESA)
        public static boolean actualizarEstado(String codigo, String nuevoEstado) {
            PedidoClienteBRIDGES pedido = mapaPedidos.get(codigo);
            if (pedido != null) {
                pedido.setEstado(nuevoEstado);
                guardarEnArchivo();
                return true;
            }
            return false;
        }

        // MÉTODO NUEVO: Forzar recarga de datos
        public static void recargarDatos() {
            cargarDesdeArchivo();
        }

        // MÉTODO NUEVO: Obtener pedido especifico
        public static PedidoClienteBRIDGES obtenerPedido(String codigo) {
            return mapaPedidos.get(codigo);
        }

        // Guardar pedidos en archivo
        private static void guardarEnArchivo() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO))) {
                for (PedidoClienteBRIDGES pedido : mapaPedidos.values()) {
                    writer.write(pedido.getCodigo() + "|" + pedido.getUsuarioCliente() + "|" +
                            pedido.getDescripcion() + "|" + pedido.getPeso() + "|" +
                            pedido.getDireccionEntrega() + "|" + pedido.getEstado());
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error guardando pedidos: " + e.getMessage());
            }
        }

        // Cargar pedidos desde archivo
        private static void cargarDesdeArchivo() {
            File archivo = new File(ARCHIVO);
            if (!archivo.exists()) return;

            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                // Limpiar mapa actual
                mapaPedidos.clear();

                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] datos = linea.split("\\|");
                    if (datos.length == 6) {
                        PedidoClienteBRIDGES pedido = new PedidoClienteBRIDGES(
                                datos[0], datos[1], datos[2],
                                Double.parseDouble(datos[3]), datos[4]
                        );
                        pedido.setEstado(datos[5]);
                        mapaPedidos.put(datos[0], pedido);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error cargando pedidos: " + e.getMessage());
            }
        }
    }

}
