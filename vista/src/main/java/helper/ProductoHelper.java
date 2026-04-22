package helper;

import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.negocio.integration.ServiceFacadeLocator;

import java.io.Serializable;

public class ProductoHelper implements Serializable {

    // alta de un producto
    public void guardarProducto(Producto producto){
        ServiceFacadeLocator.getInstanceFacadeProducto().guardarProducto(producto);
    }

    // obtener un producto por su id
    public Producto buscarProductoPorID(String id){
        return ServiceFacadeLocator.getInstanceFacadeProducto().buscarProductoPorID(id);
    }
}
