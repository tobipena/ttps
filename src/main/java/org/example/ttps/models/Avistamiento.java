package org.example.ttps.models;

import jakarta.persistence.*;

@Entity
@Table(name="AVISTAMIENTO")
public class Avistamiento {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
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
}
