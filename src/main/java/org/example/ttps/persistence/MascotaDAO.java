package org.example.ttps.persistence;

import org.example.ttps.models.Mascota;

import java.util.List;

public interface MascotaDAO extends GenericDAO<Mascota> {
    // Filtrar por estado (perdido, recuperado, adoptado, etc.)
    List<Mascota> getByEstado(String estado);

    // Filtrar por usuario creador (para ver sus publicaciones)
    List<Mascota> getByUsuario(Long idUsuario);

    // Filtrar por ubicación/barrio (para mostrar mascotas perdidas en zona)
    List<Mascota> getByBarrio(String barrio);

    // Filtrar por coordenadas (si querés proximidad, podrías usar un rango)
    List<Mascota> getByCoordenadas(String coordenadas);

    // Búsquedas por características
    List<Mascota> buscarPorFiltros(String tamaño, String color, String especie);
}
