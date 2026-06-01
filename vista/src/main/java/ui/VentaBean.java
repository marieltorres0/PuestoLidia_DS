package ui;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import mx.puestoLidia.entity.ItemVenta;
import mx.puestoLidia.entity.Producto;
import helper.VentaHelper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Named("ventaBean")
@ViewScoped
public class VentaBean implements Serializable {

    private String textoBusqueda;
    private List<ItemVenta> listaCompra;
    private ItemVenta detalleSeleccionado;

    private BigDecimal totalCompra;
    private boolean tipoVentaCredito;
    private BigDecimal montoRecibido;
    private BigDecimal cambio;

    private List<Producto> productosCategoria; // Lista para el modal de botones

    private VentaHelper helper;

    @PostConstruct
    public void init() {
        helper = new VentaHelper();
        limpiarPantalla();
    }

    public void limpiarPantalla() {
        listaCompra = new ArrayList<>();
        textoBusqueda = "";
        totalCompra = BigDecimal.ZERO;
        montoRecibido = null;
        cambio = BigDecimal.ZERO;
        tipoVentaCredito = false;
        detalleSeleccionado = null;
        productosCategoria = new ArrayList<>();
    }

    // ==========================================
    // ESCÁNER Y BÚSQUEDA MANUAL
    // ==========================================
    public void buscarAgregarProducto() {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) return;

        Producto p = helper.buscarProducto(textoBusqueda);

        if (p != null) {
            agregarProductoAlTicket(p);
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "No encontrado", "El producto no existe."));
        }
        textoBusqueda = "";
    }

    // ==========================================
    // LÓGICA DE BOTONES DE CATEGORÍA
    // ==========================================
    public void cargarCategoria(String prefijo) {
        productosCategoria = helper.buscarProductosPorPrefijo(prefijo);
    }

    public void agregarDesdeCategoria(Producto p) {
        agregarProductoAlTicket(p);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregado", p.getNombre() + " añadido al ticket."));
    }

    // Método auxiliar para no repetir código de agregar al ticket
    private void agregarProductoAlTicket(Producto p) {
        boolean existe = false;
        for (ItemVenta item : listaCompra) {
            if (item.getIdProducto().getIdProducto().equals(p.getIdProducto())) {
                aumentarCantidad(item);
                existe = true;
                break;
            }
        }
        if (!existe) {
            ItemVenta nuevoItem = new ItemVenta();
            nuevoItem.setIdProducto(p);
            nuevoItem.setCantidad(1);
            nuevoItem.setPrecioUnitario(p.getPrecio());
            nuevoItem.setImporte(p.getPrecio().multiply(BigDecimal.ONE));

            listaCompra.add(nuevoItem);
            calcularTotal();
        }
    }

    // ==========================================
    // CONTROL DE CANTIDADES Y ELIMINACIÓN
    // ==========================================
    public void aumentarCantidad(ItemVenta item) {
        item.setCantidad(item.getCantidad() + 1);
        item.setImporte(item.getPrecioUnitario().multiply(new BigDecimal(item.getCantidad())));
        calcularTotal();
    }

    public void disminuirCantidad(ItemVenta item) {
        if (item.getCantidad() == 1) {
            listaCompra.remove(item);
        } else {
            item.setCantidad(item.getCantidad() - 1);
            item.setImporte(item.getPrecioUnitario().multiply(new BigDecimal(item.getCantidad())));
        }
        calcularTotal();
    }

    public void eliminarProductoSeleccionado() {
        if (detalleSeleccionado != null) {
            listaCompra.remove(detalleSeleccionado);
            detalleSeleccionado = null;
            calcularTotal();
        }
    }

    // ==========================================
    // MATEMÁTICAS Y COBRO
    // ==========================================
    private void calcularTotal() {
        totalCompra = BigDecimal.ZERO;
        for (ItemVenta item : listaCompra) {
            totalCompra = totalCompra.add(item.getImporte());
        }
        calcularCambio();
    }

    public void calcularCambio() {
        if (montoRecibido != null && montoRecibido.compareTo(totalCompra) >= 0) {
            cambio = montoRecibido.subtract(totalCompra);
        } else {
            cambio = BigDecimal.ZERO;
        }
    }

    public void finalizarVenta() {
        if (listaCompra.isEmpty()) return;

        if (!tipoVentaCredito && (montoRecibido == null || montoRecibido.compareTo(totalCompra) < 0)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Monto insuficiente."));
            return;
        }

        try {
            helper.procesarVenta(listaCompra, totalCompra, montoRecibido, cambio, tipoVentaCredito);
            limpiarPantalla();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Venta cobrada. Inventario actualizado."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================
    public String getTextoBusqueda() { return textoBusqueda; }
    public void setTextoBusqueda(String textoBusqueda) { this.textoBusqueda = textoBusqueda; }
    public List<ItemVenta> getListaCompra() { return listaCompra; }
    public void setListaCompra(List<ItemVenta> listaCompra) { this.listaCompra = listaCompra; }
    public ItemVenta getDetalleSeleccionado() { return detalleSeleccionado; }
    public void setDetalleSeleccionado(ItemVenta detalleSeleccionado) { this.detalleSeleccionado = detalleSeleccionado; }
    public BigDecimal getTotalCompra() { return totalCompra; }
    public void setTotalCompra(BigDecimal totalCompra) { this.totalCompra = totalCompra; }
    public boolean isTipoVentaCredito() { return tipoVentaCredito; }
    public void setTipoVentaCredito(boolean tipoVentaCredito) { this.tipoVentaCredito = tipoVentaCredito; }
    public BigDecimal getMontoRecibido() { return montoRecibido; }
    public void setMontoRecibido(BigDecimal montoRecibido) { this.montoRecibido = montoRecibido; }
    public BigDecimal getCambio() { return cambio; }
    public void setCambio(BigDecimal cambio) { this.cambio = cambio; }
    public List<Producto> getProductosCategoria() { return productosCategoria; }
    public void setProductosCategoria(List<Producto> productosCategoria) { this.productosCategoria = productosCategoria; }
}