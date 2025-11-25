package org.example.ttps.controller;

import jakarta.validation.Valid;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.UsuarioDTO;
import org.example.ttps.repositories.UsuarioRepository;
import org.example.ttps.services.UsuarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioRepository usuarioRepository, UsuarioService usuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public Usuario altaUsuario(@Valid @RequestBody UsuarioDTO usuario){
        if (usuarioRepository.existsUsuarioByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        Usuario u = usuarioService.crearUsuario(usuario);
        return usuarioRepository.save(u);
    }

    @PutMapping("edit/{user_id}")
    public Usuario editarUsuario(@RequestBody UsuarioDTO usuarioDTO, @PathVariable Long user_id){
        Usuario u = usuarioRepository.findById(user_id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (usuarioRepository.existsUsuarioByEmail(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("El email nuevo ya está registrado");
        }
        u = usuarioService.actualizarUsuario(u, usuarioDTO);
        return usuarioRepository.save(u);
    }

}
