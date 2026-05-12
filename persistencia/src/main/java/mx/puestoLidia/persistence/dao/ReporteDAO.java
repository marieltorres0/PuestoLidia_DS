package mx.puestoLidia.persistence.dao;



import jakarta.persistence.EntityManager;
import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.persistence.persistence.AbstractDAO;
import mx.puestoLidia.persistence.persistence.HibernateUtil;
import java.util.List;

/**
 * @author Eduardo Avitia Castro
 */
public class ReporteDAO extends AbstractDAO<Producto> {

    // 1. Le decimos al AbstractDAO que vamos a trabajar con Productos
    public ReporteDAO() {
        super(Producto.class);
    }

    // 2. Le pasamos el EntityManager usando la clase de tu compañero
    @Override
    protected EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();
    }

    // 3. Método para la Historia de Usuario PBI-PROD-US6
    public List<Producto> obtenerReporteStockCritico() {
        String jpql = "SELECT p FROM Producto p WHERE p.cantidad <= p.umbral ORDER BY p.cantidad ASC";

        return execute(em -> {
            em.clear(); // Limpiamos caché
            return em.createQuery(jpql, Producto.class).getResultList();
        });
    }
}