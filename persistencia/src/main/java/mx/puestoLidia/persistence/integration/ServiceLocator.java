package mx.puestoLidia.persistence.integration;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.persistence.dao.ItemVentaDAO;
import mx.puestoLidia.persistence.dao.ProductoDAO;
import mx.puestoLidia.persistence.dao.VentaDAO;
import mx.puestoLidia.persistence.persistence.HibernateUtil;

public class ServiceLocator {

    private static ProductoDAO productoDAO;
    private static VentaDAO ventaDAO;
    private static ItemVentaDAO itemVentaDAO;

    private static EntityManager getEntityManager(){
        return HibernateUtil.getEntityManager();
    }

    public static ProductoDAO getInstanceProductoDAO(){
        if(productoDAO == null){
            productoDAO = new ProductoDAO(getEntityManager());
        }
        return productoDAO;
    }

    public static VentaDAO getInstanceVentaDAO(){
        if(ventaDAO == null){
            ventaDAO = new VentaDAO(getEntityManager());
        }
        return ventaDAO;
    }

    public static ItemVentaDAO getInstanceItemVentaDAO(){
        if(itemVentaDAO == null){
            itemVentaDAO = new ItemVentaDAO(getEntityManager());
        }
        return itemVentaDAO;
    }

}