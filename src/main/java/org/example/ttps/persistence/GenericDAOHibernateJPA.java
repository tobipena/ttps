package org.example.ttps.persistence;

import org.example.ttps.models.Usuario;

import javax.persistence.*;
import java.util.List;

public class GenericDAOHibernateJPA<T> implements GenericDAO<T>{
    protected Class<T> persistentClass;
    public GenericDAOHibernateJPA(Class<T> clase) {
        this.persistentClass = clase;
    }
    public Class<T> getPersistentClass() {
        return persistentClass;
    }
    @Override
    public T persist(T entity) {
        jakarta.persistence.EntityManager em = EMF.getEMF().createEntityManager();
        jakarta.persistence.EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            em.persist(entity);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e; // escribir en un log o mostrar mensaje
        } finally {
            em.close();
        }
        return entity;
    }
    @Override
    public T update(T entity) {
        jakarta.persistence.EntityManager em = EMF.getEMF().createEntityManager();
        jakarta.persistence.EntityTransaction etx = em.getTransaction();
        etx.begin();
        T entityMerged = em.merge(entity);
        etx.commit();
        em.close();
        return entityMerged;
    }

    @Override
    public void delete(T entity) {
        jakarta.persistence.EntityTransaction tx = null;
        try(jakarta.persistence.EntityManager em = EMF.getEMF().createEntityManager()){
            tx = em.getTransaction();
            tx.begin();
            em.remove(em.merge(entity));
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e; // escribir en un log o mostrar un mensaje
        }
    }
    public List<T> getAll(String columnOrder) {
        Query consulta =
                (Query) EMF.getEMF().createEntityManager()
                        .createQuery("SELECT e FROM " +
                                getPersistentClass().getSimpleName() +
                                " e order by e." + columnOrder);
        List<T> resultado = (List<T>) consulta.getResultList();
        return resultado;
    }

    @Override
    public void delete(Long id) {

    }

    public T get(Long id){
        return null;
    }
}
