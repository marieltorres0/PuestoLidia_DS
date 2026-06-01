package mx.puestoLidia.persistence.integration;

import mx.puestoLidia.persistence.dao.ItemVentaDAO;
import mx.puestoLidia.persistence.dao.ProductoDAO;
import mx.puestoLidia.persistence.dao.VentaDAO;
import mx.puestoLidia.persistence.dao.UsuarioDAO;
import mx.puestoLidia.persistence.dao.InventarioDAO; // <-- AGREGAR ESTE IMPORT

public class ServiceLocator {

    private static VentaDAO ventaDAO;
    private static ProductoDAO productoDAO;
    private static ItemVentaDAO itemVentaDAO;
    private static UsuarioDAO usuarioDAO;
    private static InventarioDAO inventarioDAO; // <-- NUEVA VARIABLE

    /**
     * Obtiene la instancia única de VentaDAO
     */
    public static VentaDAO getInstanceVentaDAO() {
        if (ventaDAO == null) {
            ventaDAO = new VentaDAO();
        }
        return ventaDAO;
    }

    /**
     * Obtiene la instancia única de ProductoDAO
     */
    public static ProductoDAO getInstanceProductoDAO() {
        if (productoDAO == null) {
            productoDAO = new ProductoDAO();
        }
        return productoDAO;
    }

    /**
     * Obtiene la instancia única de ItemVentaDAO
     */
    public static ItemVentaDAO getInstanceItemVentaDAO() {
        if (itemVentaDAO == null) {
            itemVentaDAO = new ItemVentaDAO();
        }
        return itemVentaDAO;
    }
    // Asegúrate de que esta variable y este método estén adentro
    private static mx.puestoLidia.persistence.dao.ReporteDAO reporteDAO;

    public static mx.puestoLidia.persistence.dao.ReporteDAO getInstanceReporteDAO() {
        if (reporteDAO == null) {
            reporteDAO = new mx.puestoLidia.persistence.dao.ReporteDAO();
        }
        return reporteDAO;
    }
    /**
     * Obtiene la instancia única de UsuarioDAO para el manejo de sesiones
     */
    public static UsuarioDAO getInstanceUsuarioDAO() {
        if (usuarioDAO == null) {
            usuarioDAO = new UsuarioDAO();
        }
        return usuarioDAO;
    }

    /**
     * Obtiene la instancia única de InventarioDAO <-- NUEVO MÉTODO
     */
    public static InventarioDAO getInstanceInventarioDAO() {
        if (inventarioDAO == null) {
            inventarioDAO = new InventarioDAO();
        }
        return inventarioDAO;
    }


}