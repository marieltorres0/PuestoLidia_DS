package mx.puestoLidia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @Size(max = 30)
    @Column(name = "idProducto", nullable = false, length = 30)
    private String idProducto;

    @Size(max = 30)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;

    @NotNull
    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @Column(name = "umbral", nullable = false)
    private Integer umbral;

    @OneToMany(mappedBy = "idProducto")
    private Set<ItemVenta> itemVentas = new LinkedHashSet<>();

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getUmbral() {
        return umbral;
    }

    public void setUmbral(Integer umbral) {
        this.umbral = umbral;
    }

    public Set<ItemVenta> getItemVentas() {
        return itemVentas;
    }

    public void setItemVentas(Set<ItemVenta> itemVentas) {
        this.itemVentas = itemVentas;
    }

}