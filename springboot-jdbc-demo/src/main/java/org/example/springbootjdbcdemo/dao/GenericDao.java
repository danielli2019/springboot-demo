package org.example.springbootjdbcdemo.dao;

import java.util.List;

public interface GenericDao<T, K> {
    T findById(K id);

    List<T> findAll();

    int save(T entity);

    int update(T entity);

    int delete(T entity);
}
