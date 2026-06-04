package mx.puestoLidia.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.persistence.persistence.AbstractDAO;
import mx.puestoLidia.persistence.persistence.HibernateUtil; // <-- IMPORTANTE

public class ProductoDAO extends AbstractDAO<Producto> {

    // Constructor vacío (ya no pide EntityManager)
    public ProductoDAO(){
        super(Producto.class);
    }

    public void guardarProducto(Producto nuevoProducto){
        try {
            save(nuevoProducto);
        } catch (Exception e) {
            throw new RuntimeException("Error DAO: No se pudo guardar en la base de datos.");
        }
    }
    public boolean actualizarStockLimpio(String idProducto, int cantidadVendida) {
        return execute(em -> {
            // 1. Buscamos el producto fresco usando la sesión actual
            Producto productoBD = em.find(Producto.class, idProducto);

            if (productoBD == null) {
                throw new RuntimeException("No se encontró el producto con ID: " + idProducto);
            }

            // 2. Calculamos el nuevo stock
            // Asegúrate de que productoBD.getCantidad() no sea null
            int stockActual = (productoBD.getCantidad() != null) ? productoBD.getCantidad() : 0;
            int nuevoStock = stockActual - cantidadVendida;

            // 3. Actualizamos el valor
            productoBD.setCantidad(nuevoStock);

            // 4. Verificamos stock crítico usando el getter de tu entidad
            // Si tu entidad Producto tiene el campo 'umbral', úsalo aquí:
            int umbral = (productoBD.getUmbral() != null) ? productoBD.getUmbral() : 0;
            boolean esCritico = nuevoStock <= umbral;

            // Nota: En JPA/Hibernate, al estar dentro de la transacción de 'execute',
            // el cambio en 'productoBD' se guarda automáticamente al finalizar.

            return esCritico;
        });
    }

    // el método heredado del abstract devuelve Optional
    // si no existe el producto con el id recibido retorna null
    public Producto buscarProductoPorID(String idBuscar){
        return find(idBuscar).orElse(null);
    }

    // aquí colocar metodos de sus diagramas (los que necesitan para su US)

    @Override
    protected EntityManager getEntityManager(){
        // Tomamos la conexión directo de tu clase de configuración
        return HibernateUtil.getEntityManager();
    }
}