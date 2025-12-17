package org.example.ttps.services;

import jakarta.validation.Valid;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.LoginDTO;
import org.example.ttps.models.dto.UsuarioDTO;
import org.example.ttps.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final GeorefService georefService;

    public AuthService(UsuarioRepository usuarioRepository, GeorefService georefService) {
        this.usuarioRepository = usuarioRepository;
        this.georefService = georefService;
    }

    public Usuario login(LoginDTO loginDTO) {
        Usuario u = usuarioRepository.findByEmail(loginDTO.getEmail()).orElseThrow(() -> new IllegalArgumentException("Datos incorrectos"));
        if (!u.getPassword().equals(loginDTO.getPassword())){
            throw new IllegalArgumentException("Datos incorrectos");
        }
        return u;
    }

    public Usuario crearUsuario(UsuarioDTO usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        
        Usuario u = new Usuario();
        u.setNombre(usuario.getNombre());
        u.setEmail(usuario.getEmail());
        u.setPassword(usuario.getPassword());
        u.setTelefono(usuario.getTelefono());
        
        // Si se proporcionan coordenadas, obtener ciudad y provincia desde georef-ar
        if (usuario.getLatitud() != null && usuario.getLongitud() != null) {
            u.setLatitud(usuario.getLatitud());
            u.setLongitud(usuario.getLongitud());
            
            // Obtener ubicación desde georef-ar (si falla, lanzará excepción)
            GeorefService.UbicacionInfo ubicacion = georefService.obtenerUbicacion(
                usuario.getLatitud(), 
                usuario.getLongitud()
            );
            u.setCiudad(ubicacion.getCiudad());
            u.setProvincia(ubicacion.getProvincia());
        } else {
            throw new IllegalArgumentException("Las coordenadas de ubicación son obligatorias");
        }
        
        return usuarioRepository.save(u);
    }

}
