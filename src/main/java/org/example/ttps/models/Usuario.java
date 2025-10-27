package org.example.ttps.models;

import org.example.ttps.models.enums.Rol;

import jakarta.persistence.*;

import java.util.ArrayList;
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
    private String estado;

    @OneToMany
    private List<Desaparicion> desapariciones;

    @OneToMany
    private List<Avistamiento> avistamientos;

    @OneToMany
    private List<Mascota> mascotas;

    public Usuario() {
        desapariciones = new ArrayList<Desaparicion>();
        avistamientos = new ArrayList<Avistamiento>();
        mascotas = new ArrayList<Mascota>();
    }

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

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Avistamiento> getAvistamientos() {return avistamientos;}

    public List<Desaparicion> getDesapariciones() {return desapariciones;}

    public void addAvistamiento(Avistamiento avistamiento) {
        this.avistamientos.add(avistamiento);
    }

    public void addDesaparicion(Desaparicion desaparicion) {
        this.desapariciones.add(desaparicion);
    }

    public void setAvistamientos(List<Avistamiento> avistamientos) {this.avistamientos = avistamientos;}

    public void setDesapariciones(List<Desaparicion> desapariciones) {this.desapariciones = desapariciones;}
}
