package mx.puestoLidia.persistence.dao;

import mx.puestoLidia.entity.Usuario;
import mx.puestoLidia.persistence.persistence.AbstractDAO;
import mx.puestoLidia.persistence.persistence.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class UsuarioDAO extends AbstractDAO<Usuario> {

    public UsuarioDAO() {
        super(Usuario.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return HibernateUtil.getEntityManager();
    }

    /**
     * Busca un usuario que coincida exactamente con el rol y la contraseña.
     * Utiliza el método execute() del AbstractDAO para manejar la transacción de forma segura.
     */
    public Usuario autenticar(String rol, String contrasena) {
        return execute(em -> {
            try {
                // Consulta JPQL para verificar el rol y la contraseña
                String jpql = "SELECT u FROM Usuario u WHERE u.rol = :rol AND u.contrasena = :contrasena";
                TypedQuery<Usuario> query = em.createQuery(jpql, Usuario.class);
                query.setParameter("rol", rol);
                query.setParameter("contrasena", contrasena);

                return query.getSingleResult();
            } catch (NoResultException e) {
                // Retorna null si las credenciales no existen o están mal
                return null;
            }
        });
    }
}