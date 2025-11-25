package org.example.ttps.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.ttps.models.enums.Rol;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="USUARIO")
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USUARIO_ID")
    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    private Long id;
    private String nombre;
    @Column(unique = true,nullable = false)
    private String email;
    private String password;
    private Long telefono;
    private String barrio;
    private String ciudad;
    private Integer puntos;
    private Rol rol;
    private Boolean activo;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonManagedReference("usuario-desapariciones")
    private List<Desaparicion> desapariciones;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonManagedReference("usuario-avistamientos")
    private List<Avistamiento> avistamientos;

    @OneToMany(mappedBy = "publicador", cascade = CascadeType.ALL)
    @JsonManagedReference("usuario-mascotas")
    private List<Mascota> mascotas;

    public Usuario() {
        desapariciones = new ArrayList<Desaparicion>();
        avistamientos = new ArrayList<Avistamiento>();
        mascotas = new ArrayList<Mascota>();
        rol = Rol.USER;
        puntos = 0;
        activo = true;
    }

    public void agregarDesaparicion(Desaparicion d) {
        this.desapariciones.add(d);
    }
    public void agregarAvistamiento(Avistamiento a) {
        this.avistamientos.add(a);
    }
    public void agregarMascota(Mascota m) {
        this.mascotas.add(m);
    }
}
