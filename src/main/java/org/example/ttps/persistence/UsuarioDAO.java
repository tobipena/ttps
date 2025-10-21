package org.example.ttps.persistence;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.enums.Estado;
import org.example.ttps.models.enums.Rol;

import java.util.List;

public interface UsuarioDAO extends GenericDAO<Usuario>{
        public Usuario getByEmail(String email);
        public List<Usuario> getByRol(Rol rol);
        public Usuario getByEmailAndPassword(String email, String password);
        public List<Usuario> getUsuariosOrdenadosPorRanking();
        public List<Usuario> getByBarrio(String barrio);
        public List<Usuario> getByCiudad(String ciudad);
        public List<Usuario> getByEstado(String estado);



}
