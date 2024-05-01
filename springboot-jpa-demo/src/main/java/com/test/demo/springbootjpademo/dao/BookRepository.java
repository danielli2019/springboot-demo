package com.test.demo.springbootjpademo.dao;

import com.test.demo.springbootjpademo.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {

}
