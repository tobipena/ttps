package org.example.ttps.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.ttps.models.Mascota;
import org.example.ttps.models.Publicacion;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.enums.TipoPublicacion;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class PublicacionDAOHibernateJPA extends GenericDAOHibernateJPA<Publicacion> implements PublicacionDAO{
    public PublicacionDAOHibernateJPA() {
        super(Publicacion.class);
    }

    public List<Publicacion> getByUsuario(Usuario usuario){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Publicacion> result = Collections.emptyList();
        try{
            result = em.createQuery("SELECT p FROM Publicacion p WHERE p.usuario= :usuario", Publicacion.class)
                    .setParameter("usuario", usuario)
                    .getResultList();
        } catch(Exception e){
            e.printStackTrace();
        }
        finally{
            em.close();
        }
        return result;
    }

    public List<Publicacion> getByTipo(TipoPublicacion tipoPublicacion){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Publicacion> result = Collections.emptyList();
        try{
            result = em.createQuery("SELECT p FROM Publicacion p WHERE p.tipoPublicacion= :tipoPublicacion", Publicacion.class)
                    .setParameter("tipoPublicacion", tipoPublicacion)
                    .getResultList();
        } catch(Exception e){
            e.printStackTrace();
        }
        finally{
            em.close();
        }
        return result;
    }

    public List<Publicacion> getByBarrio(String barrio){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Publicacion> result = Collections.emptyList();
        try{
            result = em.createQuery("SELECT p FROM Publicacion p WHERE p.usuario.barrio= :barrio", Publicacion.class)
                    .setParameter("barrio", barrio)
                    .getResultList();
        } catch(Exception e){
            e.printStackTrace();
        }
        finally{
            em.close();
        }
        return result;
    }

    public List<Publicacion> getRecientes(){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Publicacion> result = Collections.emptyList();
        try{
            result = em.createQuery("SELECT p FROM Publicacion p ORDER BY p.fecha DESC", Publicacion.class)
                    .getResultList();
        } catch(Exception e){
            e.printStackTrace();
        }
        finally{
            em.close();
        }
        return result;
    }

    public List<Publicacion> getByFechaBetween(Date desde, Date hasta) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Publicacion> result = Collections.emptyList();
        try {
            result = em.createQuery(
                            "SELECT p FROM Publicacion p WHERE p.fecha BETWEEN :desde AND :hasta ORDER BY p.fecha DESC",
                            Publicacion.class)
                    .setParameter("desde", desde)
                    .setParameter("hasta", hasta)
                    .getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }
        return result;
    }
    public List<Publicacion> getByCoordenada(String coordenada) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Publicacion> result = Collections.emptyList();
        try {
            result = em.createQuery(
                            "SELECT p FROM Publicacion p WHERE p.coordenada = :coordenada",
                            Publicacion.class)
                    .setParameter("coordenada", coordenada)
                    .getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }
        return result;
    }
}
