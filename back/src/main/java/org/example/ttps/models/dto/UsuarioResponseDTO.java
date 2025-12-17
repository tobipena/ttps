package org.example.ttps.models.dto;

import lombok.Data;
import org.example.ttps.models.Usuario;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String ciudad;
    private String provincia;
    private Integer puntos;

    public static UsuarioResponseDTO fromUsuario(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setCiudad(usuario.getCiudad());
        dto.setProvincia(usuario.getProvincia());
        dto.setPuntos(usuario.getPuntos());
        return dto;
    }
}
