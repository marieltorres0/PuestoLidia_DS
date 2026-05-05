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



    // bandera para identificar Alta de Modificación
    private boolean esEdicion;

    public ProductoBeanUI(){
        productoHelper = new ProductoHelper();
    }

    // validar campos vacíos primero
    public boolean validarCamposVacios() {
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
        // Mostrar los campos que quedaron vacíos
        if (!camposVacios.isEmpty()) {
            String nombresCampos = String.join(", ", camposVacios);
            mostrarError("Campos obligatorios", "Por favor, llena los siguientes campos: " + nombresCampos + ".");
            return false;
        }

        return true;
    }

    // validar que los datos sean correctos
    public boolean validarDatos(){

        // validar id repetido (sólo si es alta)
        if(!esEdicion){
            if(productoHelper.buscarProductoPorID(idProducto)!= null){
                mostrarError("ID DUPLICADO", "Ya existe un producto con el ID: " + idProducto + ".");
                return false;
            }
        }

        // validar que no haya valores negativos
        if(precio.compareTo(BigDecimal.ZERO) <0 || cantidad <0 || umbral <0){
            mostrarError("Datos no válidos","Los valores numéricos no pueden ser negativos.");
            return false;
        }

        // si todos los datos son válidos
        return true;
    }

    // prepara los datos si se va modificar un producto
    public void prepararModificacion(String idProductoModificar){
        Producto productoModificar = productoHelper.buscarProductoPorID(idProductoModificar);

        if(productoModificar != null){
            // llenar bean con los datos del producto a modificar
            this.idProducto = productoModificar.getIdProducto();
            this.nombre = productoModificar.getNombre();
            this.precio = productoModificar.getPrecio();
            this.cantidad = productoModificar.getCantidad();
            this.umbral = productoModificar.getUmbral();

            this.esEdicion = true;
        } else{
            mostrarError("Error", "No se encontró el producto que se quiere modificar.");
        }
    }

    // dar de alta o actualizar un producto
    public void guardarProducto(){
        try {
            // validar los datos introducidos
            if(!validarCamposVacios() || !validarDatos()){
                return;
            }

            // si los datos son ideales
            Producto producto = new Producto();
            producto.setIdProducto(idProducto);
            producto.setNombre(nombre);
            producto.setPrecio(precio);
            producto.setCantidad(cantidad);
            producto.setUmbral(umbral);

            // verificar si es una alta o una modificación
            if(esEdicion){

                productoHelper.modificarProducto(producto);
                // Mensaje de éxito
                mostrarInfo("ÉXITO","El producto " + nombre + " se actualizó correctamente.");
            } else {

                productoHelper.guardarProducto(producto);
                // Mensaje de éxito
                mostrarInfo("ÉXITO","El producto " + nombre + " se guardó correctamente.");
            }

            limpiarDatos();

            // Cerrar el diálogo llamando al bean
            PrimeFaces.current().executeScript("PF('wvModalRegistro').hide();");

        } catch (Exception e) {
            // Si la base de datos se cae o hay un error inesperado
            mostrarError("Error crítico","No se pudo procesar: " + e.getMessage());
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
        this.esEdicion = false; // Se resetea por seguridad
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

    public boolean isEsEdicion() { return esEdicion;}

    public void setEsEdicion(boolean esEdicion) { this.esEdicion = esEdicion; }
}
