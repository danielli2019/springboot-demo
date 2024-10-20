package org.example.springbootjdbcdemo.service;

import org.example.springbootjdbcdemo.dao.BookDao;
import org.example.springbootjdbcdemo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    private BookDao bookDao;

    public Book getBook(String id) {
        return bookDao.findById(id);
    }
}
