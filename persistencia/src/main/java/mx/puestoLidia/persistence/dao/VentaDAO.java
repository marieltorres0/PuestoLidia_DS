package mx.puestoLidia.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.entity.*;
import mx.puestoLidia.persistence.persistence.AbstractDAO;

import java.util.List;

public class VentaDAO extends AbstractDAO<Venta> {
    private final EntityManager entityManager;

    public VentaDAO(EntityManager em){
        super(Venta.class);
        this.entityManager = em;
    }

    // metodo principal
    public void registrarVentaCompletaTransaccional(Venta nuevaVenta, List<ItemVenta> carrito) {
        EntityManager em = getEntityManager();

        try {
            // 1. Iniciamos la transacción
            em.getTransaction().begin();



            // 3. Guardamos la cabecera de la venta
            em.persist(nuevaVenta);

            // 4. Procesamos el carrito
            for (ItemVenta item : carrito) {
                if (item.getId() == null) {
                    item.setId(new ItemVentaId());
                }

                // OBTENEMOS EL PRODUCTO REAL DE LA BASE DE DATOS
                // Esto le avisa a Hibernate que el producto YA EXISTE
                Producto productoReal = em.find(Producto.class, item.getIdProducto().getIdProducto());

                if (productoReal == null) {
                    throw new RuntimeException("El producto con ID " + item.getIdProducto().getIdProducto() + " no existe.");
                }

                // Vinculamos el ítem con los objetos reales de esta sesión
                item.setIdProducto(productoReal);
                item.setIdVenta(nuevaVenta);

                // Guardamos el ítem (Esto hará el INSERT en item_venta)
                em.persist(item);

                // ACTUALIZAMOS EL STOCK
                // Al modificar el objeto 'productoReal' que sacamos con em.find,
                // Hibernate hará el UPDATE en la tabla producto automáticamente al hacer commit.
                productoReal.setCantidad(productoReal.getCantidad() - item.getCantidad());
            }

            // 5. Confirmamos la transacción (Aquí se ejecutan los INSERTS de venta/items y el UPDATE de stock)
            em.getTransaction().commit();
            System.out.println("¡Transacción completada con éxito en la Base de Datos!");

        } catch (Exception e) {
            if (em.getTransaction() != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error DAO: No se pudo completar la transacción de la venta.", e);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
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
    // el método heredado del abstract devuelve Optiona, si no existe la venta con el id recibido retorna null
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
