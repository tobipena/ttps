package org.example.ttps.controller;

import jakarta.validation.Valid;
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
import org.example.ttps.services.DesaparicionService;
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
    private final DesaparicionService desaparicionService;

    public DesaparicionController(DesaparicionRepository desaparicionRepository,
                                  MascotaRepository mascotaRepository,
                                  UsuarioRepository usuarioRepository,
                                  DesaparicionService desaparicionService) {
        this.desaparicionService = desaparicionService;
        this.desaparicionRepository = desaparicionRepository;
        this.mascotaRepository = mascotaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public Desaparicion crearDesaparicion(@Valid @RequestBody DesaparicionDTO desaparicionDTO){
        Usuario u = usuarioRepository.findById(desaparicionDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        MascotaDTO mascotaDTO = desaparicionDTO.getMascotaDTO();
        Mascota m = desaparicionService.crearMascota(mascotaDTO, u);
        Mascota mascotaGuardada = mascotaRepository.save(m);

        u.agregarMascota(mascotaGuardada);

        Desaparicion d = desaparicionService.crearDesaparicion(desaparicionDTO, mascotaGuardada, u);

        u.agregarDesaparicion(d);
        return desaparicionRepository.save(d);
    }

    @PutMapping("/{id}")
    public Desaparicion editarDesaparicion(@PathVariable Long id, @RequestBody DesaparicionEditDTO desaparicionEditDTO) {
        Desaparicion desaparicion = desaparicionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Desaparición no encontrada"));

        desaparicion = desaparicionService.editarDesaparicion(desaparicion, desaparicionEditDTO);

        return desaparicionRepository.save(desaparicion);
    }

    @DeleteMapping("/{id}")
    public void eliminarDesaparicion(@PathVariable Long id) {
        Desaparicion desaparicion = desaparicionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Desaparición no encontrada"));
        
        Usuario usuario = desaparicion.getUsuario();
        
        if (usuario != null) {
            usuario.getDesapariciones().remove(desaparicion);
        }
        desaparicionRepository.delete(desaparicion);
    }

}
