package mx.puestoLidia.negocio.facade;

import mx.puestoLidia.entity.ItemVenta;
import mx.puestoLidia.entity.Venta;
import mx.puestoLidia.negocio.delegate.DelegateVenta;

import java.math.BigDecimal;
import java.util.List;

public class FacadeVenta {
    private final DelegateVenta delegateVenta;

    public FacadeVenta(){
        this.delegateVenta = new DelegateVenta();
    }

    // ==== REGISTRAR LA VENTA COMPLETA TRANSACCIONAL ====
    public void registrarVentaCompleta(Venta nuevaVenta, List<ItemVenta> carrito) {
        delegateVenta.registrarVentaCompleta(nuevaVenta, carrito);
    }

    // === SUMAR / AGREGAR PRODUCTOS AL CARRITO ===
    public void agregrarOSumarProductoEnCarrito(String idProducto, int cantidadSolicitada, List<ItemVenta> carritoActual){
        delegateVenta.agregarOActualizarProductoEnCarrito(idProducto,cantidadSolicitada,carritoActual);
    }

    // ==== RESTAR / QUITAR PRODUCTOS DEL CARRITO ====
    public void restarOQuitarDelCarrito(String idProducto, List<ItemVenta> carritoActual){
        delegateVenta.eliminarOQuitarProductoDelCarrito(idProducto, carritoActual);
    }

    // CALCULAR TOTAL DE LA VENTA
    public BigDecimal calcularTotalCarrito(List<ItemVenta> carritoActual){
        return delegateVenta.calcularTotalCarrito(carritoActual);
    }

    // ==== CRUD Básico ====
    public void guardarVenta(Venta ventaGuardar) {
        delegateVenta.guardarVenta(ventaGuardar);
    }

    public Venta buscarVentaPorID(int idVenta) { return delegateVenta.buscarVentaPorID(idVenta);}

    public void modificarVenta(Venta ventaModificar) {
        delegateVenta.modificarVenta(ventaModificar);
    }

    public void eliminarVenta(Venta ventaEliminar) {
        delegateVenta.eliminarVenta(ventaEliminar);
    }
}
