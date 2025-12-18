package org.example.ttps.services;

import org.example.ttps.models.Mascota;
import org.example.ttps.models.dto.MascotaDTO;
import org.example.ttps.models.enums.Estado;
import org.example.ttps.repositories.MascotaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MascotaService {

    private final MascotaRepository mascotaRepository;

    public MascotaService(MascotaRepository mascotaRepository) {
        this.mascotaRepository = mascotaRepository;
    }

    public List<Mascota> listarMascotasPorUsuario(Long idUsuario) {
        return mascotaRepository.findByPublicadorId(idUsuario);
    }

    public List<Mascota> listarMascotasPerdidas() {
        return mascotaRepository.findByEstadoOrEstado(Estado.PERDIDO_AJENO, Estado.PERDIDO_PROPIO);
    }

    private boolean noVacio(String s) {
        return s != null && !s.isEmpty();
    }
    public Mascota editarMascota(MascotaDTO mascotaDTO,Long pet_id) {
        Mascota mascota = mascotaRepository.findById(pet_id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if (noVacio(mascotaDTO.getNombre())) {mascota.setNombre(mascotaDTO.getNombre());}
        if (noVacio(mascotaDTO.getTamano())) {mascota.setTamano(mascotaDTO.getTamano());}
        if (noVacio(mascotaDTO.getAnimal())) {mascota.setAnimal(mascotaDTO.getAnimal());}
        if (mascotaDTO.getEstado() != null) {mascota.setEstado(mascotaDTO.getEstado());}
        if (noVacio(mascotaDTO.getDescripcion())) {mascota.setDescripcion(mascotaDTO.getDescripcion());}
        if (noVacio(mascotaDTO.getColor())) {mascota.setColor(mascotaDTO.getColor());}
        return mascotaRepository.save(mascota);
    }

}
