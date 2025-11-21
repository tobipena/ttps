package org.example.ttps.controller;

import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.UsuarioDTO;
import org.example.ttps.repositories.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/register")
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
    private boolean noVacio(String s) {
        return s != null && !s.isEmpty();
    }
    @PutMapping("edit/{user_id}")
    public Usuario editarUsuario(@RequestBody UsuarioDTO datos, @PathVariable Long user_id){
        Usuario u = usuarioRepository.findById(user_id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (noVacio(datos.getNombre())) {u.setNombre(datos.getNombre());}
        if (noVacio(datos.getEmail())) {u.setEmail(datos.getEmail());}
        if (noVacio(datos.getPassword())) {u.setPassword(datos.getPassword());}
        if (datos.getTelefono() != null) {u.setTelefono(datos.getTelefono());}
        if (noVacio(datos.getBarrio())) {u.setBarrio(datos.getBarrio());}
        if (noVacio(datos.getCiudad())) {u.setCiudad(datos.getCiudad());}

        return usuarioRepository.save(u);
    }

}
