package org.example.ttps.models;

import org.example.ttps.models.enums.TipoPublicacion;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="PUBLICACION")
public class Publicacion {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String comentario;
    private String coordenada;
    private byte[] foto;
    private Date fecha;

    @ManyToOne
    private Usuario usuario;

    @OneToOne
    private Mascota mascota;

    private TipoPublicacion tipoPublicacion;

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Long getId() {
        return id;
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
}
