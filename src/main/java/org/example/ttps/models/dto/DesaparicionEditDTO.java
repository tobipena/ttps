package org.example.ttps.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class DesaparicionEditDTO {
    @NotBlank(message = "El comentario es obligatorio")
    private String comentario;
    @NotBlank(message = "La coordenada es obligatoria")
    private String coordenada;
    @NotNull(message = "La fecha es obligatoria")
    private Date fecha;

    public String getComentario() {
        return comentario;
    }

    public String getCoordenada() {
        return coordenada;
    }

    public Date getFecha() {
        return fecha;
    }
}
