package mx.puestoLidia.negocio.integration;

import mx.puestoLidia.negocio.facade.FacadeProducto;

public class ServiceFacadeLocator {
    private static FacadeProducto facadeProducto;

    public static FacadeProducto getInstanceFacadeProducto(){
        if (facadeProducto == null){
            facadeProducto = new FacadeProducto();
        }
        return facadeProducto;
    }
}
