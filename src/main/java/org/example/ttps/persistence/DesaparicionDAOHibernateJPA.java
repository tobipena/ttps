package org.example.ttps.persistence;

import jakarta.persistence.EntityManager;
import org.example.ttps.models.Desaparicion;
import org.example.ttps.models.Usuario;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DesaparicionDAOHibernateJPA extends GenericDAOHibernateJPA<Desaparicion> implements DesaparicionDAO{
    public DesaparicionDAOHibernateJPA() {
        super(Desaparicion.class);
    }

    public List<Desaparicion> getByUsuario(Usuario usuario){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Desaparicion> result = Collections.emptyList();
        try{
            result = em.createQuery("SELECT d FROM Desaparicion d WHERE d.usuario= :usuario", Desaparicion.class)
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

    public List<Desaparicion> getByBarrio(String barrio){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Desaparicion> result = Collections.emptyList();
        try{
            result = em.createQuery("SELECT d FROM Desaparicion d WHERE d.usuario.barrio= :barrio", Desaparicion.class)
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

    public List<Desaparicion> getRecientes(){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Desaparicion> result = Collections.emptyList();
        try{
            result = em.createQuery("SELECT d FROM Desaparicion d ORDER BY d.fecha DESC", Desaparicion.class)
                    .getResultList();
        } catch(Exception e){
            e.printStackTrace();
        }
        finally{
            em.close();
        }
        return result;
    }

    public List<Desaparicion> getByFechaBetween(Date desde, Date hasta) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Desaparicion> result = Collections.emptyList();
        try {
            result = em.createQuery(
                            "SELECT d FROM Desaparicion d WHERE d.fecha BETWEEN :desde AND :hasta ORDER BY d.fecha DESC",
                            Desaparicion.class)
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
    public List<Desaparicion> getByCoordenada(String coordenada) {
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Desaparicion> result = Collections.emptyList();
        try {
            result = em.createQuery(
                            "SELECT d FROM Desaparicion d WHERE d.coordenada = :coordenada",
                            Desaparicion.class)
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
