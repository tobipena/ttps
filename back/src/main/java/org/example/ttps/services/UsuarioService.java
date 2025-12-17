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

    private boolean noVacio(String s) {
        return s != null && !s.isEmpty();
    }

    public Usuario obtenerUsuario(Long userId) {
        return usuarioRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    public Usuario obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    public Usuario actualizarUsuario(Long user_id, UsuarioDTO usuarioDTO) {
        Usuario u = usuarioRepository.findById(user_id).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        
        // Solo validar email si está cambiando a uno diferente
        if (noVacio(usuarioDTO.getEmail()) && !usuarioDTO.getEmail().equals(u.getEmail())) {
            if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
                throw new IllegalArgumentException("El email nuevo ya está registrado");
            }
        }

        if (noVacio(usuarioDTO.getNombre())) {u.setNombre(usuarioDTO.getNombre());}
        if (noVacio(usuarioDTO.getEmail())) {u.setEmail(usuarioDTO.getEmail());}
        if (noVacio(usuarioDTO.getPassword())) {u.setPassword(usuarioDTO.getPassword());}
        if (noVacio(usuarioDTO.getTelefono())) {u.setTelefono(usuarioDTO.getTelefono());}
        if (noVacio(usuarioDTO.getCiudad())) {u.setCiudad(usuarioDTO.getCiudad());}
        if (noVacio(usuarioDTO.getProvincia())) {u.setProvincia(usuarioDTO.getProvincia());}
        if (usuarioDTO.getLatitud() != null) {u.setLatitud(usuarioDTO.getLatitud());}
        if (usuarioDTO.getLongitud() != null) {u.setLongitud(usuarioDTO.getLongitud());}

        return usuarioRepository.save(u);
    }

}
