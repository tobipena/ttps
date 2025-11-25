package org.example.ttps.services;

import org.example.ttps.models.Desaparicion;
import org.example.ttps.models.Mascota;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.DesaparicionDTO;
import org.example.ttps.models.dto.DesaparicionEditDTO;
import org.example.ttps.models.dto.MascotaDTO;
import org.springframework.stereotype.Service;

@Service
public class DesaparicionService {

    public Mascota crearMascota(MascotaDTO mascotaDTO, Usuario usuario){
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

    public Desaparicion crearDesaparicion(DesaparicionDTO desaparicionDTO, Mascota mascota, Usuario usuario){
        Desaparicion d = new Desaparicion();
        d.setFecha(desaparicionDTO.getFecha());
        d.setComentario(desaparicionDTO.getComentario());
        d.setCoordenada(desaparicionDTO.getCoordenada());
        d.setMascota(mascota);
        d.setUsuario(usuario);
        return d;
    }

    public Desaparicion editarDesaparicion(Desaparicion desaparicion, DesaparicionEditDTO desaparicionEditDTO) {
        desaparicion.setFecha(desaparicionEditDTO.getFecha());
        desaparicion.setComentario(desaparicionEditDTO.getComentario());
        desaparicion.setCoordenada(desaparicionEditDTO.getCoordenada());
        return desaparicion;
    }

}
