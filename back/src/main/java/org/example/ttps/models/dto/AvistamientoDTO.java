package org.example.ttps.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class AvistamientoDTO {
    @NotBlank(message = "El comentario es obligatorio")
    private String comentario;
    @NotBlank(message = "La coordenada es obligatoria")
    private String coordenada;
//    private byte[] foto;
    @NotNull(message = "La fecha es obligatoria")
    private Date fecha;
    @NotNull(message = "El ID de la desaparición es obligatorio")
    private Long desaparicionId;
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;
}
