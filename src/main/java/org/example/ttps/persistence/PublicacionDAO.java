package org.example.ttps.persistence;

import org.example.ttps.models.Publicacion;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.enums.TipoPublicacion;

import java.util.Date;
import java.util.List;

public interface PublicacionDAO extends GenericDAO<Publicacion>{
    public List<Publicacion> getByUsuario(Usuario usuario);
    public List<Publicacion> getByTipo(TipoPublicacion tipoPublicacion);
    public List<Publicacion> getByBarrio(String barrio);
    public List<Publicacion> getRecientes();
    public List<Publicacion> getByFechaBetween(Date desde, Date hasta);
    public List<Publicacion> getByCoordenada(String coordenada);

}
