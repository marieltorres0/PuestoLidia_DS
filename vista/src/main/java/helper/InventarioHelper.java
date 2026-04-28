package helper;

import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.negocio.integration.ServiceFacadeLocator;

import java.io.Serializable;
import java.util.List;


public class InventarioHelper implements Serializable {

    // Obtener todos los productos ordenados (Para llenar la tabla al inicio)
    public List<Producto> obtenerInventarioOrdenado() {
        return ServiceFacadeLocator.getInstanceFacadeInventario().obtenerInventarioOrdenado();
    }

    // Buscar productos por nombre (Para la barra de búsqueda)
    public List<Producto> buscarPorNombre(String filtro) {
        return ServiceFacadeLocator.getInstanceFacadeInventario().buscarPorNombre(filtro);
    }

    // ACTUALIZAR PRODUCTO (NUEVO: Para guardar la suma cuando hagamos una Entrada)
    public void actualizarProducto(Producto producto) {
        ServiceFacadeLocator.getInstanceFacadeInventario().actualizarProducto(producto);
    }
}