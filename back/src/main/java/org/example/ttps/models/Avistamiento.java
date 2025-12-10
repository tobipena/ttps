package org.example.ttps.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name="AVISTAMIENTO")
@Data
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
    @JsonBackReference("usuario-avistamientos")
    private Usuario usuario;

    @ManyToOne
    @JsonBackReference("desaparicion-avistamientos")
    private Desaparicion desaparicion;
}
