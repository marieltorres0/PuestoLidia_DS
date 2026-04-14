package mx.puestoLidia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCliente", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Size(max = 10)
    @Column(name = "telefono", length = 10)
    private String telefono;

    @NotNull
    @Column(name = "adeudo", nullable = false, precision = 10, scale = 2)
    private BigDecimal adeudo;

    @OneToMany(mappedBy = "idCliente")
    private Set<Abono> abonos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idCliente")
    private Set<Venta> ventas = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public BigDecimal getAdeudo() {
        return adeudo;
    }

    public void setAdeudo(BigDecimal adeudo) {
        this.adeudo = adeudo;
    }

    public Set<Abono> getAbonos() {
        return abonos;
    }

    public void setAbonos(Set<Abono> abonos) {
        this.abonos = abonos;
    }

    public Set<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(Set<Venta> ventas) {
        this.ventas = ventas;
    }

}