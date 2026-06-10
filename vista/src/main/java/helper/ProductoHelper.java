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

    //obtener lista de los productos con umbral<=cantidad
    public List<Producto> obtenerReporteStockCritico() {
        return ServiceFacadeLocator.getInstanceFacadeProducto().obtenerReporteStockCritico();
    }
}
