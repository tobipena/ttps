package org.example.ttps.models.dto;

import lombok.Data;
import org.example.ttps.models.enums.Estado;

import java.util.Date;
import java.util.List;

@Data
public class DesaparicionResponseDTO {
    private Long id;
    private String comentario;
    private Double latitud;
    private Double longitud;
    private byte[] foto;
    private Date fecha;
    private MascotaSimpleDTO mascota;

    @Data
    public static class MascotaSimpleDTO {
        private Long id;
        private String nombre;
        private String tamano;
        private String color;

        @Deprecated // Mantener por compatibilidad
        private byte[] foto;

        private String descripcion;
        private String animal;
        private Estado estado;

        // Nueva lista de imágenes
        private List<ImagenSimpleDTO> imagenes;
    }

    @Data
    public static class ImagenSimpleDTO {
        private Long id;
        private byte[] datos;
        private String tipo;
    }
}
