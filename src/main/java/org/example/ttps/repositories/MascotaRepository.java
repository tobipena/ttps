package org.example.ttps.repositories;

import org.example.ttps.models.Mascota;
import org.example.ttps.models.enums.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota,Long> {
    List<Mascota> findByUsuarioId(Long usuarioId);
    List<Mascota> findByEstado(Estado estado);
}
