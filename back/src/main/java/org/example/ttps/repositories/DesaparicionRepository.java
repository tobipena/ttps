package org.example.ttps.repositories;

import org.example.ttps.models.Desaparicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesaparicionRepository extends JpaRepository<Desaparicion,Long> {
}
