package com.test.demo.springbootpgsqldemo.dao;

import com.test.demo.springbootpgsqldemo.entity.User;

import java.util.List;

public interface BaseMapper<T, K> {
    int deleteByPrimaryKey(K id);

    int insert(T row);

    T selectByPrimaryKey(K id);

    List<T> selectAll();

    int updateByPrimaryKey(T row);

    T selectOne(K id);
}
