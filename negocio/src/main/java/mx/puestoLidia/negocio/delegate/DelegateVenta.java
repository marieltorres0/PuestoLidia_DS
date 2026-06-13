package mx.puestoLidia.negocio.delegate;

import mx.puestoLidia.entity.ItemVenta;
import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.entity.Venta;
import mx.puestoLidia.persistence.integration.ServiceLocator;

import java.math.BigDecimal;
import java.util.List;

public class DelegateVenta {

    // ==== REGISTRAR LA VENTA COMPLETA TRANSACCIONAL ====
    public void registrarVentaCompleta(Venta nuevaVenta, List<ItemVenta> carrito){
        ServiceLocator.getInstanceVentaDAO().registrarVentaCompletaTransaccional(nuevaVenta,carrito);
    }

    // === SUMAR / AGREGAR PRODUCTOS AL CARRITO ===
    public void agregarOActualizarProductoEnCarrito(String idProducto, int cantidadSolicitada, List<ItemVenta> carritoActual) {
        // buscar el producto fresco en la BD para validar stock real
        Producto producto = ServiceLocator.getInstanceProductoDAO().buscarProductoPorID(idProducto);
        if (producto == null) {
            throw new RuntimeException("El producto no existe.");
        }

        // buscar si el producto ya estaba en el carrito
        ItemVenta itemExistente = null;
        for (ItemVenta iv : carritoActual) {
            if (iv.getIdProducto().getIdProducto().equals(idProducto)) {
                itemExistente = iv;
                break;
            }
        }

        // calcular cuánto quiere en total el usuario
        int cantidadTotalFinal = cantidadSolicitada;
        if (itemExistente != null) {
            cantidadTotalFinal += itemExistente.getCantidad();
        }

        // VALIDAR EL STOCK EN LA BASE DE DATOS
        if (cantidadTotalFinal > producto.getCantidad()) {
            throw new RuntimeException("Stock insuficiente para " + producto.getNombre() +
                    ". Disponible: " + producto.getCantidad());
        }

        // actualizar carrito
        if (itemExistente != null) {
            itemExistente.setCantidad(cantidadTotalFinal);
            itemExistente.setImporte(itemExistente.getPrecioUnitario().multiply(new java.math.BigDecimal(cantidadTotalFinal)));
        } else {
            ItemVenta nuevoItem = new ItemVenta();
            nuevoItem.setId(new mx.puestoLidia.entity.ItemVentaId());
            nuevoItem.setIdProducto(producto);
            nuevoItem.setCantidad(cantidadSolicitada);
            nuevoItem.setPrecioUnitario(producto.getPrecio());
            nuevoItem.setImporte(producto.getPrecio().multiply(new java.math.BigDecimal(cantidadSolicitada)));
            carritoActual.add(nuevoItem);
        }
    }

    // ==== RESTAR O QUITAR PRODUCTOS AL CARRITO ====
    public void eliminarOQuitarProductoDelCarrito(String idProducto, List<ItemVenta> carritoActual) {
        carritoActual.removeIf(iv -> iv.getIdProducto().getIdProducto().equals(idProducto));
    }

    // ==== CALCULAR EL TOTAL DE LA VENTA ====
    public BigDecimal calcularTotalCarrito(List<ItemVenta> carritoActual) {
        java.math.BigDecimal granTotal = java.math.BigDecimal.ZERO;
        for (ItemVenta iv : carritoActual) {
            granTotal = granTotal.add(iv.getImporte());
        }
        return granTotal;
    }

    // ==== CRUD BÁSICO ====
    public void guardarVenta(Venta ventaGuardar){
        ServiceLocator.getInstanceVentaDAO().guardarVenta(ventaGuardar);
    }

    public Venta buscarVentaPorID(int idVenta){
        return ServiceLocator.getInstanceVentaDAO().buscarVentaPorId(idVenta);
    }

    public void modificarVenta(Venta ventaModificar){
        ServiceLocator.getInstanceVentaDAO().modificarVenta(ventaModificar);
    }

    public void eliminarVenta(Venta ventaEliminar){
        ServiceLocator.getInstanceVentaDAO().eliminarVenta(ventaEliminar);
    }
}
