package ui;

import helper.InventarioHelper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.puestoLidia.entity.Producto;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.List;

/**
 * @author Eduardo Avitia Castro
 */
@Named("inventarioBeanUI")
@SessionScoped
public class InventarioBeanUI implements Serializable {

    private InventarioHelper inventarioHelper;

    // --- VARIABLES DE LA PANTALLA ---
    private List<Producto> listaProductos;
    private String textoBusqueda;

    // --- VARIABLES PARA EL MODAL DE ENTRADA ---
    private Producto productoSeleccionado;
    private Integer cantidadEntrada;

    public InventarioBeanUI() {
        inventarioHelper = new InventarioHelper();
    }

    @PostConstruct
    public void init() {
        cargarInventario();
    }

    // 1. Método para llenar la tabla al abrir la pantalla
    public void cargarInventario() {
        listaProductos = inventarioHelper.obtenerInventarioOrdenado();
    }

    // 2. Método para la barra de búsqueda
    public void buscarProducto() {
        if (textoBusqueda != null && !textoBusqueda.trim().isEmpty()) {
            listaProductos = inventarioHelper.buscarPorNombre(textoBusqueda);
        } else {
            cargarInventario(); // Si borran el texto, vuelve a cargar todo
        }
    }

    // 3. Método para preparar el modal de Entrada
    public void abrirModalEntrada() {
        // Validar que sí hayan seleccionado una fila en la tabla
        if (productoSeleccionado == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Selecciona un producto de la tabla primero."));
            return;
        }

        cantidadEntrada = null; // Limpiar el campo por si tenía algo de antes
        PrimeFaces.current().executeScript("PF('wvModalEntrada').show();"); // Abrir ventanita
    }

    // 4. Método para guardar la Entrada en la Base de Datos
    public void registrarEntrada() {
        // Validar que no metan números negativos o ceros
        if (cantidadEntrada == null || cantidadEntrada <= 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresa una cantidad mayor a 0."));
            return;
        }

        try {
            // Hacer la suma matemática
            int nuevaCantidad = productoSeleccionado.getCantidad() + cantidadEntrada;
            productoSeleccionado.setCantidad(nuevaCantidad);

            // Mandar a actualizar a la BD usando nuestro Helper
            inventarioHelper.actualizarProducto(productoSeleccionado);

            // Mensaje de éxito verde
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Se agregaron " + cantidadEntrada + " unidades a " + productoSeleccionado.getNombre()));

            // Cerrar ventanita y actualizar tabla de fondo
            PrimeFaces.current().executeScript("PF('wvModalEntrada').hide();");
            cargarInventario();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error crítico", "No se pudo guardar la entrada."));
            e.printStackTrace();
        }
    }

    // ================= GETTERS Y SETTERS =================

    public List<Producto> getListaProductos() { return listaProductos; }
    public void setListaProductos(List<Producto> listaProductos) { this.listaProductos = listaProductos; }

    public String getTextoBusqueda() { return textoBusqueda; }
    public void setTextoBusqueda(String textoBusqueda) { this.textoBusqueda = textoBusqueda; }

    public Producto getProductoSeleccionado() { return productoSeleccionado; }
    public void setProductoSeleccionado(Producto productoSeleccionado) { this.productoSeleccionado = productoSeleccionado; }

    public Integer getCantidadEntrada() { return cantidadEntrada; }
    public void setCantidadEntrada(Integer cantidadEntrada) { this.cantidadEntrada = cantidadEntrada; }
}