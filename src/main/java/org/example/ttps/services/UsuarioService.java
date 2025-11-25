package org.example.ttps.services;

import jakarta.persistence.EntityNotFoundException;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.UsuarioDTO;
import org.example.ttps.repositories.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario crearUsuario(UsuarioDTO usuario) {
        if (usuarioRepository.existsUsuarioByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
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
    public Usuario actualizarUsuario(Long user_id, UsuarioDTO usuarioDTO) {
        Usuario u = usuarioRepository.findById(user_id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        if (usuarioRepository.existsUsuarioByEmail(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("El email nuevo ya está registrado");
        }

        if (noVacio(usuarioDTO.getNombre())) {u.setNombre(usuarioDTO.getNombre());}
        if (noVacio(usuarioDTO.getEmail())) {
            u.setEmail(usuarioDTO.getEmail());
        }
        if (noVacio(usuarioDTO.getPassword())) {u.setPassword(usuarioDTO.getPassword());}
        if (usuarioDTO.getTelefono() != null) {u.setTelefono(usuarioDTO.getTelefono());}
        if (noVacio(usuarioDTO.getBarrio())) {u.setBarrio(usuarioDTO.getBarrio());}
        if (noVacio(usuarioDTO.getCiudad())) {u.setCiudad(usuarioDTO.getCiudad());}

        return usuarioRepository.save(u);
    }

}
