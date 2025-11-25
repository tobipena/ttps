package org.example.ttps.services;

import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.UsuarioDTO;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    public Usuario crearUsuario(UsuarioDTO usuario) {
        Usuario u = new Usuario();
        u.setNombre(usuario.getNombre());
        u.setEmail(usuario.getEmail());
        u.setPassword(usuario.getPassword());
        u.setTelefono(usuario.getTelefono());
        u.setBarrio(usuario.getBarrio());
        u.setCiudad(usuario.getCiudad());
        return u;
    }

    private boolean noVacio(String s) {
        return s != null && !s.isEmpty();
    }
    public Usuario actualizarUsuario(Usuario usuarioExistente, UsuarioDTO usuarioDTO) {
        if (noVacio(usuarioDTO.getNombre())) {usuarioExistente.setNombre(usuarioDTO.getNombre());}
        if (noVacio(usuarioDTO.getEmail())) {
            usuarioExistente.setEmail(usuarioDTO.getEmail());
        }
        if (noVacio(usuarioDTO.getPassword())) {usuarioExistente.setPassword(usuarioDTO.getPassword());}
        if (usuarioDTO.getTelefono() != null) {usuarioExistente.setTelefono(usuarioDTO.getTelefono());}
        if (noVacio(usuarioDTO.getBarrio())) {usuarioExistente.setBarrio(usuarioDTO.getBarrio());}
        if (noVacio(usuarioDTO.getCiudad())) {usuarioExistente.setCiudad(usuarioDTO.getCiudad());}
        return usuarioExistente;
    }

}
