package mx.puestoLidia.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.entity.Venta;
import mx.puestoLidia.persistence.persistence.AbstractDAO;

public class VentaDAO extends AbstractDAO<Venta> {

    private final EntityManager entityManager;

    public VentaDAO(EntityManager entityManager) {
        super(Venta.class);
        this.entityManager = entityManager;
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }
}