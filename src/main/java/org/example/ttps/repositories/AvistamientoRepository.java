package org.example.ttps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.example.ttps.models.Avistamiento;

import java.util.Optional;

@Repository
public interface AvistamientoRepository extends JpaRepository<Avistamiento, Long> {

//    @Query("select a.habilitada from Aplicacion a where a.nombreAplicacion = :nombreAplicacion")
//    Optional<Boolean> findHabilitadaByNombreAplicacion(@Param("nombreAplicacion") String nombreAplicacion);
//
//    Aplicacion findByNombreAplicacion(@Param("nombreAplicacion") String nombreAplicacion);

}