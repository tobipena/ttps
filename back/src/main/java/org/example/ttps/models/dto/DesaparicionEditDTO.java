package org.example.ttps.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class DesaparicionEditDTO {
    @NotBlank(message = "El comentario es obligatorio")
    private String comentario;
    private Double latitud;
    private Double longitud;
    @NotNull(message = "La fecha es obligatoria")
    private Date fecha;
}
