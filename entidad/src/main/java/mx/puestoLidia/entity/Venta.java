package mx.puestoLidia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "venta")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVenta", nullable = false)
    private Integer id;

    @Column(name = "fechaHora")
    private Instant fechaHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCliente")
    private Cliente idCliente;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario idUsuario;

    @NotNull
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @NotNull
    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @NotNull
    @Column(name = "cambio", nullable = false, precision = 10, scale = 2)
    private BigDecimal cambio;

    @NotNull
    @Column(name = "tipo", nullable = false,columnDefinition = "ENUM('contado','credito')")
    private String tipo;

    @OneToMany(mappedBy = "idVenta")
    private Set<ItemVenta> itemVentas = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Instant fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getCambio() {
        return cambio;
    }

    public void setCambio(BigDecimal cambio) {
        this.cambio = cambio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Set<ItemVenta> getItemVentas() {
        return itemVentas;
    }

    public void setItemVentas(Set<ItemVenta> itemVentas) {
        this.itemVentas = itemVentas;
    }

}