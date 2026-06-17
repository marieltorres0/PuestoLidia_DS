package mx.puestoLidia.persistence.dao;

import jakarta.persistence.EntityManager;
import mx.puestoLidia.entity.Cliente;
import mx.puestoLidia.persistence.persistence.AbstractDAO;
import java.util.List;

/**
 * ClienteDAO extends AbstractDAO<Cliente>
 * Acceso a datos específico para entidad Cliente
 */
public class ClienteDAO extends AbstractDAO<Cliente> {

    private final EntityManager entityManager;

    public ClienteDAO(EntityManager em) {
        super(Cliente.class);
        this.entityManager = em;
    }

    public void guardarCliente(Cliente nuevoCliente) {
        try {
            save(nuevoCliente);
        } catch (Exception e) {
            throw new RuntimeException("Error DAO: No se pudo guardar el cliente en la base de datos.", e);
        }
    }

    public Cliente buscarClientePorID(Integer idBuscar) {
        return find(idBuscar).orElse(null);
    }

    public void modificarCliente(Cliente clienteModificado) {
        try {
            update(clienteModificado);
        } catch (Exception e) {
            throw new RuntimeException("Error DAO: No se pudo modificar el cliente en la base de datos.", e);
        }
    }

    public void eliminarCliente(Cliente clienteEliminar) {
        try {
            delete(clienteEliminar);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error DAO: No se pudo eliminar el cliente de la base de datos.", e);
        }
    }

    public List<Cliente> obtenerTodosClientes() {
        String jpql = "SELECT c FROM Cliente c ORDER BY c.nombre ASC";
        return execute(em -> {
            em.clear();
            return em.createQuery(jpql, Cliente.class).getResultList();
        });
    }

    public Cliente buscarClientePorTelefono(String telefono) {
        String jpql = "SELECT c FROM Cliente c WHERE c.telefono = :telefono";
        return execute(em -> {
            try {
                return em.createQuery(jpql, Cliente.class)
                        .setParameter("telefono", telefono)
                        .getSingleResult();
            } catch (jakarta.persistence.NoResultException ex) {
                return null;
            }
        });
    }

    public List<Cliente> obtenerClientesConAdeudo() {
        String jpql = "SELECT c FROM Cliente c WHERE c.adeudo > 0 ORDER BY c.adeudo DESC";
        return execute(em -> {
            em.clear();
            return em.createQuery(jpql, Cliente.class).getResultList();
        });
    }

    public List<Cliente> buscarClientesPorNombre(String filtro) {
        String jpql = "SELECT c FROM Cliente c WHERE LOWER(c.nombre) LIKE LOWER(:filtro) ORDER BY c.nombre ASC";
        return execute(em -> {
            em.clear();
            return em.createQuery(jpql, Cliente.class)
                    .setParameter("filtro", "%" + filtro + "%")
                    .getResultList();
        });
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}