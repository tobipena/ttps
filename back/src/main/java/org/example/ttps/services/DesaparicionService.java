package org.example.ttps.services;

import org.example.ttps.models.Desaparicion;
import org.example.ttps.models.Imagen;
import org.example.ttps.models.Mascota;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.dto.DesaparicionDTO;
import org.example.ttps.models.dto.DesaparicionEditDTO;
import org.example.ttps.models.dto.ImagenDTO;
import org.example.ttps.models.dto.MascotaDTO;
import org.example.ttps.repositories.DesaparicionRepository;
import org.example.ttps.repositories.MascotaRepository;
import org.example.ttps.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;
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

        // Procesar imágenes si existen
        if (mascotaDTO.getImagenes() != null && !mascotaDTO.getImagenes().isEmpty()) {
            for (ImagenDTO imagenDTO : mascotaDTO.getImagenes()) {
                try {
                    Imagen imagen = new Imagen();
                    // Decodificar Base64 a bytes
                    byte[] imageBytes = Base64.getDecoder().decode(imagenDTO.getDatos());
                    imagen.setDatos(imageBytes);
                    imagen.setTipo(imagenDTO.getTipo());
                    m.addImagen(imagen);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error al decodificar imagen Base64: " + e.getMessage());
                }
            }
        }

        return m;
    }

    private Desaparicion crearDesaparicion(DesaparicionDTO desaparicionDTO, Mascota mascota, Usuario usuario){
        Desaparicion d = new Desaparicion();
        d.setFecha(desaparicionDTO.getFecha());
        d.setComentario(desaparicionDTO.getComentario());
        d.setLatitud(desaparicionDTO.getLatitud());
        d.setLongitud(desaparicionDTO.getLongitud());
        d.setMascota(mascota);
        d.setUsuario(usuario);
        return d;
    }

    public Desaparicion editarDesaparicion(Long id, DesaparicionEditDTO desaparicionEditDTO) {
        Desaparicion desaparicion = desaparicionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Desaparición no encontrada"));

        desaparicion.setFecha(desaparicionEditDTO.getFecha());
        desaparicion.setComentario(desaparicionEditDTO.getComentario());
        desaparicion.setLatitud(desaparicionEditDTO.getLatitud());
        desaparicion.setLongitud(desaparicionEditDTO.getLongitud());

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
            dto.setLatitud(d.getLatitud());
            dto.setLongitud(d.getLongitud());
            dto.setFoto(d.getFoto());
            dto.setFecha(d.getFecha());
            
            if (d.getMascota() != null) {
                org.example.ttps.models.dto.DesaparicionResponseDTO.MascotaSimpleDTO mascotaDTO = 
                    new org.example.ttps.models.dto.DesaparicionResponseDTO.MascotaSimpleDTO();
                mascotaDTO.setId(d.getMascota().getId());
                mascotaDTO.setNombre(d.getMascota().getNombre());
                mascotaDTO.setTamano(d.getMascota().getTamano());
                mascotaDTO.setColor(d.getMascota().getColor());
                mascotaDTO.setFoto(d.getMascota().getFoto()); // Deprecated pero mantener compatibilidad
                mascotaDTO.setDescripcion(d.getMascota().getDescripcion());
                mascotaDTO.setAnimal(d.getMascota().getAnimal());
                mascotaDTO.setEstado(d.getMascota().getEstado());

                // Mapear las imágenes
                if (d.getMascota().getImagenes() != null && !d.getMascota().getImagenes().isEmpty()) {
                    List<org.example.ttps.models.dto.DesaparicionResponseDTO.ImagenSimpleDTO> imagenesDTO =
                        d.getMascota().getImagenes().stream().map(img -> {
                            org.example.ttps.models.dto.DesaparicionResponseDTO.ImagenSimpleDTO imagenDTO =
                                new org.example.ttps.models.dto.DesaparicionResponseDTO.ImagenSimpleDTO();
                            imagenDTO.setId(img.getId());
                            imagenDTO.setDatos(img.getDatos());
                            imagenDTO.setTipo(img.getTipo());
                            return imagenDTO;
                        }).collect(java.util.stream.Collectors.toList());
                    mascotaDTO.setImagenes(imagenesDTO);
                }

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
