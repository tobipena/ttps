package org.example.ttps.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class DesaparicionEditDTO {
    @NotBlank(message = "El comentario es obligatorio")
    private String comentario;
    @NotBlank(message = "La coordenada es obligatoria")
    private String coordenada;
    @NotNull(message = "La fecha es obligatoria")
    private Date fecha;
}
