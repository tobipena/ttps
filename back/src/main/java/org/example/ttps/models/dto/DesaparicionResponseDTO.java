package org.example.ttps.models.dto;

import lombok.Data;
import org.example.ttps.models.enums.Estado;

import java.util.Date;

@Data
public class DesaparicionResponseDTO {
    private Long id;
    private String comentario;
    private String coordenada;
    private byte[] foto;
    private Date fecha;
    private MascotaSimpleDTO mascota;

    @Data
    public static class MascotaSimpleDTO {
        private Long id;
        private String nombre;
        private String tamano;
        private String color;
        private byte[] foto;
        private String descripcion;
        private String animal;
        private Estado estado;
    }
}
