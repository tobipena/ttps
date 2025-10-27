package org.example.ttps.persistence;

import org.example.ttps.models.Desaparicion;
import org.example.ttps.models.Usuario;

import java.util.Date;
import java.util.List;

public interface DesaparicionDAO extends GenericDAO<Desaparicion>{
    public List<Desaparicion> getByUsuario(Usuario usuario);
    public List<Desaparicion> getByBarrio(String barrio);
    public List<Desaparicion> getRecientes();
    public List<Desaparicion> getByFechaBetween(Date desde, Date hasta);
    public List<Desaparicion> getByCoordenada(String coordenada);

}
