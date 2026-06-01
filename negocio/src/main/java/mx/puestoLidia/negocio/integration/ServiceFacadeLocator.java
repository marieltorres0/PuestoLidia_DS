package mx.puestoLidia.negocio.integration;

import mx.puestoLidia.negocio.facade.FacadeProducto;
import mx.puestoLidia.negocio.facade.FacadeInventario;
import mx.puestoLidia.negocio.facade.FacadeReporte;
import mx.puestoLidia.negocio.facade.FacadeVenta;
import mx.puestoLidia.negocio.facade.FacadeAutentificacion; // <-- IMPORT SOLUCIONADO
import mx.puestoLidia.persistence.dao.ReporteDAO;

public class ServiceFacadeLocator {

    private static FacadeProducto facadeProducto;
    private static FacadeInventario facadeInventario;
    private static FacadeReporte facadeReporte;
    private static FacadeAutentificacion facadeAutentificacion;
    private static FacadeVenta facadeVenta;

    public static FacadeAutentificacion getInstanceFacadeAutentificacion() {
        if (facadeAutentificacion == null) {
            facadeAutentificacion = new FacadeAutentificacion();
        }
        return facadeAutentificacion;
    }

    public static FacadeProducto getInstanceFacadeProducto(){
        if (facadeProducto == null){
            facadeProducto = new FacadeProducto();
        }
        return facadeProducto;
    }

    public static FacadeInventario getInstanceFacadeInventario(){
        if (facadeInventario == null){
            facadeInventario = new FacadeInventario();
        }
        return facadeInventario;
    }

    public static FacadeReporte getInstanceFacadeReporte() {
        if (facadeReporte == null) {
            facadeReporte = new FacadeReporte();
        }
        return facadeReporte;
    }

    public static FacadeVenta getInstanceFacadeVenta() {
        if (facadeVenta == null) {
            facadeVenta = new FacadeVenta();
        }
        return facadeVenta;
    }

    private static ReporteDAO reporteDAO;
    public static ReporteDAO getInstanceReporteDAO() {
        if (reporteDAO == null) {
            reporteDAO = new ReporteDAO();
        }
        return reporteDAO;
    }
}