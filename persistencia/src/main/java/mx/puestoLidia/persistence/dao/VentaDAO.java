package mx.puestoLidia.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.entity.Venta;
import mx.puestoLidia.persistence.persistence.AbstractDAO;
import mx.puestoLidia.persistence.persistence.HibernateUtil;


public class VentaDAO extends AbstractDAO<Venta> {



    public VentaDAO() {
        super(Venta.class);
    }

    // 2. Tomamos la conexión directamente del HibernateUtil
    @Override
    protected EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();
    }




}