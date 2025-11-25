package org.example.ttps.controller;

import jakarta.validation.Valid;
import org.example.ttps.models.Avistamiento;
import org.example.ttps.models.Desaparicion;
import org.example.ttps.models.Mascota;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.AvistamientoDTO;
import org.example.ttps.repositories.AvistamientoRepository;
import org.example.ttps.repositories.DesaparicionRepository;
import org.example.ttps.repositories.MascotaRepository;
import org.example.ttps.repositories.UsuarioRepository;
import org.example.ttps.services.AvistamientoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avisamientos")
public class AvistamientoController {
    private final AvistamientoRepository avistamientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DesaparicionRepository desaparicionRepository;
    private final AvistamientoService avistamientoService;
    public AvistamientoController(AvistamientoRepository avistamientoRepository,
                                  UsuarioRepository usuarioRepository,
                                  DesaparicionRepository desaparicionRepository,
                                  AvistamientoService avistamientoService) {
        this.avistamientoService = avistamientoService;
        this.avistamientoRepository = avistamientoRepository;
        this.usuarioRepository = usuarioRepository;
        this.desaparicionRepository = desaparicionRepository;
    }
    @PostMapping("/create")
    public Avistamiento crearAvistamiento(@Valid @RequestBody AvistamientoDTO avistamientoDTO) {
        Usuario u = usuarioRepository.findById(avistamientoDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Desaparicion d = desaparicionRepository.findById(avistamientoDTO.getDesaparicionId())
                .orElseThrow(() -> new RuntimeException("Desaparición no encontrada"));

        Avistamiento a = avistamientoService.crearAvistamiento(avistamientoDTO, u, d);

        u.agregarAvistamiento(a);
        d.agregarAvistamiento(a);
        return avistamientoRepository.save(a);
    }
    @GetMapping()
    public List<Avistamiento> findAll() {
        return avistamientoRepository.findAll();
    }
}
