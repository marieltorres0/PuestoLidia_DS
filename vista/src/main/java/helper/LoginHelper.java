package helper;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import mx.puestoLidia.entity.Usuario;
import mx.puestoLidia.negocio.delegate.DelegateAutentificacion;
import ui.LoginBeanUI;

import java.io.Serializable;

public class LoginHelper implements Serializable {

    /**
     * Procesa la solicitud de inicio de sesión que viene del UI.
     */
    public String autenticar(LoginBeanUI ui) {
        try {
            DelegateAutentificacion delegate = new DelegateAutentificacion();

            // Le pasamos las variables que capturó el Bean de la pantalla
            Usuario usuarioValidado = delegate.login(ui.getRol(), ui.getContrasena());

            // Si pasa, guardamos al usuario en la sesión del Bean
            ui.setUsuarioLogueado(usuarioValidado);

            // Regla de redirección
            String rolBD = usuarioValidado.getRol().toLowerCase();
            if (rolBD.equals("admin")) {
                return "seccion.xhtml?faces-redirect=true";
            } else if (rolBD.equals("cajero")) {
                return "seccion.xhtml?faces-redirect=true";
            }

            return null;

        } catch (Exception e) {
            // Mandamos el error de vuelta a la pantalla (Growl)
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Acceso", e.getMessage()));
            return null;
        }
    }

    /**
     * Limpia la sesión y regresa al login.
     */
    public String cerrarSesion(LoginBeanUI ui) {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        ui.setUsuarioLogueado(null);
        return "login.xhtml?faces-redirect=true";
    }
}
