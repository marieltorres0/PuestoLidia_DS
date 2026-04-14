package mx.puestoLidia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "rol", nullable = false, columnDefinition = "ENUM('admin','cajero')")
    private String rol;

    @Size(max = 50)
    @NotNull
    @Column(name = "contrasena", nullable = false, length = 50)
    private String contrasena;

    @OneToMany(mappedBy = "idUsuario")
    private Set<Abono> abonos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idUsuario")
    private Set<Venta> ventas = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
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