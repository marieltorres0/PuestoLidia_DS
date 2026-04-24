package mx.puestoLidia.negocio.integration;

import mx.puestoLidia.negocio.facade.FacadeProducto;
import mx.puestoLidia.negocio.facade.FacadeInventario; // <-- NUEVO IMPORT

public class ServiceFacadeLocator {

    private static FacadeProducto facadeProducto;
//98
    // <-- NUEVA VARIABLE -->
    private static FacadeInventario facadeInventario;

    public static FacadeProducto getInstanceFacadeProducto(){
        if (facadeProducto == null){
            facadeProducto = new FacadeProducto();
        }
        return facadeProducto;
    }

    // <-- NUEVO METODO -->
    public static FacadeInventario getInstanceFacadeInventario(){
        if (facadeInventario == null){
            facadeInventario = new FacadeInventario();
        }
        return facadeInventario;
    }
}