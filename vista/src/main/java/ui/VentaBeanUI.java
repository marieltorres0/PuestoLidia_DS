package ui;

import helper.ClienteHelper;
import helper.ProductoHelper;
import helper.VentaHelper;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.puestoLidia.entity.*;
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
    private ClienteHelper clienteHelper;

    // Variables de pantalla
    private String idProductoBusqueda;
    private List<ItemVenta> carrito;
    private ItemVenta itemSeleccionado; // Rastrea qué fila está seleccionada en la tabla
    private String tipoPago = "contado"; // por defecto

    // 'total' funciona dinámicamente: representa el total del carrito,
    // pero durante el cobro representa lo que "falta por pagar"
    private BigDecimal total;
    private BigDecimal montoRecibido;
    private BigDecimal cambio;
    private BigDecimal montoAcumulado;

    // para venta a credito
    private String clienteBusqueda;
    private Cliente nuevoCliente;

    // Solo para mostrar en el modal de éxito en venta a crédito
    private BigDecimal adeudoGeneradoClienteCredito;
    private String nombreClienteCredito;

    private List<Producto> productosStockBajo;

    public VentaBeanUI() {
        inicializarVenta();
    }

    // Inicializa o resetea todos los valores para comenzar una venta limpia
    public void inicializarVenta() {
        ventaHelper = new VentaHelper();
        productoHelper = new ProductoHelper();
        clienteHelper = new ClienteHelper();

        this.idProductoBusqueda = null;
        this.carrito = new ArrayList<>();
        this.itemSeleccionado = null;
        this.total = BigDecimal.ZERO;
        this.montoRecibido = null;
        this.cambio = BigDecimal.ZERO;
        this.montoAcumulado = BigDecimal.ZERO;
        this.tipoPago = "contado";
        this.clienteBusqueda = null;
        this.nuevoCliente = new Cliente();
        this.adeudoGeneradoClienteCredito = BigDecimal.ZERO;
        this.nombreClienteCredito = null;
        this.productosStockBajo = new ArrayList<>();
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
        this.montoAcumulado = BigDecimal.ZERO;
    }

    // Se ejecuta al presionar "COBRAR VENTA (F1)" para asegurar que el input del modal empiece vacío
    public void prepararCobro() {
        this.montoRecibido = null;
        this.clienteBusqueda = null;
        this.nuevoCliente = new Cliente();
        this.tipoPago = "contado";
    }

    // Procesar el cobro: VALIDA LOS MONTOS Y EL CLIENTE EN CRÉDITO
    public void procesarCobro() {
        if (carrito.isEmpty()) {
            mostrarError("Operación inválida", "El carrito de ventas se encuentra vacío.");
            return;
        }

        if (montoRecibido == null) { montoRecibido = BigDecimal.ZERO; }
        BigDecimal totalRealVenta = ventaHelper.calcularTotalCarrito(carrito);

        // VALIDACIONES A CRÉDITO
        if ("credito".equals(tipoPago)) {
            // En crédito, si el pago es menor al total, el cliente es obligatorio
            if (montoRecibido.compareTo(totalRealVenta) < 0) {
                if (clienteBusqueda == null || clienteBusqueda.trim().isEmpty()) {
                    mostrarError("Error", "Debes seleccionar un cliente para ventas a crédito.");
                    return;
                }
                ejecutarVenta();
            } else {
                // Si el monto es mayor o igual al total, SIEMPRE debe pasar por confirmación
                PrimeFaces.current().executeScript("PF('wvConfirmarVenta').show();");
            }
        } else {
            // VENTA A CONTADO monto debe ser mayor a 0
            if (montoRecibido.compareTo(BigDecimal.ZERO) <= 0) {
                mostrarError("Monto inválido", "Ingresa una cantidad mayor a cero.");
                return;
            }

            // Caso 1: El monto recibido NO alcanza para cubrir el total restante (Pago parcial)
            if (montoRecibido.compareTo(total) < 0) {
                total = total.subtract(montoRecibido);
                montoAcumulado = montoAcumulado.add(montoRecibido);
                montoRecibido = null;
                mostrarInfo("Abono registrado", "Resta cobrar: $" + total);
                return;
            } else {
                // pago completo (Termina de pagar el 'total' restante)
                ejecutarVenta();
            }
        }
    }

    // GUARDADO FINAL DE LA VENTA
    public void ejecutarVenta() {
        BigDecimal totalRealVenta = ventaHelper.calcularTotalCarrito(carrito);

        // si el monto cubre el total, lo procesamos como contado
        if ("credito".equals(tipoPago) && montoRecibido.compareTo(totalRealVenta) >= 0) {
            tipoPago = "contado";
        }

        try {
            Venta nuevaVenta = new Venta();
            nuevaVenta.setFechaHora(Instant.now());
            nuevaVenta.setIdUsuario(new Usuario()); nuevaVenta.getIdUsuario().setId(1);
            nuevaVenta.setTotal(totalRealVenta);

            if ("contado".equals(tipoPago)) {
                cambio = montoRecibido.subtract(total);
                montoAcumulado = montoAcumulado.add(montoRecibido);

                nuevaVenta.setMonto(montoAcumulado);
                nuevaVenta.setCambio(cambio);
                nuevaVenta.setTipo("contado");
            } else {
                // Lógica de crédito ya validada que tiene cliente
                String nombreStr = clienteBusqueda.contains(" | ") ? clienteBusqueda.substring(0, clienteBusqueda.indexOf(" | ")).trim() : clienteBusqueda;
                Cliente clienteReal = clienteHelper.buscarClientesPorNombre(nombreStr).get(0);

                nuevaVenta.setIdCliente(clienteReal);
                this.nombreClienteCredito = clienteReal.getNombre();

                nuevaVenta.setMonto(montoRecibido);
                nuevaVenta.setCambio(BigDecimal.ZERO);
                nuevaVenta.setTipo("credito");
                this.adeudoGeneradoClienteCredito = totalRealVenta.subtract(montoRecibido);
            }

            ventaHelper.registrarVentaCompleta(nuevaVenta, carrito);
            verificarStockBajo();
            PrimeFaces.current().executeScript("PF('wvConfirmarVenta').hide(); PF('wvModalCobro').hide(); PF('wvModalExito').show();");

        } catch (Exception e) {
            mostrarError("Error", "No se pudo registrar la venta.");
        }
    }

    // PARA VENTA A CRÉDITO
    // para el filtro del mini modal de los clientes
    public List<String> buscarCoincidenciasCliente(String query){
        // Traemos todos los clientes para poder buscar por nombre o por teléfono
        List<Cliente> todosLosClientes = clienteHelper.obtenerTodosClientes();
        List<String> resultados = new ArrayList<>();

        // Pasar a minúsculas para evitar conflictos
        String filtro = (query == null) ? "" : query.trim().toLowerCase();

        for (Cliente c : todosLosClientes){
            String nombre = (c.getNombre() != null) ? c.getNombre().toLowerCase() : "";
            String telefonoReal = (c.getTelefono() != null) ? c.getTelefono() : "";
            String telefonoMostrar = (!telefonoReal.trim().isEmpty()) ? telefonoReal : "Sin teléfono";

            // Si no escribieron nada , o si el nombre coincide, o si el teléfono coincide
            if (filtro.isEmpty() || nombre.contains(filtro) || telefonoReal.contains(filtro)) {
                resultados.add(c.getNombre() + " | " + telefonoMostrar);
            }
        }
        return resultados;
    }

    // Para registrar un cliente nuevo
    public void registrarNuevoCliente(){
        try {
            nuevoCliente.setAdeudo(BigDecimal.ZERO);
            clienteHelper.guardarCliente(nuevoCliente);

            // ormato "Nombre | Teléfono" para rellenar la caja
            String telefono = (nuevoCliente.getTelefono() != null && !nuevoCliente.getTelefono().trim().isEmpty()) ? nuevoCliente.getTelefono() : "Sin teléfono";
            this.clienteBusqueda = nuevoCliente.getNombre() + " | " + telefono;

            mostrarInfo("Cliente registrado", "El cliente se seleccionó automáticamente para esta venta.");
            // preparamos una nueva instancia por si abren el modal de nuevo
            this.nuevoCliente = new Cliente();
            PrimeFaces.current().executeScript("PF('wvModalNuevoCliente').hide();");
        } catch (Exception e) {
            mostrarError("Error de registro", "No se pudo guardar el cliente: " + e.getMessage());
        }
    }

    // Para lanzar la alerta de stock bajo
    public void verificarStockBajo(){
        this.productosStockBajo = ventaHelper.evaluarStockCritico(carrito);

        if(this.productosStockBajo != null && !this.productosStockBajo.isEmpty()){
            PrimeFaces.current().ajax().update("frmAlertaStock");
            PrimeFaces.current().executeScript("PF('wvAlertaStock').show();");
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

    public String getTipoPago() {return tipoPago;}
    public void setTipoPago(String tipoPago) {this.tipoPago = tipoPago;}

    public String getClienteBusqueda() {return clienteBusqueda;}
    public void setClienteBusqueda(String clienteBusqueda) {this.clienteBusqueda = clienteBusqueda;}

    public Cliente getNuevoCliente() {return nuevoCliente;}
    public void setNuevoCliente(Cliente nuevoCliente) {this.nuevoCliente = nuevoCliente;}

    public BigDecimal getAdeudoGeneradoClienteCredito() {return adeudoGeneradoClienteCredito;}
    public void setAdeudoGeneradoClienteCredito(BigDecimal adeudoGeneradoClienteCredito) {this.adeudoGeneradoClienteCredito = adeudoGeneradoClienteCredito;}

    public String getNombreClienteCredito() {return nombreClienteCredito;}
    public void setNombreClienteCredito(String nombreClienteCredito) {this.nombreClienteCredito = nombreClienteCredito;}

    public List<Producto> getProductosStockBajo() {return productosStockBajo;}
    public void setProductosStockBajo(List<Producto> productosStockBajo) {this.productosStockBajo = productosStockBajo;}
}