package ui;


import helper.LoginHelper;
import jakarta.enterprise.context.SessionScoped;
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
        return helper.autenticar(this);
    }

    public String cerrarSesion() {
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