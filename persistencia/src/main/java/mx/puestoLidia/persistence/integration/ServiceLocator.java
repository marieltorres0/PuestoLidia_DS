package mx.puestoLidia.persistence.integration;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.persistence.dao.ProductoDAO;
import mx.puestoLidia.persistence.dao.InventarioDAO; // <-- 1. ASEGÚRATE DE IMPORTAR ESTO
import mx.puestoLidia.persistence.persistence.HibernateUtil;

public class ServiceLocator {

    private static ProductoDAO productoDAO;

    // <-- 2. AGREGA ESTA VARIABLE NUEVA -->
    private static InventarioDAO inventarioDAO;


    private static EntityManager getEntityManager(){
        return HibernateUtil.getEntityManager();
    }

    public static ProductoDAO getInstanceProductoDAO(){
        if(productoDAO == null){
            productoDAO = new ProductoDAO(getEntityManager());
        }
        return productoDAO;
    }

    // <-- 3. AGREGA ESTE MÉTODO COMPLETO NUEVO -->
    public static InventarioDAO getInstanceInventarioDAO(){
        if(inventarioDAO == null){
            inventarioDAO = new InventarioDAO(getEntityManager());
        }
        return inventarioDAO;
    }
}