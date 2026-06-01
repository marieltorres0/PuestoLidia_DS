package mx.puestoLidia.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.entity.ItemVenta;
import mx.puestoLidia.persistence.persistence.AbstractDAO;

public class ItemVentaDAO extends AbstractDAO<ItemVenta> {

    private final EntityManager entityManager;

    public ItemVentaDAO(EntityManager entityManager) {
        super(ItemVenta.class);
        this.entityManager = entityManager;
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }
}