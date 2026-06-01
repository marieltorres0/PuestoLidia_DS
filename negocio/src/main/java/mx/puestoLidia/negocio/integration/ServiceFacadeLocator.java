package mx.puestoLidia.negocio.integration;

import mx.puestoLidia.negocio.facade.FacadeProducto;
import mx.puestoLidia.negocio.facade.FacadeInventario;
import mx.puestoLidia.negocio.facade.FacadeReporte;
import mx.puestoLidia.negocio.facade.FacadeVenta; // <-- IMPORTAR
import mx.puestoLidia.persistence.dao.ReporteDAO;

public class ServiceFacadeLocator {

    private static FacadeProducto facadeProducto;
    private static FacadeInventario facadeInventario;
    private static FacadeReporte facadeReporte;

    // <-- NUEVA VARIABLE PARA VENTA -->
    private static FacadeVenta facadeVenta;

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

    // <-- NUEVO MÉTODO PARA VENTA -->
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