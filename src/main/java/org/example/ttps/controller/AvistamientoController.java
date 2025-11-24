package org.example.ttps.controller;

import org.example.ttps.models.Avistamiento;
import org.example.ttps.models.Desaparicion;
import org.example.ttps.models.Mascota;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.AvistamientoDTO;
import org.example.ttps.repositories.AvistamientoRepository;
import org.example.ttps.repositories.DesaparicionRepository;
import org.example.ttps.repositories.MascotaRepository;
import org.example.ttps.repositories.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avisamientos")
public class AvistamientoController {
    private final AvistamientoRepository avistamientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DesaparicionRepository desaparicionRepository;
    public AvistamientoController(AvistamientoRepository avistamientoRepository,
                                  UsuarioRepository usuarioRepository,
                                  DesaparicionRepository desaparicionRepository) {
        this.avistamientoRepository = avistamientoRepository;
        this.usuarioRepository = usuarioRepository;
        this.desaparicionRepository = desaparicionRepository;
    }
    @PostMapping("/create")
    public Avistamiento crearAvistamiento(@RequestBody AvistamientoDTO dto) {
        Usuario u = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Desaparicion d = desaparicionRepository.findById(dto.getDesaparicionId())
                .orElseThrow(() -> new RuntimeException("Desaparición no encontrada"));
        Avistamiento a = new Avistamiento();
        a.setComentario(dto.getComentario());
        a.setCoordenada(dto.getCoordenada());
//        a.setFoto(dto.getFoto());
        a.setFecha(dto.getFecha());
        a.setUsuario(u);
        a.setDesaparicion(d);

        u.agregarAvistamiento(a);
        d.agregarAvistamiento(a);
        return avistamientoRepository.save(a);
    }
    @GetMapping()
    public List<Avistamiento> findAll() {
        return avistamientoRepository.findAll();
    }
}
