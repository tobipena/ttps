package org.example.ttps.models.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class DesaparicionDTO {
    @NotBlank(message = "El comentario es obligatorio")
    private String comentario;
    @NotBlank(message = "La coordenada es obligatoria")
    private String coordenada;

    //private byte[] foto;

    @NotNull(message = "La fecha es obligatoria")
    private Date fecha;
    @NotNull(message = "La mascota es obligatoria")
    @Valid
    private MascotaDTO mascotaDTO;
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    public String getComentario() {
        return comentario;
    }
    public String getCoordenada() {
        return coordenada;
    }
//    public byte[] getFoto() {
//        return foto;
//    }
    public Date getFecha() {
        return fecha;
    }
    public MascotaDTO getMascotaDTO() {
        return mascotaDTO;
    }
    public Long getUsuarioId() {
        return usuarioId;
    }
}
