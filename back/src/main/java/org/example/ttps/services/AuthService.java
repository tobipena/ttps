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

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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
        
        if (usuario.getBarrio() != null) {
            u.setBarrio(usuario.getBarrio());
        }
        if (usuario.getCiudad() != null) {
            u.setCiudad(usuario.getCiudad());
        }
        
        return usuarioRepository.save(u);
    }

}
