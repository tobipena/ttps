package org.example.ttps.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="DESAPARICION")
@Data
public class Desaparicion {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private Long id;
    private String comentario;
    private Double latitud;
    private Double longitud;
    private byte[] foto;
    private Date fecha;

    @ManyToOne
    @JsonBackReference("usuario-desapariciones")
    private Usuario usuario;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mascota_id")
    @JsonManagedReference("desaparicion-mascota")
    private Mascota mascota;

    @OneToMany(mappedBy = "desaparicion", cascade = CascadeType.ALL)
    @JsonManagedReference("desaparicion-avistamientos")
    private List<Avistamiento> avistamientos;

    public void agregarAvistamiento(Avistamiento a) {
        this.avistamientos.add(a);
    }
}
