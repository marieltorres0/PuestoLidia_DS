package mx.puestoLidia.negocio.delegate;

import mx.puestoLidia.entity.Usuario;
import mx.puestoLidia.negocio.facade.FacadeAutentificacion;
// Asegúrate de importar la ruta correcta de tu ServiceFacadeLocator
import mx.puestoLidia.negocio.integration.ServiceFacadeLocator;

public class DelegateAutentificacion {

    /**
     * Método que la vista (LoginBean) mandará llamar para autenticar al usuario.
     */
    public Usuario login(String rol, String contrasena) throws Exception {

        // Obtenemos la instancia del Facade a través del Locator
        FacadeAutentificacion facade = ServiceFacadeLocator.getInstanceFacadeAutentificacion();

        // Le pasamos la bolita al Facade para que aplique las reglas de negocio
        return facade.iniciarSesion(rol, contrasena);
    }
}