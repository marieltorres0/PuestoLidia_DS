package mx.puestoLidia.persistence.integration;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.persistence.dao.ProductoDAO;
import mx.puestoLidia.persistence.persistence.HibernateUtil;

public class ServiceLocator {

    private  static ProductoDAO productoDAO;

    private static EntityManager getEntityManager(){
        return HibernateUtil.getEntityManager();
    }

    public static ProductoDAO getInstanceProductoDAO(){
        if(productoDAO == null){
            productoDAO = new ProductoDAO(getEntityManager());
            return productoDAO;
        } else {
            return productoDAO;
        }
    }
}
