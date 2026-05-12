package mx.puestoLidia.negocio.integration;

import mx.puestoLidia.negocio.facade.FacadeProducto;
import mx.puestoLidia.negocio.facade.FacadeInventario;
import mx.puestoLidia.negocio.facade.FacadeReporte;
import mx.puestoLidia.persistence.dao.ReporteDAO;

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

    private static FacadeReporte facadeReporte;

    public static FacadeReporte getInstanceFacadeReporte() {
        if (facadeReporte == null) {
            facadeReporte = new FacadeReporte();
        }
        return facadeReporte;
    }
    private static ReporteDAO reporteDAO;

    public static ReporteDAO getInstanceReporteDAO() {
        if (reporteDAO == null) {
            reporteDAO = new ReporteDAO();
        }
        return reporteDAO;
    }
}