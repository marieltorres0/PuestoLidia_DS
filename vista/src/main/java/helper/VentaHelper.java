package helper;

import mx.puestoLidia.entity.ItemVenta;
import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.entity.Venta;
import mx.puestoLidia.negocio.integration.ServiceFacadeLocator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VentaHelper {

    // ==== REGISTRAR LA VENTA COMPLETA TRANSACCIONAL ====
    public void registrarVentaCompleta(Venta nuevaVenta, List<ItemVenta> carrito) {
        ServiceFacadeLocator.getInstanceFacadeVenta().registrarVentaCompleta(nuevaVenta, carrito);
    }

    // === SUMAR / AGREGAR PRODUCTOS AL CARRITO ===
    public void agregrarOSumarProductoEnCarrito(String idProducto, int cantidadSolicitada, List<ItemVenta> carritoActual){
        ServiceFacadeLocator.getInstanceFacadeVenta().agregrarOSumarProductoEnCarrito(idProducto, cantidadSolicitada, carritoActual);
    }

    // ==== RESTAR / QUITAR PRODUCTOS DEL CARRITO ====
    public void restarOQuitarDelCarrito(String idProducto, List<ItemVenta> carritoActual){
        ServiceFacadeLocator.getInstanceFacadeVenta().restarOQuitarDelCarrito(idProducto, carritoActual);
    }

    // CALCULAR TOTAL DE LA VENTA
    public BigDecimal calcularTotalCarrito(List<ItemVenta> carritoActual){
        return ServiceFacadeLocator.getInstanceFacadeVenta().calcularTotalCarrito(carritoActual);
    }

    // EVALUAR STOCK CRÍTICO (VEN.US3)
    public List<Producto> evaluarStockCritico(List<ItemVenta> carrito) {
        return ServiceFacadeLocator.getInstanceFacadeVenta().evaluarStockCritico(carrito);
    }

    // ==== CRUD Básico ====
    public void guardarVenta(Venta ventaGuardar) { ServiceFacadeLocator.getInstanceFacadeVenta().guardarVenta(ventaGuardar);}

    public Venta buscarVentaPorID(int idVenta) { return ServiceFacadeLocator.getInstanceFacadeVenta().buscarVentaPorID(idVenta);}

    public void modificarVenta(Venta ventaModificar) { ServiceFacadeLocator.getInstanceFacadeVenta().modificarVenta(ventaModificar);}

    public void eliminarVenta(Venta ventaEliminar) { ServiceFacadeLocator.getInstanceFacadeVenta().eliminarVenta(ventaEliminar);}
}
