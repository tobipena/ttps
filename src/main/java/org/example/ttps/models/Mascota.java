package org.example.ttps.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.example.ttps.models.enums.Estado;


@Entity
@Table(name="MASCOTA")
public class Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private Long id;
    private String nombre;
    private String tamano;
    private String color;
    private byte[] foto;
    private String descripcion;
    private String animal;
    private Estado estado;

    @ManyToOne
    private Usuario publicador;

    @OneToOne(mappedBy="mascota")
    private Desaparicion desaparicion;

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public void setPublicador(Usuario publicador) {
        this.publicador = publicador;
    }
}