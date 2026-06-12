package mx.puestoLidia.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.entity.Venta;
import mx.puestoLidia.persistence.persistence.AbstractDAO;

public class VentaDAO extends AbstractDAO<Venta> {
    private final EntityManager entityManager;

    public VentaDAO(EntityManager em){
        super(Venta.class);
        this.entityManager = em;
    }

    // CRUD
    // nueva venta
    public void guardarVenta(Venta nuevaVenta){
        try{
            save(nuevaVenta);
        } catch (Exception e) {
            throw new RuntimeException("Error DAO: No se pudo guardar la venta en la base de datos.",e);
        }
    }
    // el método heredado del abstract devuelve Optional
    // si no existe la venta con el id recibido retorna null
    public Venta buscarVentaPorId(Integer idBuscar){
        return find(idBuscar).orElse(null);
    }

    // modificar venta (se usará ninguna o rara vez)
    public void modificarVenta(Venta ventaModificar){
        try{
            update(ventaModificar);
        } catch (Exception e) {
            throw new RuntimeException("Error DAO: No se pudo modificar la venta en la base de datos.",e);
        }
    }

    // eliminar venta (rara vez su utilización)
    public void eliminarVenta(Venta ventaEliminar){
        try{
            delete(ventaEliminar);
        } catch (Exception e) {
            throw new RuntimeException("Error DAO: No se pudo eliminar la venta en la base de datos.",e);
        }
    }

    @Override
    public EntityManager getEntityManager(){ return entityManager;}
}
