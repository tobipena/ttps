package org.example.ttps.services;

import org.example.ttps.models.Mascota;
import org.example.ttps.models.dto.MascotaDTO;
import org.springframework.stereotype.Service;

@Service
public class MascotaService {

    private boolean noVacio(String s) {
        return s != null && !s.isEmpty();
    }
    public Mascota editarMascota(MascotaDTO mascotaDTO,Mascota mascota) {
        if (noVacio(mascotaDTO.getNombre())) {mascota.setNombre(mascotaDTO.getNombre());}
        if (noVacio(mascotaDTO.getTamano())) {mascota.setTamano(mascotaDTO.getTamano());}
        if (mascotaDTO.getEstado() != null) {mascota.setEstado(mascotaDTO.getEstado());}
        if (noVacio(mascotaDTO.getDescripcion())) {mascota.setDescripcion(mascotaDTO.getDescripcion());}
        if (noVacio(mascotaDTO.getColor())) {mascota.setColor(mascotaDTO.getColor());}
        return mascota;
    }

}
