package mx.puestoLidia.negocio.delegate;

import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.persistence.integration.ServiceLocator;

import java.util.List;

public class DelegateProducto {

    // alta de un producto
    public void guardarProducto(Producto producto){
        ServiceLocator.getInstanceProductoDAO().guardarProducto(producto);
    }

    // obtener un producto por su id
    public Producto buscarProductoPorID(String idBuscar){
        return ServiceLocator.getInstanceProductoDAO().buscarProductoPorID(idBuscar);
    }

    // modificación de un producto (actualizarlo)
    public void modificarProducto(Producto productoModificado){
        ServiceLocator.getInstanceProductoDAO().modificarProducto(productoModificado);
    }

    // Obtener todos los productos ordenados
    public List<Producto> obtenerInventarioOrdenado() {
        return ServiceLocator.getInstanceProductoDAO().obtenerInventarioOrdenado();
    }

    // Buscar productos por coincidencia de nombre
    public List<Producto> buscarPorNombre(String filtro) {
        return ServiceLocator.getInstanceProductoDAO().buscarPorNombre(filtro);
    }
    public void actualizarProducto(Producto producto) {
        ServiceLocator.getInstanceProductoDAO().update(producto);
    }

    // eliminar un producto
    public void eliminarProducto(Producto productoEliminar){
        ServiceLocator.getInstanceProductoDAO().eliminarProducto(productoEliminar);
    }

    //obtener lista de los productos con umbral<=cantidad
    public List<Producto> obtenerReporteStockCritico() {
        return ServiceLocator.getInstanceProductoDAO().obtenerReporteStockCritico();
    }
}
