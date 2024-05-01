package com.test.demo.springbootpgsqldemo.service;

import com.test.demo.springbootpgsqldemo.entity.Book;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BookService {
    List<Book> getBooks();

    Book getBook(String bookId);

    int addBook(Book book);

    int updateBook(Book book);

    int deleteBook(String bookId);
}
