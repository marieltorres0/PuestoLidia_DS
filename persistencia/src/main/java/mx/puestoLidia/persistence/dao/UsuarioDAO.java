package mx.puestoLidia.persistence.dao;

import mx.puestoLidia.entity.Usuario;
import jakarta.persistence.EntityManager;
import java.util.Optional;

public class UsuarioDAO {

    private EntityManager em; // Asegúrate de que esto coincida con cómo inyectas tu EntityManager en otros DAOs

    public UsuarioDAO() {
        // Constructor vacío o lógica de conexión según tu proyecto
    }

    // El método clave para buscar al usuario y evitar el error
    public Optional<Usuario> find(int id) {
        try {
            // Cambia 'em' por tu forma de obtener el EntityManager (ej. JPAUtil.getEntityManager())
            Usuario usuario = em.find(Usuario.class, id);
            return Optional.ofNullable(usuario);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}