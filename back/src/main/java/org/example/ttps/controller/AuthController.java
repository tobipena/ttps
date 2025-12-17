package org.example.ttps.controller;

import jakarta.validation.Valid;
import org.example.ttps.models.dto.AuthResponseDTO;
import org.example.ttps.models.dto.LoginDTO;
import org.example.ttps.models.dto.UsuarioDTO;
import org.example.ttps.models.Usuario;
import org.example.ttps.services.AuthService;
import org.example.ttps.services.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final TokenService tokenService;

    private static final int TOKEN_EXPIRATION = 3600000; // 1 hora en milisegundos

    public AuthController(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            Usuario u = authService.login(loginDTO);
            String token = tokenService.generateToken(u, TOKEN_EXPIRATION);
            AuthResponseDTO response = new AuthResponseDTO(
                token, 
                TOKEN_EXPIRATION, 
                u.getNombre()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario u = authService.crearUsuario(usuarioDTO);
            String token = tokenService.generateToken(u, TOKEN_EXPIRATION);
            AuthResponseDTO response = new AuthResponseDTO(
                token, 
                TOKEN_EXPIRATION, 
                u.getNombre()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(e.getMessage());
        }
    }
}
