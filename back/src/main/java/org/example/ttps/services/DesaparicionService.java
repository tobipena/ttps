package org.example.ttps.services;

import org.example.ttps.models.Desaparicion;
import org.example.ttps.models.Mascota;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.DesaparicionDTO;
import org.example.ttps.models.dto.DesaparicionEditDTO;
import org.example.ttps.models.dto.MascotaDTO;
import org.example.ttps.repositories.DesaparicionRepository;
import org.example.ttps.repositories.MascotaRepository;
import org.example.ttps.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesaparicionService {

    private final UsuarioRepository usuarioRepository;
    private final DesaparicionRepository desaparicionRepository;
    private final MascotaRepository mascotaRepository;

    public DesaparicionService(UsuarioRepository usuarioRepository,
                               DesaparicionRepository desaparicionRepository,
                               MascotaRepository mascotaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.desaparicionRepository = desaparicionRepository;
        this.mascotaRepository = mascotaRepository;
    }

    public Desaparicion altaDesaparicion(DesaparicionDTO desaparicionDTO) {
        Usuario u = usuarioRepository.findById(desaparicionDTO.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Mascota m = crearMascota(desaparicionDTO.getMascotaDTO(), u);
        m = mascotaRepository.save(m);

        u.agregarMascota(m);
        Desaparicion d = crearDesaparicion(desaparicionDTO, m, u);

        u.agregarDesaparicion(d);
        return desaparicionRepository.save(d);
    }

    private Mascota crearMascota(MascotaDTO mascotaDTO, Usuario usuario){
        Mascota m = new Mascota();
        m.setNombre(mascotaDTO.getNombre());
        m.setTamano(mascotaDTO.getTamano());
        m.setEstado(mascotaDTO.getEstado());
        m.setDescripcion(mascotaDTO.getDescripcion());
        m.setColor(mascotaDTO.getColor());
        m.setAnimal(mascotaDTO.getAnimal());
        m.setPublicador(usuario);
        return m;
    }

    private Desaparicion crearDesaparicion(DesaparicionDTO desaparicionDTO, Mascota mascota, Usuario usuario){
        Desaparicion d = new Desaparicion();
        d.setFecha(desaparicionDTO.getFecha());
        d.setComentario(desaparicionDTO.getComentario());
        d.setCoordenada(desaparicionDTO.getCoordenada());
        d.setMascota(mascota);
        d.setUsuario(usuario);
        return d;
    }

    public Desaparicion editarDesaparicion(Long id, DesaparicionEditDTO desaparicionEditDTO) {
        Desaparicion desaparicion = desaparicionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Desaparición no encontrada"));

        desaparicion.setFecha(desaparicionEditDTO.getFecha());
        desaparicion.setComentario(desaparicionEditDTO.getComentario());
        desaparicion.setCoordenada(desaparicionEditDTO.getCoordenada());

        return desaparicionRepository.save(desaparicion);
    }

    public Desaparicion  eliminarDesaparicion(Long id) {
        Desaparicion desaparicion = desaparicionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Desaparición no encontrada"));

        Usuario usuario = desaparicion.getUsuario();

        if (usuario != null) {
            usuario.getDesapariciones().remove(desaparicion);
        }
        desaparicionRepository.delete(desaparicion);
        return desaparicion;
    }

    public List<Desaparicion> listarDesapariciones(){
        return desaparicionRepository.findAll();
    }

    public List<org.example.ttps.models.dto.DesaparicionResponseDTO> listarDesaparicionesDTO(){
        List<Desaparicion> desapariciones = desaparicionRepository.findAll();
        return desapariciones.stream().map(d -> {
            org.example.ttps.models.dto.DesaparicionResponseDTO dto = new org.example.ttps.models.dto.DesaparicionResponseDTO();
            dto.setId(d.getId());
            dto.setComentario(d.getComentario());
            dto.setCoordenada(d.getCoordenada());
            dto.setFoto(d.getFoto());
            dto.setFecha(d.getFecha());
            
            if (d.getMascota() != null) {
                org.example.ttps.models.dto.DesaparicionResponseDTO.MascotaSimpleDTO mascotaDTO = 
                    new org.example.ttps.models.dto.DesaparicionResponseDTO.MascotaSimpleDTO();
                mascotaDTO.setId(d.getMascota().getId());
                mascotaDTO.setNombre(d.getMascota().getNombre());
                mascotaDTO.setTamano(d.getMascota().getTamano());
                mascotaDTO.setColor(d.getMascota().getColor());
                mascotaDTO.setFoto(d.getMascota().getFoto());
                mascotaDTO.setDescripcion(d.getMascota().getDescripcion());
                mascotaDTO.setAnimal(d.getMascota().getAnimal());
                mascotaDTO.setEstado(d.getMascota().getEstado());
                dto.setMascota(mascotaDTO);
            }
            
            return dto;
        }).collect(java.util.stream.Collectors.toList());
    }

    public Desaparicion obtenerDesaparicionPorId(Long id) {
        return desaparicionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Desaparición no encontrada"));
    }

}
