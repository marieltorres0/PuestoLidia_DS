package mx.puestoLidia.persistence.integration;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.persistence.dao.ProductoDAO;
import mx.puestoLidia.persistence.dao.InventarioDAO;
import mx.puestoLidia.persistence.dao.ReporteDAO;
import mx.puestoLidia.persistence.dao.VentaDAO;
import mx.puestoLidia.persistence.dao.ItemVentaDAO; // <-- ¡ESTA ES LA LÍNEA QUE FALTABA!
import mx.puestoLidia.persistence.persistence.HibernateUtil;

public class ServiceLocator {

    private static ProductoDAO productoDAO;
    private static ReporteDAO reporteDAO;
    private static InventarioDAO inventarioDAO;
    private static VentaDAO ventaDAO;
    private static ItemVentaDAO itemVentaDAO;

    private static EntityManager getEntityManager(){
        return HibernateUtil.getEntityManager();
    }

    public static ItemVentaDAO getInstanceItemVentaDAO() {
        if(itemVentaDAO == null){
            itemVentaDAO = new ItemVentaDAO(getEntityManager());
        }
        return itemVentaDAO;
    }

    public static ProductoDAO getInstanceProductoDAO(){
        if(productoDAO == null){
            productoDAO = new ProductoDAO(getEntityManager());
        }
        return productoDAO;
    }

    public static InventarioDAO getInstanceInventarioDAO(){
        if(inventarioDAO == null){
            inventarioDAO = new InventarioDAO(getEntityManager());
        }
        return inventarioDAO;
    }

    public static ReporteDAO getInstanceReporteDAO() {
        if (reporteDAO == null) {
            reporteDAO = new ReporteDAO();
        }
        return reporteDAO;
    }

    public static VentaDAO getInstanceVentaDAO(){
        if(ventaDAO == null){
            ventaDAO = new VentaDAO(getEntityManager());
        }
        return ventaDAO;
    }
}