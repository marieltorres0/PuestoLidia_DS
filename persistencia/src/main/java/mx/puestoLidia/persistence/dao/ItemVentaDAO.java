package mx.puestoLidia.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.entity.ItemVenta;
import mx.puestoLidia.entity.ItemVentaId;
import mx.puestoLidia.persistence.persistence.AbstractDAO;

public class ItemVentaDAO extends AbstractDAO<ItemVenta> {

    private final EntityManager entityManager;

    public ItemVentaDAO(EntityManager em){
        super(ItemVenta.class);
        this.entityManager = em;
    }

    // CRUD
    // nueva venta
    public void guardarItemVenta(ItemVenta nuevoItemVenta){
        try{
            save(nuevoItemVenta);
        } catch (Exception e) {
            throw new RuntimeException("Error DAO: No se pudo guardar el ItemVenta en la base de datos.",e);
        }
    }
    // el método heredado del abstract devuelve Optional
    // si no existe el itemVenta con el id recibido retorna null
    public ItemVenta buscarItemVentaPorId(ItemVentaId itemVentaId){
        return find(itemVentaId).orElse(null);
    }

    // modificar (se usará ninguna o rara vez)
    public void modificarItemVenta(ItemVenta itemVentaModificar){
        try{
            update(itemVentaModificar);
        } catch (Exception e) {
            throw new RuntimeException("Error DAO: No se pudo modificar el ItemVenta en la base de datos.",e);
        }
    }

    // eliminar (rara vez su utilización)
    public void eliminarItemVenta(ItemVenta itemVentaEliminar){
        try{
            delete(itemVentaEliminar);
        } catch (Exception e) {
            throw new RuntimeException("Error DAO: No se pudo eliminar el ItemVenta en la base de datos.",e);
        }
    }
    @Override
    public EntityManager getEntityManager(){
        return entityManager;
    }
}
