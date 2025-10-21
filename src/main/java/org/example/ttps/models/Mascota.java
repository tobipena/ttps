package org.example.ttps.models;

import jakarta.persistence.*;
import org.example.ttps.models.enums.Estado;

import javax.persistence.*;

@Entity
@Table(name="MASCOTA")
public class Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String tamaño;
    private String color;
    private byte[] foto;
    private String descripcion;
    private String animal;
    private Estado estado;

    @ManyToOne
    private Usuario dueño;

    @OneToOne
    private Publicacion publicacion;

}
