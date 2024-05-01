package com.test.demo.springbootpgsqldemo.dao;

import com.test.demo.springbootpgsqldemo.entity.Book;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper extends BaseMapper<Book, String> {
//    List<Book> findAll();
//
//    Book getByPrimaryKey(String bookId);
//
//    int insert(Book book);
//
//    int update(Book book);
//
//    int delete(String bookId);
}
