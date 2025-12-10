package org.example.ttps.services;

import jakarta.validation.Valid;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.LoginDTO;
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

}
