package mx.puestoLidia.negocio.facade;

import mx.puestoLidia.entity.ItemVenta;
import mx.puestoLidia.entity.ItemVentaId;
import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.entity.Usuario;
import mx.puestoLidia.entity.Venta;
import mx.puestoLidia.persistence.dao.ItemVentaDAO;
import mx.puestoLidia.persistence.dao.ProductoDAO;
import mx.puestoLidia.persistence.dao.VentaDAO;
import mx.puestoLidia.persistence.integration.ServiceLocator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class FacadeVenta {

    // ==========================================
    // FLUJO DE ALTAS (Cobrar Venta)
    // ==========================================
    public void procesarAltaVenta(List<ItemVenta> listaCompra, BigDecimal total, BigDecimal monto, BigDecimal cambio, boolean esCredito) throws Exception {
        VentaDAO ventaDAO = ServiceLocator.getInstanceVentaDAO();
        ProductoDAO productoDAO = ServiceLocator.getInstanceProductoDAO();
        ItemVentaDAO itemVentaDAO = ServiceLocator.getInstanceItemVentaDAO();

        Venta nuevaVenta = new Venta();
        nuevaVenta.setFechaHora(Instant.now());
        nuevaVenta.setTotal(total);
        nuevaVenta.setMonto(monto != null ? monto : total);
        nuevaVenta.setCambio(cambio != null ? cambio : BigDecimal.ZERO);
        nuevaVenta.setTipo(esCredito ? "credito" : "contado");

        // Asignamos el usuario con ID 1 para cumplir con la regla de la base de datos
        Usuario cajero = new Usuario();
        cajero.setId(1);
        nuevaVenta.setIdUsuario(cajero);

        // 1. Guardar la Venta primero para generar el ID autoincremental
        ventaDAO.save(nuevaVenta);

        // 2. Recorrer y procesar cada artículo del ticket de compra
        for (ItemVenta item : listaCompra) {
            Producto prodBD = productoDAO.find(item.getIdProducto().getIdProducto())
                    .orElseThrow(() -> new Exception("Producto no encontrado: " + item.getIdProducto().getNombre()));

            // Validar si hay stock suficiente antes de realizar la operación
            if (prodBD.getCantidad() < item.getCantidad()) {
                throw new Exception("Stock insuficiente para el producto: " + prodBD.getNombre());
            }

            // Restar las unidades correspondientes del inventario
            prodBD.setCantidad(prodBD.getCantidad() - item.getCantidad());
            productoDAO.update(prodBD);

            // Construir la llave compuesta manual (ItemVentaId)
            ItemVentaId llaveCompuesta = new ItemVentaId();
            llaveCompuesta.setIdVenta(nuevaVenta.getId());
            llaveCompuesta.setIdProducto(prodBD.getIdProducto());

            // Enlazar los objetos y mapear llaves foráneas en la entidad ItemVenta
            item.setId(llaveCompuesta);
            item.setIdVenta(nuevaVenta);
            item.setIdProducto(prodBD);

            // Persistir de forma individual cada fila del detalle
            itemVentaDAO.save(item);
        }
    }

    // ==========================================
    // FLUJO DE BAJAS (Cancelar Venta Completa)
    // ==========================================
    public void procesarBajaVenta(int idVenta) throws Exception {
        VentaDAO ventaDAO = ServiceLocator.getInstanceVentaDAO();
        ProductoDAO productoDAO = ServiceLocator.getInstanceProductoDAO();
        ItemVentaDAO itemVentaDAO = ServiceLocator.getInstanceItemVentaDAO();

        // Buscar el registro de la venta en la base de datos
        Venta venta = ventaDAO.find(idVenta)
                .orElseThrow(() -> new Exception("La venta con ID " + idVenta + " no existe."));

        // 1. Devolver el stock a los productos recorriendo el detalle de la venta
        if (venta.getItemVentas() != null) {
            for (ItemVenta item : venta.getItemVentas()) {
                Producto prodBD = productoDAO.find(item.getIdProducto().getIdProducto()).orElse(null);

                if (prodBD != null) {
                    // Sumar la cantidad de vuelta al inventario
                    prodBD.setCantidad(prodBD.getCantidad() + item.getCantidad());
                    productoDAO.update(prodBD);
                }

                // Eliminar el registro hijo del detalle de venta
                itemVentaDAO.delete(item);
            }
        }

        // 2. Eliminar el registro padre de la venta una vez limpio de dependencias
        ventaDAO.delete(venta);
    }
}