package org.example.ttps.repositories;

import org.example.ttps.models.Mascota;
import org.example.ttps.models.enums.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota,Long> {
    List<Mascota> findByPublicadorId(Long publicadorId);
    List<Mascota> findByEstado(Estado estado);

    List<Mascota> findByEstadoOrEstado(Estado estado, Estado estado1);
}
