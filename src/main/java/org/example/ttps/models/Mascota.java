package org.example.ttps.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.example.ttps.models.enums.Estado;


@Entity
@Table(name="MASCOTA")
@Data
public class Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private Long id;
    private String nombre;
    private String tamano;
    private String color;
    private byte[] foto;
    private String descripcion;
    private String animal;
    private Estado estado;

    @ManyToOne
    @JsonBackReference("usuario-mascotas")
    private Usuario publicador;

    @OneToOne(mappedBy="mascota", cascade = CascadeType.ALL)
    @JsonBackReference("desaparicion-mascota")
    private Desaparicion desaparicion;
}