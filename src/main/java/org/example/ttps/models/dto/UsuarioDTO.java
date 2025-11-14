package org.example.ttps.models.dto;

public class UsuarioDTO {

    private String nombre;
    private String email;
    private String password;
    private Long telefono;
    private String barrio;
    private String ciudad;

    public String getNombre() {return nombre;}
    public String getEmail() {return email;}
    public String getPassword() {return password;}
    public Long getTelefono() {return telefono;}
    public String getBarrio() {return barrio;}
    public String getCiudad() {return ciudad;}
}
