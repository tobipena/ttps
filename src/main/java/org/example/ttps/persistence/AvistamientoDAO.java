package org.example.ttps.persistence;

import org.example.ttps.models.Avistamiento;
import org.example.ttps.models.Desaparicion;
import org.example.ttps.models.Usuario;

import java.util.Date;
import java.util.List;

public interface AvistamientoDAO extends GenericDAO<Avistamiento>{
    public List<Avistamiento> getByUsuario(Usuario usuario);
    public List<Avistamiento> getByDesaparicion(Desaparicion desaparicion);
    public List<Avistamiento> getRecientes();
    public List<Avistamiento> getByFechaBetween(Date desde, Date hasta);
    public List<Avistamiento> getByCoordenada(String coordenada);
}
