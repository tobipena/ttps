package org.example.ttps.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="DESAPARICION")
public class Desaparicion {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private Long id;
    private String comentario;
    private String coordenada;
    private byte[] foto;
    private Date fecha;

    @ManyToOne
    private Usuario usuario;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mascota_id")
    private Mascota mascota;

    @OneToMany
    private List<Avistamiento> avistamientos;

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

    public void setUsuario(Usuario usuario) {this.usuario = usuario;}

    public Usuario getUsuario() {return usuario;}

    public void setMascota(Mascota mascota) {this.mascota = mascota;}

    public Mascota getMascota() {return mascota;}
}
