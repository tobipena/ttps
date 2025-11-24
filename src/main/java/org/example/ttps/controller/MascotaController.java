package org.example.ttps.controller;

import org.example.ttps.models.Mascota;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.MascotaDTO;
import org.example.ttps.models.enums.Estado;
import org.example.ttps.repositories.MascotaRepository;
import org.example.ttps.repositories.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/mascotas")
public class MascotaController {
    private final MascotaRepository mascotaRepository;
    private final UsuarioRepository usuarioRepository;

    public MascotaController(MascotaRepository mascotaRepository,UsuarioRepository usuarioRepository) {
        this.mascotaRepository = mascotaRepository;
        this.usuarioRepository = usuarioRepository;
    }
    @GetMapping("/usuario/{user_id}")
    public List<Mascota> findByUsuarioId(@PathVariable Long user_id) {
        return mascotaRepository.findByPublicadorId(user_id);
    }
    private boolean noVacio(String s) {
        return s != null && !s.isEmpty();
    }
    @PutMapping("/{pet_id}")
    public Mascota editar(@PathVariable Long id_mascota, @RequestBody MascotaDTO mascotaDTO) {
        Mascota m = mascotaRepository.findById(id_mascota).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (noVacio(mascotaDTO.getNombre())) {m.setNombre(mascotaDTO.getNombre());}
        if (noVacio(mascotaDTO.getTamano())) {m.setTamano(mascotaDTO.getTamano());}
        if (mascotaDTO.getEstado() != null) {m.setEstado(mascotaDTO.getEstado());}
        if (noVacio(mascotaDTO.getDescripcion())) {m.setDescripcion(mascotaDTO.getDescripcion());}
        if (noVacio(mascotaDTO.getColor())) {m.setColor(mascotaDTO.getColor());}

        return mascotaRepository.save(m);
    }
    @DeleteMapping("/{pet_id}")
    public void borrar(@PathVariable Long id_mascota) {
        mascotaRepository.deleteById(id_mascota);
    }
    @GetMapping("/perdidas")
    public List<Mascota> findAll() {
        return mascotaRepository.findByEstadoOrEstado(Estado.PERDIDO_AJENO, Estado.PERDIDO_PROPIO);
    }
}
