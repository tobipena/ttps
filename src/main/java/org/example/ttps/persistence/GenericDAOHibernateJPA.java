package org.example.ttps.persistence;

import org.example.ttps.models.Usuario;

import jakarta.persistence.*;
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
        EntityManager em = EMF.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(entity);
        tx.commit();
        return entity;
    }

    @Override
    public T update(T entity) {
        EntityManager em = EMF.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.merge(entity);
        tx.commit();
        return entity;
    }
    @Override
    public void delete(T entity) {
        EntityManager em = EMF.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T merged = em.merge(entity);
            em.remove(merged);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar la entidad", e);
        }
    }
    public List<T> getAll(String columnOrder) {
        EntityManager em = EMF.getEMF().createEntityManager();
        return em.createQuery("SELECT e FROM " +
                                getPersistentClass().getSimpleName() +
                                " e order by e." + columnOrder)
                .getResultList();
    }

    public void deleteAll(){
        EntityManager em = EMF.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
            em.createQuery("DELETE FROM " + getPersistentClass().getSimpleName()).executeUpdate();
            em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar todas las entidades", e);
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = EMF.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T entity = em.find(persistentClass, id);
            if (entity != null) {
                em.remove(entity);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar la entidad por ID", e);
        }
    }

    public T get(Long id){
        EntityManager em = EMF.getEMF().createEntityManager();
        return em.find(persistentClass, id);
    }
}
