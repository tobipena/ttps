package org.example.ttps.controller;

import org.example.ttps.models.Desaparicion;
import org.example.ttps.models.Mascota;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.DesaparicionDTO;
import org.example.ttps.models.dto.DesaparicionEditDTO;
import org.example.ttps.models.dto.MascotaDTO;
import org.example.ttps.models.enums.Estado;
import org.example.ttps.repositories.DesaparicionRepository;
import org.example.ttps.repositories.MascotaRepository;
import org.example.ttps.repositories.UsuarioRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/desapariciones")
public class DesaparicionController {

    private final UsuarioRepository usuarioRepository;
    private final DesaparicionRepository desaparicionRepository;
    private final MascotaRepository mascotaRepository;

    public DesaparicionController(DesaparicionRepository desaparicionRepository,
                                  MascotaRepository mascotaRepository,
                                  UsuarioRepository usuarioRepository) {
        this.desaparicionRepository = desaparicionRepository;
        this.mascotaRepository = mascotaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public Desaparicion crearDesaparicion(@RequestBody DesaparicionDTO desaparicionDTO){
        if (desaparicionDTO.getMascotaDTO() == null) {
            throw new IllegalArgumentException("La información de la mascota es obligatoria");
        }
        if (desaparicionDTO.getUsuarioId() == null) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio");
        }
        Usuario u = usuarioRepository.findById(desaparicionDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        MascotaDTO mascotaDTO = desaparicionDTO.getMascotaDTO();
        Mascota m = new Mascota();
        m.setNombre(mascotaDTO.getNombre());
        m.setTamano(mascotaDTO.getTamano());
        m.setEstado(mascotaDTO.getEstado());
        m.setDescripcion(mascotaDTO.getDescripcion());
        m.setColor(mascotaDTO.getColor());
        m.setAnimal(mascotaDTO.getAnimal());
        m.setPublicador(u);
        Mascota mascotaGuardada = mascotaRepository.save(m);
        u.agregarMascota(mascotaGuardada);

        Desaparicion d = new Desaparicion();
        d.setFecha(desaparicionDTO.getFecha());
        d.setComentario(desaparicionDTO.getComentario());
        d.setCoordenada(desaparicionDTO.getCoordenada());
        d.setMascota(mascotaGuardada);
        d.setUsuario(u);

        u.agregarDesaparicion(d);
        return desaparicionRepository.save(d);
    }

    @PutMapping("/{id}")
    public Desaparicion editarDesaparicion(@PathVariable Long id, @RequestBody DesaparicionEditDTO desaparicionEditDTO) {
        Desaparicion desaparicion = desaparicionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Desaparición no encontrada"));

        desaparicion.setFecha(desaparicionEditDTO.getFecha());
        desaparicion.setComentario(desaparicionEditDTO.getComentario());
        desaparicion.setCoordenada(desaparicionEditDTO.getCoordenada());

        return desaparicionRepository.save(desaparicion);
    }

    @DeleteMapping("/{id}")
    public void eliminarDesaparicion(@PathVariable Long id) {
        Desaparicion desaparicion = desaparicionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Desaparición no encontrada"));
        
        Usuario usuario = desaparicion.getUsuario();
        Mascota mascota = desaparicion.getMascota();
        
        if (usuario != null) {
            usuario.getDesapariciones().remove(desaparicion);
        }
        desaparicionRepository.delete(desaparicion);
    }

}
