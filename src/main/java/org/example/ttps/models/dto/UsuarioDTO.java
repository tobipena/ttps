package org.example.ttps.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un formato válido")
    private String email;
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
    @NotNull(message = "El telefono no puede estar vacío")
    private Long telefono;
    @NotBlank(message = "El barrio no puede estar vacío")
    private String barrio;
    @NotBlank(message = "La ciudad no puede estar vacío")
    private String ciudad;
}
