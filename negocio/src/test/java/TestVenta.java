import mx.puestoLidia.entity.ItemVenta;
import mx.puestoLidia.entity.Usuario;
import mx.puestoLidia.entity.Venta;
import mx.puestoLidia.negocio.integration.ServiceFacadeLocator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestVenta {

    public static void main(String[] args) {
        System.out.println("[PRUEBA] Iniciando prueba de flujo transaccional de venta...");

        Scanner sc = new Scanner(System.in);

        try {
            // Inicialización del carrito temporal en memoria
            List<ItemVenta> carrito = new ArrayList<>();

            System.out.print("Ingrese ID del producto a procesar: ");
            String idProducto = sc.nextLine();

            // Delegación de lógica de inserción y validación de existencias a la capa de negocio
            System.out.println("[PRUEBA] Agregando 2 unidades del producto '" + idProducto + "' al carrito...");
            ServiceFacadeLocator.getInstanceFacadeVenta().agregrarOSumarProductoEnCarrito(idProducto, 2, carrito);
            System.out.println("[ÉXITO] Carrito actualizado en memoria.");

            // Recuperación del cálculo de totales centralizado en el negocio
            BigDecimal totalCalculado = ServiceFacadeLocator.getInstanceFacadeVenta().calcularTotalCarrito(carrito);
            System.out.println("[PRUEBA] Monto total calculado del carrito: $" + totalCalculado);

            // Mock de datos para la cabecera de la venta (Asignación fija a Usuario ID 1)
            Usuario cajero = new Usuario();
            cajero.setId(1);

            Venta nuevaVenta = new Venta();
            nuevaVenta.setFechaHora(Instant.now());
            nuevaVenta.setIdUsuario(cajero);
            nuevaVenta.setTotal(totalCalculado);
            nuevaVenta.setMonto(totalCalculado.add(new BigDecimal("50.00"))); // Simulación de pago con excedente de $50
            nuevaVenta.setCambio(new BigDecimal("50.00"));
            nuevaVenta.setTipo("contado");

            // Ejecución del proceso de persistencia transaccional
            System.out.println("[PRUEBA] Invocando registrarVentaCompleta en Facade...");
            ServiceFacadeLocator.getInstanceFacadeVenta().registrarVentaCompleta(nuevaVenta, carrito);

            System.out.println("[ÉXITO] Registro guardado y stock actualizado correctamente.");

        } catch (Exception e) {
            System.err.println("[ERROR] Error en el flujo de la venta.");
            System.err.println("[EXCEPCIÓN NEGOCIO] " + e.getMessage());
            e.printStackTrace();
        } finally {
            sc.close();
        }

        System.out.println("FIN DE PRUEBA");
    }
}