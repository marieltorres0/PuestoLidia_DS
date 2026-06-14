package ui;

import helper.ProductoHelper;
import helper.VentaHelper;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.puestoLidia.entity.ItemVenta;
import mx.puestoLidia.entity.Producto;
import mx.puestoLidia.entity.Usuario;
import mx.puestoLidia.entity.Venta;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Named("ventaBeanUI")
@SessionScoped
public class VentaBeanUI implements Serializable {

    private VentaHelper ventaHelper;
    private ProductoHelper productoHelper;

    // Variables de pantalla
    private String idProductoBusqueda;
    private List<ItemVenta> carrito;
    private ItemVenta itemSeleccionado; // Rastrea qué fila está seleccionada en la tabla

    // 'total' funciona dinámicamente: representa el total del carrito,
    // pero durante el cobro representa lo que "falta por pagar"
    private BigDecimal total;
    private BigDecimal montoRecibido;
    private BigDecimal cambio;

    // Rastrea cuánto dinero en efectivo ha ingresado el cajero en total (para pagos parciales)
    private BigDecimal montoAcumulado;

    public VentaBeanUI() {
        inicializarVenta();
    }

    // Inicializa o resetea todos los valores para comenzar una venta limpia
    public void inicializarVenta() {
        ventaHelper = new VentaHelper();
        productoHelper = new ProductoHelper();

        this.idProductoBusqueda = null;
        this.carrito = new ArrayList<>();
        this.itemSeleccionado = null;
        this.total = BigDecimal.ZERO;
        this.montoRecibido = null;
        this.cambio = BigDecimal.ZERO;
        this.montoAcumulado = BigDecimal.ZERO;
    }

    // muestra las coindicencias del campo de busqueda
    public List<String> buscarCoincidencias(String query){
        if(query == null || query.trim().isEmpty()){
            return new ArrayList<>();
        }
        List<Producto> productos = productoHelper.filtrarPorIdOPorNombre(query);
        List<String> resultados = new ArrayList<>();

        for(Producto p : productos){
            resultados.add(p.getIdProducto() + " | " + p.getNombre());
        }
        return resultados;
    }

    // Se ingresa el id en el campo de texto (escáner o manual)
    public void agregarProducto() {
        if (idProductoBusqueda == null || idProductoBusqueda.trim().isEmpty()) {
            return;
        }
        try {
            String idReal = idProductoBusqueda;
            if (idProductoBusqueda.contains(" | ")) {
                idReal = idProductoBusqueda.substring(0, idProductoBusqueda.indexOf(" | "));
            }

            ventaHelper.agregrarOSumarProductoEnCarrito(idReal.trim(), 1, carrito);
            recalcularTotalVenta();
            // Limpiamos el campo de búsqueda tanto en éxito como en error
            this.idProductoBusqueda = null;
        } catch (Exception e) {
            mostrarError("Validación de Inventario", e.getMessage());
            this.idProductoBusqueda = null;
        }
    }

    /**
     * Incrementa la cantidad de un producto.
     * @param idProducto ID del producto desde el botón de la tabla, o vacío si proviene del atajo de teclado (+).
     */
    public void aumentarCantidad(String idProducto) {
        // JSF convierte el 'null' de la vista a una cadena vacía "", por eso agregamos isEmpty()
        if ((idProducto == null || idProducto.trim().isEmpty()) && itemSeleccionado != null) {
            idProducto = itemSeleccionado.getIdProducto().getIdProducto();
        }

        if (idProducto == null || idProducto.trim().isEmpty()) {
            mostrarInfo("Aviso", "Selecciona un producto de la lista primero.");
            return;
        }

        try {
            ventaHelper.agregrarOSumarProductoEnCarrito(idProducto, 1, carrito);
            recalcularTotalVenta();
        } catch (Exception e) {
            mostrarError("Límite de Stock", e.getMessage());
        }
    }

    /**
     * Disminuye la cantidad de un producto. Si es 1, se remueve automáticamente.
     * @param idProducto ID del producto desde el botón de la tabla, o vacío si proviene del atajo de teclado (-).
     */
    public void disminuirCantidad(String idProducto) {
        if ((idProducto == null || idProducto.trim().isEmpty()) && itemSeleccionado != null) {
            idProducto = itemSeleccionado.getIdProducto().getIdProducto();
        }

        if (idProducto == null || idProducto.trim().isEmpty()) {
            mostrarInfo("Aviso", "Selecciona un producto de la lista primero.");
            return;
        }

        try {
            ventaHelper.restarOQuitarDelCarrito(idProducto, carrito);
            recalcularTotalVenta();

            // Si se usó el teclado y el elemento ya no está en el carrito (llegó a 0), limpiamos la selección
            if (itemSeleccionado != null && !carrito.contains(itemSeleccionado)) {
                itemSeleccionado = null;
            }
        } catch (Exception e) {
            mostrarError("Error al modificar carrito", e.getMessage());
        }
    }

