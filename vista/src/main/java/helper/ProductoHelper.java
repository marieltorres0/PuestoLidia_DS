package helper;

import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.negocio.integration.ServiceFacadeLocator;

import java.io.Serializable;
import java.util.List;

public class ProductoHelper implements Serializable {

    // alta de un producto
    public void guardarProducto(Producto producto){
        ServiceFacadeLocator.getInstanceFacadeProducto().guardarProducto(producto);
    }

    // obtener un producto por su id
    public Producto buscarProductoPorID(String id){
        return ServiceFacadeLocator.getInstanceFacadeProducto().buscarProductoPorID(id);
    }

    // modificar un producto
    public void modificarProducto(Producto productoModificado){
        ServiceFacadeLocator.getInstanceFacadeProducto().modificarProducto(productoModificado);
    }

    // ----- PARA CONSULTA -----

    // Obtener todos los productos ordenados (Para llenar la tabla al inicio)
    public List<Producto> obtenerInventarioOrdenado() {
        return ServiceFacadeLocator.getInstanceFacadeProducto().obtenerInventarioOrdenado();
    }

    // Buscar productos por nombre (Para la barra de búsqueda)
    public List<Producto> filtrarPorIdOPorNombre(String filtro) {
        return ServiceFacadeLocator.getInstanceFacadeProducto().filtrarPorIdOPorNombre(filtro);
    }

    // ACTUALIZAR PRODUCTO (NUEVO: Para guardar la suma cuando hagamos una Entrada)
    public void actualizarProducto(Producto producto) {
        ServiceFacadeLocator.getInstanceFacadeProducto().actualizarProducto(producto);
    }

    // eliminar producto
    public void eliminarProducto(Producto productoELiminar){
        ServiceFacadeLocator.getInstanceFacadeProducto().eliminarProducto(productoELiminar);
    }

    //obtener lista de los productos con umbral<=cantidad
    public List<Producto> obtenerReporteStockCritico() {
        return ServiceFacadeLocator.getInstanceFacadeProducto().obtenerReporteStockCritico();
    }
}
