package ui;

import helper.ProductoHelper;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.puestoLidia.entity.Producto;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Named("productoBeanUI")
@SessionScoped
public class ProductoBeanUI implements Serializable {
    private ProductoHelper productoHelper;

    private String idProducto;
    private String nombre;
    private BigDecimal precio;
    private Integer cantidad;
    private Integer umbral;

    public ProductoBeanUI(){
        productoHelper = new ProductoHelper();
    }

    // validar campos vacíos primero
    public boolean validarCamposVaciosAltaProducto() {
        List<String> camposVacios = new ArrayList<>();

        if (idProducto == null || idProducto.trim().isEmpty()) {
            camposVacios.add("ID");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            camposVacios.add("Nombre");
        }
        if (precio == null) {
            camposVacios.add("Precio");
        }
        if (cantidad == null) {
            camposVacios.add("Cantidad inicial");
        }
        if (umbral == null) {
            camposVacios.add("Cantidad crítica");
        }
        // Mostrar los campos que quedaron vacios
        if (!camposVacios.isEmpty()) {
            String nombresCampos = String.join(", ", camposVacios);

            mostrarError("Campos obligatorios", "Por favor, llena los siguientes campos: " + nombresCampos + ".");
            return false;
        }

        return true;
    }

    // validar que los datos sean correctos
    public boolean validarDatosAltaProducto(){
        // validar id repetido
        if(productoHelper.buscarProductoPorID(idProducto)!= null){
            mostrarError("ID DUPLICADO", "Ya existe un producto con el ID: " + idProducto + ".");
            return false;
        }

        // validar que no haya valores negativos
        if(precio.compareTo(BigDecimal.ZERO) <0 || cantidad <0 || umbral <0){
            mostrarError("Datos no válidos","Los valores numéricos no pueden ser negativos.");
            return false;
        }

        // si todos los datos son válidos
        return true;
    }

    // dar de alta el producto (aqui se guarda)
    public void altaProducto(){
        try {
            // validar los datos introducidos
            if(!validarCamposVaciosAltaProducto() || !validarDatosAltaProducto()){
                return;
            }

            // si los datos son ideales
            Producto nuevoProducto = new Producto();
            nuevoProducto.setIdProducto(idProducto);
            nuevoProducto.setNombre(nombre);
            nuevoProducto.setPrecio(precio);
            nuevoProducto.setCantidad(cantidad);
            nuevoProducto.setUmbral(umbral);

            productoHelper.guardarProducto(nuevoProducto);

            // Mensaje de éxito
            mostrarInfo("ÉXITO","El producto " + nombre + " se guardó correctamente.");

            /*
             Limpiar los datos (aun no se si sera necesario
             ya que creo que la alta de un producto se manejará desde un botón
             y se abrirá como una ventana emergente/modal/dialogo
            */
            limpiarDatos();

            // Cerrar el diálogo llamando al bean
            PrimeFaces.current().executeScript("PF('wvModalRegistro').hide();");

        } catch (Exception e) {
            // Si la base de datos se cae o hay un error inesperado
            mostrarError("Error crítico","No se pudo guardar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Metodo para mostrar error en la pantalla
    private void mostrarError(String titulo, String detalle) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, detalle));
    }

    // Metodo para mostrar información en la pantalla
    private void mostrarInfo(String titulo, String detalle) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, detalle));
    }

    // Metodo para limpiar los atributos del producto
    private void limpiarDatos(){
        this.idProducto = null;
        this.nombre = null;
        this.precio = null;
        this.cantidad = null;
        this.umbral = null;
    }


    // Getters y Setters
    public String getIdProducto() { return idProducto; }
    public void setIdProducto(String idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Integer getUmbral() { return umbral; }
    public void setUmbral(Integer umbral) { this.umbral = umbral; }
}
