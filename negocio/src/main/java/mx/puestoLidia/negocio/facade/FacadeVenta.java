package mx.puestoLidia.negocio.facade;

import mx.puestoLidia.entity.ItemVenta;
import mx.puestoLidia.entity.ItemVentaId;
import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.entity.Usuario;
import mx.puestoLidia.entity.Venta;
import mx.puestoLidia.persistence.persistence.HibernateUtil;
import mx.puestoLidia.persistence.integration.ServiceLocator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class FacadeVenta {

    public void procesarAltaVenta(List<ItemVenta> listaCompra, BigDecimal total,
                                  BigDecimal monto, BigDecimal cambio,
                                  boolean esCredito) throws Exception {

        // Abrimos UNA SOLA sesión y transacción para todo el proceso
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // 1. Crear y guardar la venta principal
            Venta nuevaVenta = new Venta();
            nuevaVenta.setFechaHora(Instant.now());
            nuevaVenta.setTotal(total);
            nuevaVenta.setMonto(monto != null ? monto : total);
            nuevaVenta.setCambio(cambio != null ? cambio : BigDecimal.ZERO);
            nuevaVenta.setTipo(esCredito ? "credito" : "contado");

            // Referencia al cajero sin cargar la entidad completa
            Usuario cajero = em.getReference(Usuario.class, 1);
            nuevaVenta.setIdUsuario(cajero);

            em.persist(nuevaVenta);
            em.flush(); // Forzamos que genere el ID de la venta

            // 2. Procesar cada item dentro de la MISMA sesión
            for (ItemVenta item : listaCompra) {

                String idProducto = item.getIdProducto().getIdProducto();

                // Buscamos el producto dentro de esta misma sesión
                Producto prodBD = em.find(Producto.class, idProducto);

                if (prodBD == null) {
                    throw new Exception("Producto no encontrado: " + idProducto);
                }

                if (prodBD.getCantidad() < item.getCantidad()) {
                    throw new Exception("Stock insuficiente para: " + prodBD.getNombre());
                }

                // Restamos el inventario
                prodBD.setCantidad(prodBD.getCantidad() - item.getCantidad());
                // No necesitamos merge porque prodBD ya está en esta sesión (managed)

                // Construir la llave compuesta
                ItemVentaId llaveCompuesta = new ItemVentaId();
                llaveCompuesta.setIdVenta(nuevaVenta.getId());
                llaveCompuesta.setIdProducto(idProducto);

                // Asignar y guardar el item usando el prodBD de esta misma sesión
                item.setId(llaveCompuesta);
                item.setIdVenta(nuevaVenta);
                item.setIdProducto(prodBD);

                em.persist(item);
            }

            tx.commit(); // Todo sale bien, confirmamos

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback(); // Si algo falla, deshacemos todo
            }
            throw new Exception("Error al procesar la venta: " + e.getMessage());
        } finally {
            em.close(); // Siempre cerramos la sesión
        }
    }

    public void procesarBajaVenta(int idVenta) throws Exception {

        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Venta venta = em.find(Venta.class, idVenta);
            if (venta == null) {
                throw new Exception("Venta no encontrada con ID: " + idVenta);
            }

            if (venta.getItemVentas() != null) {
                for (ItemVenta item : venta.getItemVentas()) {
                    Producto prodBD = em.find(Producto.class,
                            item.getIdProducto().getIdProducto());
                    if (prodBD != null) {
                        prodBD.setCantidad(prodBD.getCantidad() + item.getCantidad());
                    }
                    em.remove(item);
                }
            }

            em.remove(venta);
            tx.commit();

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new Exception("Error al cancelar la venta: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}