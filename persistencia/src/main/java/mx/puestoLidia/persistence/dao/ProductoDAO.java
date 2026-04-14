package mx.puestoLidia.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.persistence.persistence.AbstractDAO;

public class ProductoDAO extends AbstractDAO<Producto> {

    private final EntityManager entityManager;

    public ProductoDAO(EntityManager em){
        super(Producto.class);
        this.entityManager = em;
    }

    // aquí colocar metodos de sus diagramas (los que necesitan para su US)

    @Override
    public EntityManager getEntityManager(){
        return entityManager;
    }
}
