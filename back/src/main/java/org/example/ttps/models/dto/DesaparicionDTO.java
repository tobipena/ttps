package org.example.ttps.models.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class DesaparicionDTO {
    @NotBlank(message = "El comentario es obligatorio")
    private String comentario;
    @NotNull(message = "La latitud es obligatoria")
    private Double latitud;
    @NotNull(message = "La longitud es obligatoria")
    private Double longitud;

    //private byte[] foto;

    @NotNull(message = "La fecha es obligatoria")
    private Date fecha;
    @NotNull(message = "La mascota es obligatoria")
    @Valid
    private MascotaDTO mascotaDTO;
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;
}
