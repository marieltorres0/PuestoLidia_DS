package ui;

import helper.LoginHelper;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.puestoLidia.entity.Usuario;

import java.io.Serializable;

@Named("loginBeanUI") // Definimos el nombre exacto que usará el xhtml
@SessionScoped
public class LoginBeanUI implements Serializable {

    private String rol;
    private String contrasena;
    private Usuario usuarioLogueado;

    // Instanciamos el Helper
    private final LoginHelper helper = new LoginHelper();

    // ==========================================
    // ACCIONES DE LA PANTALLA
    // ==========================================
    public String autenticar() {
        // Le pasamos esta misma clase (this) al Helper para que haga su trabajo
        String rutaDestino = helper.autenticar(this);

        // Si el helper valida todo bien y nos manda a "seccion", guardamos el rol en la sesión
        if (rutaDestino != null && rutaDestino.contains("seccion")) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("rolActual", this.rol);
        }

        return rutaDestino;
    }

    public String cerrarSesion() {
        // Al cerrar sesión, limpiamos la variable para que el siguiente cajero no la vea
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("rolActual");
        return helper.cerrarSesion(this);
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================
    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }
}