package org.example.ttps.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.ttps.models.Avistamiento;
import org.example.ttps.models.Desaparicion;
import org.example.ttps.models.Usuario;
import org.example.ttps.models.enums.Rol;

import java.util.Collections;
import java.util.List;

public class UsuarioDAOHibernateJPA extends GenericDAOHibernateJPA<Usuario>
        implements UsuarioDAO {
    public UsuarioDAOHibernateJPA() {
        super(Usuario.class);
    }
    public Usuario getByEmail(String email){
        EntityManager em = EMF.getEMF().createEntityManager();
        Usuario usr;
        try{
            usr = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email",Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch(NoResultException ex){
            usr=null;
        } finally{
            em.close();
        }
        return usr;
    }

    public List<Usuario> getByRol(Rol rol){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Usuario> result = Collections.emptyList();;
        try {
            result = em.createQuery("SELECT u FROM Usuario u WHERE u.rol = :rol", Usuario.class)
                    .setParameter("rol", rol)
                    .getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }
        return result;
    }
    public Usuario getByEmailAndPassword(String email, String password){
        EntityManager em = EMF.getEMF().createEntityManager();
        Usuario usr;
        try {
            usr = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email AND u.password = :password", Usuario.class)
                    .setParameter("email", email).setParameter("password", password)
                    .getSingleResult();
        } catch (NoResultException ex) {
            usr= null;
        } finally {
            em.close();
        }
        return usr;
    }
    public List<Usuario> getUsuariosOrdenadosPorRanking(){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Usuario> result = Collections.emptyList();
        try{
            result = em.createQuery("SELECT u FROM Usuario u ORDER BY u.puntos DESC", Usuario.class)
                    .getResultList();
        } catch (Exception ex){
            ex.printStackTrace();
        } finally{
            em.close();
        }
        return result;
    }
    public List<Usuario> getByBarrio(String barrio){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Usuario> result = Collections.emptyList();;
        try {
            result = em.createQuery("SELECT u FROM Usuario u WHERE u.barrio = :barrio", Usuario.class)
                    .setParameter("barrio", barrio)
                    .getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }
        return result;
    }
    public List<Usuario> getByCiudad(String ciudad){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Usuario> result = Collections.emptyList();;
        try {
            result = em.createQuery("SELECT u FROM Usuario u WHERE u.ciudad = :ciudad", Usuario.class)
                    .setParameter("ciudad", ciudad)
                    .getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }
        return result;
    }
    public List<Usuario> getByEstado(String estado){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Usuario> result = Collections.emptyList();;
        try {
            result = em.createQuery("SELECT u FROM Usuario u WHERE u.estado = :estado", Usuario.class)
                    .setParameter("estado", estado)
                    .getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }
        return result;
    }

    public List<Desaparicion> getDesapariciones(Usuario usuario){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Desaparicion> result = Collections.emptyList();
        try{
            result = em.createQuery("SELECT d FROM Desaparicion d JOIN Usuario u ON d.usuario = :usuario", Desaparicion.class)
                    .setParameter("usuario", usuario)
                    .getResultList();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return result;
    }

    public List<Avistamiento> getAvistamientos(Usuario usuario){
        EntityManager em = EMF.getEMF().createEntityManager();
        List<Avistamiento> result = Collections.emptyList();
        try{
            result = em.createQuery("SELECT a FROM Avistamiento a JOIN Usuario u ON a.usuario = :usuario", Avistamiento.class)
                    .setParameter("usuario", usuario)
                    .getResultList();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return result;
    }
}
