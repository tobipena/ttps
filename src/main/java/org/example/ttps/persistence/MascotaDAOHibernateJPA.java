package org.example.ttps.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.ttps.models.Mascota;

import java.util.Collections;
import java.util.List;

public class MascotaDAOHibernateJPA extends GenericDAOHibernateJPA<Mascota> implements MascotaDAO {

    public MascotaDAOHibernateJPA() {
        super(Mascota.class);
    }

    public List<Mascota> getByEstado(String estado){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Mascota> mascotas = Collections.emptyList();
        try{
            mascotas = em.createQuery("SELECT m FROM Mascota m WHERE m.estado = :estado",Mascota.class)
                    .setParameter("estado", estado)
                    .getResultList();
        } catch(Exception ex){
            ex.printStackTrace();
        } finally{
            em.close();
        }
        return mascotas;
    }

    public List<Mascota> getByUsuario(Long idUsuario){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Mascota> mascotas = Collections.emptyList();
        try{
            mascotas = em.createQuery("SELECT m FROM Mascota m WHERE m.dueño.id = :idUsuario",Mascota.class)
                    .setParameter("idUsuario", idUsuario)
                    .getResultList();
        } catch(Exception ex){
            ex.printStackTrace();
        } finally{
            em.close();
        }
        return mascotas;
    }

    public List<Mascota> getByBarrio(String barrio){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Mascota> mascotas = Collections.emptyList();
        try{
            mascotas = em.createQuery("SELECT m FROM Mascota m WHERE m.dueño.barrio = :barrio",Mascota.class)
                    .setParameter("barrio", barrio)
                    .getResultList();
        } catch(Exception ex){
            ex.printStackTrace();
        } finally{
            em.close();
        }
        return mascotas;
    }

    public List<Mascota> getByCoordenadas(String coordenadas){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Mascota> mascotas = Collections.emptyList();
        try{
            mascotas = em.createQuery("SELECT m FROM Mascota m WHERE m.publicacion.coordenada = :coordenadas",Mascota.class)
                    .setParameter("coordenadas", coordenadas)
                    .getResultList();
        } catch(Exception ex){
            ex.printStackTrace();
        } finally{
            em.close();
        }
        return mascotas;
    }

    public List<Mascota> buscarPorFiltros(String tamaño, String color, String especie){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Mascota> mascotas = Collections.emptyList();
        try{
            mascotas = em.createQuery("SELECT m FROM Mascota m WHERE m.tamaño = :tamaño AND m.color = :color AND m.animal = :especie",Mascota.class)
                    .setParameter("tamaño", tamaño).setParameter("color", color).setParameter("especie", especie)
                    .getResultList();
        } catch(Exception ex){
            ex.printStackTrace();
        } finally{
            em.close();
        }
        return mascotas;
    }
}
