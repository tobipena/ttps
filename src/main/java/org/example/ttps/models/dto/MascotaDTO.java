package org.example.ttps.models.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.ttps.models.enums.Estado;

public class MascotaDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    @NotBlank(message = "El tamaño no puede estar vacío")
    private String tamano;
    @NotBlank(message = "El color no puede estar vacío")
    private String color;
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;
    @NotNull(message = "El estado no puede ser nulo")
    private Estado estado;
    @NotBlank(message = "El tipo de animal no puede estar vacío")
    private String animal;

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public String getAnimal() {
        return animal;
    }
}
