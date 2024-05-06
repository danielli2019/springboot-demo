package org.example.springbootjdbcdemo.dao;

import org.example.springbootjdbcdemo.entity.Book;

import java.sql.SQLException;

public interface BookDao {
    int insert(Book book) throws SQLException;

    int deleteByPrimaryKey(String id) throws SQLException;

    int updateByPrimaryKey(Book book) throws SQLException;

    Book selectByPrimaryKey(String id) throws SQLException;
}
