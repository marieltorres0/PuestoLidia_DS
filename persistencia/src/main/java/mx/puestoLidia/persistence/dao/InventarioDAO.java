package mx.puestoLidia.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.persistence.persistence.AbstractDAO;
import java.util.List;

public class InventarioDAO extends AbstractDAO<Producto> {

    private final EntityManager entityManager;

    public InventarioDAO(EntityManager em) {
        super(Producto.class);
        this.entityManager = em;
    }

    // Obtener todos los productos ordenados de la A a la Z
    public List<Producto> obtenerInventarioOrdenado() {
        String jpql = "SELECT p FROM Producto p ORDER BY p.nombre ASC";
        return execute(em -> {
            em.clear(); // <-- ESTA ES LA LÍNEA MÁGICA QUE LIMPIA LA MEMORIA
            return em.createQuery(jpql, Producto.class).getResultList();
        });
    }

    // Buscar productos por nombre
    public List<Producto> buscarPorNombre(String filtro) {
        String jpql = "SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(:filtro) ORDER BY p.nombre ASC";
        return execute(em -> {
            em.clear(); // <-- LIMPIA LA MEMORIA AQUÍ TAMBIÉN
            return em.createQuery(jpql, Producto.class)
                    .setParameter("filtro", "%" + filtro + "%")
                    .getResultList();
        });
    }


    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
}