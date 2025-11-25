package org.example.ttps.services;

import org.example.ttps.models.Avistamiento;
import org.example.ttps.models.Desaparicion;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.AvistamientoDTO;
import org.springframework.stereotype.Service;

@Service
public class AvistamientoService {

    public Avistamiento crearAvistamiento(AvistamientoDTO avistamientoDTO, Usuario usuario, Desaparicion desaparicion) {
        Avistamiento a = new Avistamiento();
        a.setComentario(avistamientoDTO.getComentario());
        a.setCoordenada(avistamientoDTO.getCoordenada());
//        a.setFoto(avistamientoDTO.getFoto());
        a.setFecha(avistamientoDTO.getFecha());
        a.setUsuario(usuario);
        a.setDesaparicion(desaparicion);
        return a;
    }

}
