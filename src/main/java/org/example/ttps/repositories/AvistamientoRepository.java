package org.example.ttps.repositories;

import org.example.ttps.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.example.ttps.models.Avistamiento;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvistamientoRepository extends JpaRepository<Avistamiento, Long> {
    List<Avistamiento> findByMascotaId(Long mascotaId);

}