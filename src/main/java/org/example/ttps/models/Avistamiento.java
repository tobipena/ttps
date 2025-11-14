package org.example.ttps.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="AVISTAMIENTO")
public class Avistamiento {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private Long id;
    private String comentario;
    private String coordenada;
    private byte[] foto;
    private java.util.Date fecha;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Desaparicion desaparicion;


    public Long getId() {
        return id;
    }

    public String getComentario() {return comentario;}
    public void setComentario(String comentario) {this.comentario = comentario;}

    public String getCoordenada() {return coordenada;}
    public void setCoordenada(String coordenada) {this.coordenada = coordenada;}

    public Date getFecha() {return fecha;}
    public void setFecha(Date fecha) {this.fecha = fecha;}

    public Usuario getUsuario() {return usuario;}
    public void setUsuario(Usuario usuario) {this.usuario = usuario;}
}
