package org.example.ttps.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.UsuarioDTO;
import org.example.ttps.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> altaUsuario(@Valid @RequestBody UsuarioDTO usuario){
        try {
            Usuario u = usuarioService.crearUsuario(usuario);
            if (u != null) {
                return ResponseEntity.ok(u);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("edit/{user_id}")
    public ResponseEntity<?> editarUsuario(@RequestBody UsuarioDTO usuarioDTO, @PathVariable Long user_id){
        try {
            Usuario u = usuarioService.actualizarUsuario(user_id, usuarioDTO);
            return ResponseEntity.ok(u);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
