package mx.puestoLidia.negocio.integration;

import mx.puestoLidia.negocio.facade.FacadeItemVenta;
import mx.puestoLidia.negocio.facade.FacadeProducto;
import mx.puestoLidia.negocio.facade.FacadeVenta;

public class ServiceFacadeLocator {

    private static FacadeProducto facadeProducto;
    private static FacadeVenta facadeVenta;
    private static FacadeItemVenta facadeItemVenta;

    public static FacadeProducto getInstanceFacadeProducto(){
        if (facadeProducto == null){
            facadeProducto = new FacadeProducto();
        }
        return facadeProducto;
    }

    public static FacadeVenta getInstanceFacadeVenta(){
        if (facadeVenta == null){
            facadeVenta = new FacadeVenta();
        }
        return facadeVenta;
    }
    public static FacadeItemVenta getInstanceFacadeItemVenta(){
        if (facadeItemVenta == null){
            facadeItemVenta = new FacadeItemVenta();
        }
        return facadeItemVenta;
    }

}