package mx.puestoLidia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ItemVentaId implements Serializable {
    private static final long serialVersionUID = 1938233717124382098L;
    @NotNull
    @Column(name = "idVenta", nullable = false)
    private Integer idVenta;

    @Size(max = 50)
    @NotNull
    @Column(name = "idProducto", nullable = false, length = 50)
    private String idProducto;

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ItemVentaId entity = (ItemVentaId) o;
        return Objects.equals(this.idProducto, entity.idProducto) &&
                Objects.equals(this.idVenta, entity.idVenta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto, idVenta);
    }

}