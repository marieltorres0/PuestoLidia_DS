package mx.puestoLidia.negocio.integration;

import mx.puestoLidia.negocio.facade.FacadeProducto;
import mx.puestoLidia.negocio.facade.FacadeVenta;
import mx.puestoLidia.negocio.facade.FacadeItemVenta;
import mx.puestoLidia.negocio.facade.FacadeCliente;

/**
 * ServiceFacadeLocator - Patrón Singleton - Acceso único a todas las Facades
 */
public class ServiceFacadeLocator {

    private static FacadeProducto facadeProducto;
    private static FacadeVenta facadeVenta;
    private static FacadeItemVenta facadeItemVenta;
    private static FacadeCliente facadeCliente;

    public static FacadeProducto getInstanceFacadeProducto() {
        if (facadeProducto == null) {
            facadeProducto = new FacadeProducto();
        }
        return facadeProducto;
    }

    public static FacadeVenta getInstanceFacadeVenta() {
        if (facadeVenta == null) {
            facadeVenta = new FacadeVenta();
        }
        return facadeVenta;
    }

    public static FacadeItemVenta getInstanceFacadeItemVenta() {
        if (facadeItemVenta == null) {
            facadeItemVenta = new FacadeItemVenta();
        }
        return facadeItemVenta;
    }

    public static FacadeCliente getInstanceFacadeCliente() {
        if (facadeCliente == null) {
            facadeCliente = new FacadeCliente();
        }
        return facadeCliente;
    }
}