package mx.puestoLidia.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.persistence.persistence.AbstractDAO;

import java.util.List;

public class ProductoDAO extends AbstractDAO<Producto> {

    private final EntityManager entityManager;

    public ProductoDAO(EntityManager em){
        super(Producto.class);
        this.entityManager = em;
    }

    public void guardarProducto(Producto nuevoProducto){
        try {
            save(nuevoProducto);
        } catch (Exception e) {
            throw new RuntimeException("Error DAO: No se pudo guardar en la base de datos.",e);
        }
    }

    // el método heredado del abstract devuelve Optional
    // si no existe el producto con el id recibido retorna null
    public Producto buscarProductoPorID(String idBuscar){
        return find(idBuscar).orElse(null);
    }

    // aquí colocar metodos de sus diagramas (los que necesitan para su US)

    // modificación de un producto heredando del abstract
    public void modificarProducto(Producto productoModificado){
        try {
            update(productoModificado);
        } catch (Exception e) {
            throw new RuntimeException("Error DAO: No se pudo modificar el producto en la base de datos.",e);
        }
    }

    // ---------------- PARA CONSULTA --------------------

    // Obtener todos los productos ordenados de la A a la Z
    public List<Producto> obtenerInventarioOrdenado() {
        String jpql = "SELECT p FROM Producto p ORDER BY p.nombre ASC";
        return execute(em -> {
            em.clear(); // <-- ESTA ES LA LÍNEA MÁGICA QUE LIMPIA LA MEMORIA
            return em.createQuery(jpql, Producto.class).getResultList();
        });
    }

    // Buscar productos por nombre
//    public List<Producto> buscarPorNombre(String filtro) {
//        String jpql = "SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(:filtro) ORDER BY p.nombre ASC";
//        return execute(em -> {
//            em.clear(); // <-- LIMPIA LA MEMORIA AQUÍ TAMBIÉN
//            return em.createQuery(jpql, Producto.class)
//                    .setParameter("filtro", "%" + filtro + "%")
//                    .getResultList();
//        });
//    }
    // Buscar productos por nombre o ID
    public List<Producto> filtrarPorIDoPorNombre(String filtro) {
        // Agregamos la condición OR LOWER(p.idProducto) LIKE LOWER(:filtro)
        String jpql = "SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(:filtro) OR LOWER(p.idProducto) LIKE LOWER(:filtro) ORDER BY p.nombre ASC";

        return execute(em -> {
            em.clear(); // <-- LIMPIA LA MEMORIA AQUÍ TAMBIÉN
            return em.createQuery(jpql, Producto.class)
                    .setParameter("filtro", "%" + filtro + "%")
                    .getResultList();
        });
    }



    // eliminación de producto
    public void eliminarProducto(Producto productoEliminar){
        try{
            delete(productoEliminar);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error DAO: No se pudo eliminar el producto de la base de datos.",e);
        }
    }

    public List<Producto> obtenerReporteStockCritico() {
        String jpql = "SELECT p FROM Producto p WHERE p.cantidad <= p.umbral ORDER BY p.cantidad ASC";

        return execute(em -> {
            em.clear(); // Limpiamos caché
            return em.createQuery(jpql, Producto.class).getResultList();
        });
    }

    @Override
    public EntityManager getEntityManager(){
        return entityManager;
    }
}
