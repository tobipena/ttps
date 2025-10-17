package org.example.ttps.persistence;

import java.util.List;

public interface GenericDAO<T>{
    public void delete(T entity);
    public void delete(Long id);
    public T get(Long id);
    public List<T> getAll(String columnOrder);
    public T persist(T entity);
    public T update(T entity);
}
