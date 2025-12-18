package org.example.ttps.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.example.ttps.models.enums.Estado;

import java.util.ArrayList;
import java.util.List;


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

    @Deprecated // Mantener por compatibilidad, usar imagenes en su lugar
    private byte[] foto;

    private String descripcion;
    private String animal;
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("mascota-imagenes")
    private List<Imagen> imagenes = new ArrayList<>();

    @ManyToOne
    @JsonBackReference("usuario-mascotas")
    private Usuario publicador;

    @OneToOne(mappedBy="mascota", cascade = CascadeType.ALL)
    @JsonBackReference("desaparicion-mascota")
    private Desaparicion desaparicion;

    public void addImagen(Imagen imagen) {
        imagenes.add(imagen);
        imagen.setMascota(this);
    }

    public void removeImagen(Imagen imagen) {
        imagenes.remove(imagen);
        imagen.setMascota(null);
    }
}