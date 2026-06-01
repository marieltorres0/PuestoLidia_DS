package mx.puestoLidia.negocio.facade;

import mx.puestoLidia.entity.Usuario;
import mx.puestoLidia.persistence.dao.UsuarioDAO;
import mx.puestoLidia.persistence.integration.ServiceLocator;

public class FacadeAutentificacion {

    public Usuario iniciarSesion(String rol, String contrasena) throws Exception {

        if (rol == null || rol.trim().isEmpty()) {
            throw new Exception("Debes seleccionar un rol para ingresar.");
        }
        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new Exception("La contraseña es obligatoria.");
        }

        // Validación corregida a "admin"
        String rolValidar = rol.toLowerCase();
        if (!rolValidar.equals("admin") && !rolValidar.equals("cajero")) {
            throw new Exception("Rol no válido. El sistema solo permite acceso a Administradores y Cajeros.");
        }

        if (contrasena.length() > 50) {
            throw new Exception("La contraseña no puede exceder los 50 caracteres de longitud.");
        }

        UsuarioDAO usuarioDAO = ServiceLocator.getInstanceUsuarioDAO();
        Usuario usuarioValidado = usuarioDAO.autenticar(rolValidar, contrasena);

        if (usuarioValidado == null) {
            throw new Exception("Credenciales incorrectas. Verifica tu rol y contraseña.");
        }

        return usuarioValidado;
    }
}