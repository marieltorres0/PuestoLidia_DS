package mx.puestoLidia.negocio.delegate;

import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.persistence.integration.ServiceLocator;

public class DelegateProducto {

    // alta de un producto
    public void guardarProducto(Producto producto){
        ServiceLocator.getInstanceProductoDAO().guardarProducto(producto);
    }

    // obtener un producto por su id
    public Producto buscarProductoPorID(String idBuscar){
        return ServiceLocator.getInstanceProductoDAO().buscarProductoPorID(idBuscar);
    }
}
