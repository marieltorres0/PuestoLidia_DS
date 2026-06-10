package mx.puestoLidia.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.persistence.persistence.AbstractDAO;

import java.util.List;

public class ProductoDAO extends AbstractDAO<Producto> {

    private final EntityManager entityManager;

    public ProductoDAO(EntityManager em){
        super(Producto.class);
        this.entityManager = em;
    }

    public void guardarProducto(Producto nuevoProducto){
        try {
            save(nuevoProducto);
        } catch (Exception e) {
            throw new RuntimeException("Error DAO: No se pudo guardar en la base de datos.");
        }
    }

    // el método heredado del abstract devuelve Optional
    // si no existe el producto con el id recibido retorna null
    public Producto buscarProductoPorID(String idBuscar){
        return find(idBuscar).orElse(null);
    }

    // aquí colocar metodos de sus diagramas (los que necesitan para su US)

    public List<Producto> obtenerReporteStockCritico() {
        String jpql = "SELECT p FROM Producto p WHERE p.cantidad <= p.umbral ORDER BY p.cantidad ASC";

        return execute(em -> {
            em.clear(); // Limpiamos caché
            return em.createQuery(jpql, Producto.class).getResultList();
        });
    }

    @Override
    public EntityManager getEntityManager(){
        return entityManager;
    }
}
