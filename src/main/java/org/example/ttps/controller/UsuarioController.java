package org.example.ttps.controller;

import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.UsuarioDTO;
import org.example.ttps.repositories.UsuarioRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public Usuario altaUsuario(@RequestBody UsuarioDTO usuario){
        Usuario u = new Usuario();
        u.setNombre(usuario.getNombre());
        u.setEmail(usuario.getEmail());
        u.setPassword(usuario.getPassword());
        u.setTelefono(usuario.getTelefono());
        u.setBarrio(usuario.getBarrio());
        u.setCiudad(usuario.getCiudad());
        return usuarioRepository.save(u);
    }

}
