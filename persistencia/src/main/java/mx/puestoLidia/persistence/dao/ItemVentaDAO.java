package mx.puestoLidia.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.entity.ItemVenta;
import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.persistence.persistence.AbstractDAO;
import mx.puestoLidia.persistence.persistence.HibernateUtil;

public class ItemVentaDAO extends AbstractDAO<ItemVenta> {

    // 1. Constructor vacío
    public ItemVentaDAO() {
        super(ItemVenta.class);
    }

    // 2. Tomamos la conexión del HibernateUtil
    @Override
    protected EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();
    }

    // ========================================================
    // MÉTODO PARA GUARDAR EVITANDO EL ERROR DE LAS DOS SESIONES
    // ========================================================
    public void guardarItemLimpio(ItemVenta itemNuevo) {
        execute(em -> {

            // Verificamos que el item tenga un producto asignado
            if (itemNuevo.getIdProducto() != null) {

                // Obtenemos el ID del producto (cambia getId() por el nombre de tu getter en Producto si es necesario)
                Object id = itemNuevo.getIdProducto();

                // Buscamos el producto fresco en la base de datos
                Producto productoFresco = em.find(Producto.class, id);

                // Reemplazamos el producto viejo por el fresco
                itemNuevo.setIdProducto(productoFresco);
            }

            // Guardamos el ItemVenta sin que Hibernate colapse
            em.persist(itemNuevo);

            return null;
        });
    }
}
