package org.example.ttps.controller;

import jakarta.validation.Valid;
import org.example.ttps.models.Desaparicion;
import org.example.ttps.models.dto.DesaparicionDTO;
import org.example.ttps.models.dto.DesaparicionEditDTO;
import org.example.ttps.services.DesaparicionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/desapariciones")
public class DesaparicionController {

    private final DesaparicionService desaparicionService;

    public DesaparicionController(DesaparicionService desaparicionService) {
        this.desaparicionService = desaparicionService;
    }

    @PostMapping
    public ResponseEntity<?> crearDesaparicion(@Valid @RequestBody DesaparicionDTO desaparicionDTO){
        try {
            Desaparicion d = desaparicionService.altaDesaparicion(desaparicionDTO);
            if (d != null) {
                return ResponseEntity.ok(d);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarDesaparicion(@PathVariable Long id, @RequestBody DesaparicionEditDTO desaparicionEditDTO) {
        try {
            Desaparicion desaparicion = desaparicionService.editarDesaparicion(id, desaparicionEditDTO);
            if (desaparicion != null) {
                return ResponseEntity.ok(desaparicion);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDesaparicion(@PathVariable Long id) {
        try {
            Desaparicion d = desaparicionService.eliminarDesaparicion(id);
            return ResponseEntity.ok(d);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarDesapariciones() {
        return ResponseEntity.ok(desaparicionService.listarDesapariciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDesaparicionPorId(@PathVariable Long id) {
        try {
            Desaparicion d = desaparicionService.obtenerDesaparicionPorId(id);
            return ResponseEntity.ok(d);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
