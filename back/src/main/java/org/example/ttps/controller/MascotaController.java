package org.example.ttps.controller;

import org.example.ttps.models.Mascota;
import org.example.ttps.models.dto.MascotaDTO;
import org.example.ttps.services.MascotaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/mascotas")
public class MascotaController {
    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }
    @GetMapping("/usuario/{user_id}")
    public ResponseEntity<?> findByUsuarioId(@PathVariable Long user_id) {
        List<Mascota> mascotas = mascotaService.listarMascotasPorUsuario(user_id);
        return ResponseEntity.ok(mascotas);
    }

    @PutMapping("/{pet_id}")
    public ResponseEntity<?> editar(@PathVariable Long pet_id, @RequestBody MascotaDTO mascotaDTO) {
        try {
            Mascota m = mascotaService.editarMascota(mascotaDTO, pet_id);
            return ResponseEntity.ok(m);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/perdidas")
    public ResponseEntity<?> findAll() {
        List<Mascota> mascotas = mascotaService.listarMascotasPerdidas();
        return ResponseEntity.ok(mascotas);
    }
}
