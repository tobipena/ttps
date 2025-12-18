package org.example.ttps.models.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.ttps.models.enums.Estado;

import java.util.ArrayList;
import java.util.List;

@Data
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

    // Lista de imágenes en Base64
    private List<ImagenDTO> imagenes = new ArrayList<>();
}