    /**
     * Elimina por completo un producto del carrito sin importar su cantidad actual.
     * @param idProducto ID del producto desde el botón de la tabla, o vacío si proviene del atajo de teclado (F3).
     */
    public void eliminarDelCarrito(String idProducto) {
        if ((idProducto == null || idProducto.trim().isEmpty()) && itemSeleccionado != null) {
            idProducto = itemSeleccionado.getIdProducto().getIdProducto();
        }

        if (idProducto == null || idProducto.trim().isEmpty()) {
            mostrarInfo("Aviso", "Selecciona un producto de la lista primero.");
            return;
        }

        try {
            final String idBuscar = idProducto; // Constante requerida para la expresión lambda
            carrito.removeIf(item -> item.getIdProducto().getIdProducto().equals(idBuscar));
            recalcularTotalVenta();

            // Si el producto removido coincide con el seleccionado actual, limpiamos la variable de selección
            if (itemSeleccionado != null && itemSeleccionado.getIdProducto().getIdProducto().equals(idBuscar)) {
                itemSeleccionado = null;
            }
        } catch (Exception e) {
            mostrarError("Error al eliminar producto", e.getMessage());
        }
    }

    // Actualiza el total del carrito sumando subtotales
    private void recalcularTotalVenta() {
        this.total = ventaHelper.calcularTotalCarrito(carrito);
        // Si el carrito cambia, se reinicia cualquier abono previo por seguridad
        this.montoAcumulado = BigDecimal.ZERO;
    }

    // Se ejecuta al presionar "COBRAR VENTA (F1)" para asegurar que el input del modal empiece vacío
    public void prepararCobro() {
        this.montoRecibido = null;
    }

    // Procesar el cobro: maneja abonos parciales y el guardado final
    public void procesarCobro() {
        if (carrito.isEmpty()) {
            mostrarError("Operación inválida", "El carrito de ventas se encuentra vacío.");
            return;
        }

        if (montoRecibido == null || montoRecibido.compareTo(BigDecimal.ZERO) <= 0) {
            mostrarError("Monto inválido", "Ingresa una cantidad mayor a cero.");
            return;
        }

        // Caso 1: El monto recibido NO alcanza para cubrir el total restante (Pago parcial)
        if (montoRecibido.compareTo(total) < 0) {
            total = total.subtract(montoRecibido); // Restamos el abono al total a pagar
            montoAcumulado = montoAcumulado.add(montoRecibido); // Guardamos cuánto nos han dado
            montoRecibido = null; // Limpiamos el input para el siguiente pago
            mostrarInfo("Abono registrado", "Resta cobrar: $" + total);
            return; // Terminamos la ejecución aquí, no guardamos aún en BD
        }

        // Caso 2: El monto recibido SÍ cubre el total restante (Cobro completo)
        try {
            cambio = montoRecibido.subtract(total);
            montoAcumulado = montoAcumulado.add(montoRecibido);

            // Mock de usuario autenticado en sesión (Cajero ID = 1)
            Usuario cajero = new Usuario();
            cajero.setId(1);

            Venta nuevaVenta = new Venta();
            nuevaVenta.setFechaHora(Instant.now());
            nuevaVenta.setIdUsuario(cajero);

            // Calculamos el costo real original del carrito para la BD,
            // ya que la variable 'total' en este punto puede estar en 0 por los abonos
            BigDecimal totalRealVenta = ventaHelper.calcularTotalCarrito(carrito);

            nuevaVenta.setTotal(totalRealVenta);
            nuevaVenta.setMonto(montoAcumulado);
            nuevaVenta.setCambio(cambio);
            nuevaVenta.setTipo("contado");

            // Persistencia del bloque transaccional completo
            ventaHelper.registrarVentaCompleta(nuevaVenta, carrito);

            // Instrucción enviada al frontend para cerrar el modal de cobro y abrir el de éxito
            // Solo se ejecuta si el bloque transaccional no lanzó excepciones
            PrimeFaces.current().executeScript("PF('wvModalCobro').hide(); PF('wvModalExito').show();");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error transaccional", "No se pudo registrar la venta: " + e.getMessage());
        }
    }

    // Método para mostrar error en la pantalla (Growl)
    private void mostrarError(String titulo, String detalle) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, detalle));
    }

    // Método para mostrar información en la pantalla (Growl)
    private void mostrarInfo(String titulo, String detalle) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, detalle));
    }

    // Getters y Setters
    public String getIdProductoBusqueda() { return idProductoBusqueda; }
    public void setIdProductoBusqueda(String idProductoBusqueda) { this.idProductoBusqueda = idProductoBusqueda; }

    public List<ItemVenta> getCarrito() { return carrito; }
    public void setCarrito(List<ItemVenta> carrito) { this.carrito = carrito; }

    public ItemVenta getItemSeleccionado() { return itemSeleccionado; }
    public void setItemSeleccionado(ItemVenta itemSeleccionado) { this.itemSeleccionado = itemSeleccionado; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public BigDecimal getMontoRecibido() { return montoRecibido; }
    public void setMontoRecibido(BigDecimal montoRecibido) { this.montoRecibido = montoRecibido; }

    public BigDecimal getCambio() { return cambio; }
    public void setCambio(BigDecimal cambio) { this.cambio = cambio; }

    public BigDecimal getMontoAcumulado() { return montoAcumulado; }
    public void setMontoAcumulado(BigDecimal montoAcumulado) { this.montoAcumulado = montoAcumulado; }
}