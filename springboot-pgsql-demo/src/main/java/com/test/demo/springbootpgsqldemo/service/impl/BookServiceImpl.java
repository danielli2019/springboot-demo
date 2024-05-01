package com.test.demo.springbootpgsqldemo.service.impl;

import com.test.demo.springbootpgsqldemo.dao.BookMapper;
import com.test.demo.springbootpgsqldemo.entity.Book;
import com.test.demo.springbootpgsqldemo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    BookMapper bookMapper;

    @Override
    public List<Book> getBooks() {
        return bookMapper.selectAll();
    }

    @Override
    public Book getBook(String bookId) {
        return bookMapper.selectByPrimaryKey(bookId);
    }

    @Override
    public int addBook(Book book) {
        return bookMapper.insert(book);
    }

    @Override
    public int updateBook(Book book) {
        return bookMapper.updateByPrimaryKey(book);
    }

    @Override
    public int deleteBook(String bookId) {
        return bookMapper.deleteByPrimaryKey(bookId);
    }
}
