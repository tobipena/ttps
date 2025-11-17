package org.example.ttps.persistence;

import org.example.ttps.models.Usuario;

import jakarta.persistence.*;
import java.util.List;

public class GenericDAOHibernateJPA<T> implements GenericDAO<T>{
    @PersistenceContext
    private EntityManager em;
    private EntityTransaction tx;

    protected Class<T> persistentClass;
    public GenericDAOHibernateJPA(Class<T> clase) {
        this.persistentClass = clase;
        em = EMF.getEMF().createEntityManager();
        tx = em.getTransaction();
    }
    public Class<T> getPersistentClass() {
        return persistentClass;
    }
    @Override
    public T persist(T entity) {
        tx.begin();
        em.persist(entity);
        tx.commit();
        return entity;
    }

    @Override
    public T update(T entity) {
        tx.begin();
        em.merge(entity);
        tx.commit();
        return entity;
    }
    @Override
    public void delete(T entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }
    public List<T> getAll(String columnOrder) {
        return em.createQuery("SELECT e FROM " +
                                getPersistentClass().getSimpleName() +
                                " e order by e." + columnOrder)
                .getResultList();
    }


    @Override
    public void delete(Long id) {
        T entity = this.get(id);
        if (entity != null) {
            tx.begin();
            em.remove(entity);
            tx.commit();
        }
    }

    public T get(Long id){
        return em.find(persistentClass, id);
    }
}
