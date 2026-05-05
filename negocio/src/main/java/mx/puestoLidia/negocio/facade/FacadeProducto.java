package mx.puestoLidia.negocio.facade;

import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.negocio.delegate.DelegateProducto;

public class FacadeProducto {
    private final DelegateProducto delegateProducto;

    public FacadeProducto( ) {
        this.delegateProducto = new DelegateProducto();
    }

    // alta de un producto
    public void guardarProducto(Producto producto){
        delegateProducto.guardarProducto(producto);
    }

    // obtener un producto por su id
    public Producto buscarProductoPorID(String idBuscar){
        return delegateProducto.buscarProductoPorID(idBuscar);
    }

    // modificación de un producto (actualización)
    public void modificarProducto(Producto productoModificado){
        delegateProducto.modificarProducto(productoModificado);
    }
}
