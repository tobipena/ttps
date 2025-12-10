package org.example.ttps.controller;

import jakarta.validation.Valid;
import org.example.ttps.models.Avistamiento;
import org.example.ttps.models.dto.AvistamientoDTO;
import org.example.ttps.services.AvistamientoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avisamientos")
public class AvistamientoController {
    private final AvistamientoService avistamientoService;
    public AvistamientoController(AvistamientoService avistamientoService) {
        this.avistamientoService = avistamientoService;
    }
    @PostMapping("/create")
    public ResponseEntity<?> crearAvistamiento(@Valid @RequestBody AvistamientoDTO avistamientoDTO) {
        try {
            Avistamiento a = avistamientoService.crearAvistamiento(avistamientoDTO);
            return ResponseEntity.ok(a);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping()
    public ResponseEntity<?> findAll() {
        List<Avistamiento> avisamientos = avistamientoService.listarAvistamientos();
        return ResponseEntity.ok(avisamientos);
    }
}
