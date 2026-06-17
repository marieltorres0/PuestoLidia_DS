package mx.puestoLidia.persistence.integration;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.persistence.persistence.HibernateUtil;
import mx.puestoLidia.persistence.dao.ProductoDAO;
import mx.puestoLidia.persistence.dao.VentaDAO;
import mx.puestoLidia.persistence.dao.ItemVentaDAO;
import mx.puestoLidia.persistence.dao.ClienteDAO;

/**
 * ServiceLocator - Patrón Singleton
 * Responsabilidad: Centralizar y mantener instancias únicas de todos los DAOs
 * Cada DAO se crea UNA SOLA VEZ en toda la aplicación
 */
public class ServiceLocator {

    private static ProductoDAO productoDAO;
    private static VentaDAO ventaDAO;
    private static ItemVentaDAO itemVentaDAO;
    private static ClienteDAO clienteDAO;

    // ============ PRODUCTO DAO ============
    public static ProductoDAO getInstanceProductoDAO() {
        if (productoDAO == null) {
            productoDAO = new ProductoDAO(getEntityManager());
        }
        return productoDAO;
    }

    // ============ VENTA DAO ============
    public static VentaDAO getInstanceVentaDAO() {
        if (ventaDAO == null) {
            ventaDAO = new VentaDAO(getEntityManager());
        }
        return ventaDAO;
    }

    // ============ ITEMVENTA DAO ============
    public static ItemVentaDAO getInstanceItemVentaDAO() {
        if (itemVentaDAO == null) {
            itemVentaDAO = new ItemVentaDAO(getEntityManager());
        }
        return itemVentaDAO;
    }

    // ============ CLIENTE DAO ============
    public static ClienteDAO getInstanceClienteDAO() {
        if (clienteDAO == null) {
            clienteDAO = new ClienteDAO(getEntityManager());
        }
        return clienteDAO;
    }

    // ============ OBTENER ENTITY MANAGER ============
    private static EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();
    }
}