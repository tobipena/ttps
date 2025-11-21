package org.example.ttps.controller;

import org.example.ttps.models.dto.LoginDTO;
import org.example.ttps.repositories.UsuarioRepository;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.UsuarioDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody LoginDTO loginDTO) {
        Usuario u = usuarioRepository.findByEmail(loginDTO.getEmail()).orElseThrow(() -> new RuntimeException("Datos incorrectos"));
        if (!u.getPassword().equals(loginDTO.getPassword())){
            throw new RuntimeException("Datos incorrectos");
        }
        return u;
    }


}
