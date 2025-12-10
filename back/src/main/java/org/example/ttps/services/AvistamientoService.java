package org.example.ttps.services;

import org.example.ttps.models.Avistamiento;
import org.example.ttps.models.Desaparicion;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.AvistamientoDTO;
import org.example.ttps.repositories.AvistamientoRepository;
import org.example.ttps.repositories.DesaparicionRepository;
import org.example.ttps.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvistamientoService {

    private final AvistamientoRepository avistamientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DesaparicionRepository desaparicionRepository;

    public AvistamientoService(AvistamientoRepository avistamientoRepository,
                               UsuarioRepository usuarioRepository,
                               DesaparicionRepository desaparicionRepository) {
        this.avistamientoRepository = avistamientoRepository;
        this.usuarioRepository = usuarioRepository;
        this.desaparicionRepository = desaparicionRepository;
    }

    public Avistamiento crearAvistamiento(AvistamientoDTO avistamientoDTO) {
        Usuario usuario = usuarioRepository.findById(avistamientoDTO.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Desaparicion desaparicion = desaparicionRepository.findById(avistamientoDTO.getDesaparicionId())
                .orElseThrow(() -> new IllegalArgumentException("Desaparición no encontrada"));

        Avistamiento a = new Avistamiento();
        a.setComentario(avistamientoDTO.getComentario());
        a.setCoordenada(avistamientoDTO.getCoordenada());
//        a.setFoto(avistamientoDTO.getFoto());
        a.setFecha(avistamientoDTO.getFecha());
        a.setUsuario(usuario);
        a.setDesaparicion(desaparicion);

        usuario.agregarAvistamiento(a);
        desaparicion.agregarAvistamiento(a);
        return avistamientoRepository.save(a);
    }

    public List<Avistamiento> listarAvistamientos() {
        return avistamientoRepository.findAll();
    }

}
