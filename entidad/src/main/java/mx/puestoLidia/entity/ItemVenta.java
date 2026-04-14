package mx.puestoLidia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "item_venta")
public class ItemVenta {
    @EmbeddedId
    private ItemVentaId id;

    @MapsId("idVenta")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idVenta", nullable = false)
    private Venta idVenta;

    @MapsId("idProducto")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto idProducto;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @Column(name = "precioUnitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @NotNull
    @Column(name = "importe", nullable = false, precision = 10, scale = 2)
    private BigDecimal importe;

    public ItemVentaId getId() {
        return id;
    }

    public void setId(ItemVentaId id) {
        this.id = id;
    }

    public Venta getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Venta idVenta) {
        this.idVenta = idVenta;
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

}