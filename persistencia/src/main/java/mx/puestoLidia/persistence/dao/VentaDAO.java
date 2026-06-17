package mx.puestoLidia.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.entity.*;
import mx.puestoLidia.persistence.persistence.AbstractDAO;

import java.math.BigDecimal;
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
            em.getTransaction().begin();
            em.persist(nuevaVenta);

            // procesar carrito
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

                // Vinculamos el ítem con los objetos reales
                item.setIdProducto(productoReal);
                item.setIdVenta(nuevaVenta);

                // Guardamos el ítem (Esto hará el INSERT en item_venta)
                em.persist(item);

                // ACTUALIZAMOS EL STOCK
                productoReal.setCantidad(productoReal.getCantidad() - item.getCantidad());
            }

            // SI LA VENTA ES A CREDITO (VEN-US2)
            if ("credito".equalsIgnoreCase(nuevaVenta.getTipo()) && nuevaVenta.getIdCliente() != null) {
                // Buscamos al cliente en la base de datos para relacionarlo
                Cliente clienteReal = em.find(Cliente.class, nuevaVenta.getIdCliente().getId());

                if (clienteReal != null) {
                    // calcular adeudo
                    BigDecimal deudaGenerada = nuevaVenta.getTotal().subtract(nuevaVenta.getMonto());

                    // actualizar adeudo
                    BigDecimal nuevoAdeudo = clienteReal.getAdeudo().add(deudaGenerada);
                    clienteReal.setAdeudo(nuevoAdeudo);

                    // Al modificar 'clienteReal', Hibernate hará el UPDATE automático
                } else {
                    throw new RuntimeException("El cliente asignado a la venta a crédito no existe en la base de datos.");
                }
            }

            // Confirmamos la transacción (Aquí se ejecutan los INSERTS de venta/items, UPDATE de stock y UPDATE de cliente)
            em.getTransaction().commit();
            System.out.println("¡Transacción completada con éxito en la Base de Datos!");

        } catch (Exception e) {
            if (em.getTransaction() != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error DAO: No se pudo completar la transacción de la venta.", e);
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

    // el método heredado del abstract devuelve Optional, si no existe la venta con el id recibido retorna null
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