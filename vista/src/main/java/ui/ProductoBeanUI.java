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

    // --- VARIABLES DE LA PANTALLA ---
    private List<Producto> listaProductos;
    private String textoBusqueda;

    // --- VARIABLES PARA EL MODAL DE ENTRADA ---
    private Producto productoSeleccionado;
    private Integer cantidadEntrada;

    public ProductoBeanUI(){
        productoHelper = new ProductoHelper();
        cargarInventario();
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
            cargarInventario();

            // Cerrar el diálogo llamando al bean
            PrimeFaces.current().executeScript("PF('wvModalRegistro').hide();");

        } catch (Exception e) {
            // Si la base de datos se cae o hay un error inesperado
            mostrarError("Error crítico","No se pudo procesar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // preparar la eliminación
    public void prepararEliminacion(String idAEliminar) {
        Producto productoAEliminar = productoHelper.buscarProductoPorID(idAEliminar);

        if(productoAEliminar != null) {
            // Guardamos los datos en la memoria del Bean
            this.idProducto = productoAEliminar.getIdProducto();
            this.nombre = productoAEliminar.getNombre();
            this.cantidad = productoAEliminar.getCantidad();

            // Validar si el producto tiene stock
            if(cantidad != null && cantidad > 0) {
                // Hay stock: Avisamos el error y terminamos (NO abrimos el modal)
                mostrarError("Error", "No se puede eliminar, el producto " + nombre + " tiene stock (" + cantidad + " unidades).");
                return;
            }

            // Si llegamos aquí, es porque el stock es 0 o null.
            // ¡Damos la orden desde Java de ABRIR el modal!
            PrimeFaces.current().executeScript("PF('wvModalEliminar').show();");

        } else {
            mostrarError("Error", "El producto ya no existe en la base de datos.");
        }
    }

    // Eliminar el producto definitivamente
    public void eliminarProducto() { // <-- Ya no necesita parámetros
        try {
            // Creamos el objeto solo con el ID que ya tenemos en memoria
            Producto productoAEliminar = new Producto();
            productoAEliminar.setIdProducto(this.idProducto);

            // Mandar a eliminar a la base de datos
            productoHelper.eliminarProducto(productoAEliminar);

            // Mensaje de éxito
            mostrarInfo("ÉXITO", "El producto " + nombre + " se eliminó correctamente.");

            // Limpiar memoria y refrescar la tabla de fondo
            limpiarDatos();

            // Cerrar el modal
            PrimeFaces.current().executeScript("PF('wvModalEliminar').hide();");

        } catch (Exception e) {
            mostrarError("Error crítico", "No se pudo eliminar: " + e.getMessage());
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
    public void limpiarDatos(){ //checar esto
        this.idProducto = null;
        this.nombre = null;
        this.precio = null;
        this.cantidad = null;
        this.umbral = null;
        this.esEdicion = false; // Se resetea por seguridad
    }

    // ------ PARA CONSULTA ------

    // 1. Método para llenar la tabla al abrir la pantalla
    public void cargarInventario() {
        listaProductos = productoHelper.obtenerInventarioOrdenado();
    }

    // 2. Método para la barra de búsqueda
    public void buscarProducto() {
        if (textoBusqueda != null && !textoBusqueda.trim().isEmpty()) {
            listaProductos = productoHelper.buscarPorNombre(textoBusqueda);
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

    // Método para guardar la Entrada en la Base de Datos
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
            productoHelper.actualizarProducto(productoSeleccionado);

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

    // MÉTODO PARA ALIMENTAR LA TABLA INVISIBLE DEL PDF
    public List<Producto> getListaProductosCriticos() {
        return productoHelper.obtenerReporteStockCritico();
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
