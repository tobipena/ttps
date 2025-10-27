package org.example.ttps.persistence;

import jakarta.persistence.EntityManager;
import org.example.ttps.models.Avistamiento;
import org.example.ttps.models.Desaparicion;
import org.example.ttps.models.Usuario;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AvistamientoDAOHibernateJPA extends GenericDAOHibernateJPA<Avistamiento> implements AvistamientoDAO{
    public AvistamientoDAOHibernateJPA() {
        super(Avistamiento.class);
    }

    public List<Avistamiento> getByUsuario(Usuario usuario){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Avistamiento> result = Collections.emptyList();
        try{
            result = em.createQuery("SELECT a FROM Avistamiento a WHERE a.usuario= :usuario", Avistamiento.class)
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

    public List<Avistamiento> getByDesaparicion(Desaparicion desaparicion){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Avistamiento> result = Collections.emptyList();
        try{
            result = em.createQuery("SELECT a FROM Avistamiento a WHERE a.desaparicion= :desaparicion", Avistamiento.class)
                    .setParameter("desaparicion", desaparicion)
                    .getResultList();
        } catch(Exception e){
            e.printStackTrace();
        }
        finally{
            em.close();
        }
        return result;
    }

    public List<Avistamiento> getRecientes(){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Avistamiento> result = Collections.emptyList();
        try{
            result = em.createQuery("SELECT a FROM Avistamiento a ORDER BY a.fecha DESC", Avistamiento.class)
                    .getResultList();
        } catch(Exception e){
            e.printStackTrace();
        }
        finally{
            em.close();
        }
        return result;
    }

    public List<Avistamiento> getByFechaBetween(Date desde, Date hasta) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Avistamiento> result = Collections.emptyList();
        try {
            result = em.createQuery(
                            "SELECT a FROM Avistamiento a WHERE a.fecha BETWEEN :desde AND :hasta ORDER BY a.fecha DESC",
                            Avistamiento.class)
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
    public List<Avistamiento> getByCoordenada(String coordenada) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Avistamiento> result = Collections.emptyList();
        try {
            result = em.createQuery(
                            "SELECT a FROM Avistamiento a WHERE a.coordenada = :coordenada",
                            Avistamiento.class)
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
