package org.example.ttps.models;

import org.example.ttps.models.enums.Rol;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="USUARIO")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USUARIO_ID")
    private Long id;
    private String nombre;
    private String email;
    private String password;
    private Long telefono;
    private String barrio;
    private String ciudad;
    private Integer puntos;
    private Rol rol;

    @OneToMany
    private List<Publicacion> publicaciones;

    @OneToMany
    private List<Mascota> mascotas;

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
