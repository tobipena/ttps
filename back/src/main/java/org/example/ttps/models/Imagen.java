package org.example.ttps.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "imagenes")
@Data
@NoArgsConstructor
public class Imagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "datos", columnDefinition = "LONGBLOB")
    private byte[] datos;

    @Column(name = "tipo", nullable = false)
    private String tipo; // "image/jpeg", "image/png", etc.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascota_id")
    @JsonBackReference("mascota-imagenes")
    private Mascota mascota;

    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida;

    @PrePersist
    protected void onCreate() {
        fechaSubida = LocalDateTime.now();
    }
}

