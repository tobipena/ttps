package org.example.ttps.controller;

import org.example.ttps.models.Mascota;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.MascotaDTO;
import org.example.ttps.models.enums.Estado;
import org.example.ttps.repositories.MascotaRepository;
import org.example.ttps.repositories.UsuarioRepository;
import org.example.ttps.services.MascotaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/mascotas")
public class MascotaController {
    private final MascotaRepository mascotaRepository;
    private final MascotaService mascotaService;

    public MascotaController(MascotaRepository mascotaRepository,MascotaService mascotaService) {
        this.mascotaService = mascotaService;
        this.mascotaRepository = mascotaRepository;
    }
    @GetMapping("/usuario/{user_id}")
    public List<Mascota> findByUsuarioId(@PathVariable Long user_id) {
        return mascotaRepository.findByPublicadorId(user_id);
    }

    @PutMapping("/{pet_id}")
    public Mascota editar(@PathVariable Long id_mascota, @RequestBody MascotaDTO mascotaDTO) {
        Mascota m = mascotaRepository.findById(id_mascota).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        m = mascotaService.editarMascota(mascotaDTO, m);
        return mascotaRepository.save(m);
    }

    @GetMapping("/perdidas")
    public List<Mascota> findAll() {
        return mascotaRepository.findByEstadoOrEstado(Estado.PERDIDO_AJENO, Estado.PERDIDO_PROPIO);
    }
}
