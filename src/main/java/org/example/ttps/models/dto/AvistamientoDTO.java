package org.example.ttps.models.dto;

import java.util.Date;

public class AvistamientoDTO {
    private String comentario;
    private String coordenada;
    private byte[] foto;
    private Date fecha;
    private Long desaparicionId;
    private Long usuarioId;

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(String coordenada) {
        this.coordenada = coordenada;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getDesaparicionId() {
        return desaparicionId;
    }

    public void setDesaparicionId(Long desaparicionId) {
        this.desaparicionId = desaparicionId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
