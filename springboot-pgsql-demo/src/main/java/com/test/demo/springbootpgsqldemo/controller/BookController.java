package com.test.demo.springbootpgsqldemo.controller;

import com.test.demo.springbootpgsqldemo.dao.BookMapper;
import com.test.demo.springbootpgsqldemo.entity.Book;
import com.test.demo.springbootpgsqldemo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
public class BookController {
    @Autowired
    BookService bookService;

    @RequestMapping(value = "/api/books" , method = RequestMethod.GET)
    public List<Book> getBookList() {
        List<Book> bookList = bookService.getBooks();
        System.out.println(bookList.size());
        return bookList;
    }

    @RequestMapping(value = "/api/books/{bookId}", method = RequestMethod.GET)
    public Book getBookById(@PathVariable String bookId) {
        Book book = bookService.getBook(bookId);
        System.out.println(book.getBookName());
        return book;
    }

    @RequestMapping(value = "/api/book", method = RequestMethod.POST)
    public int addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @RequestMapping(value = "/api/book", method = RequestMethod.PUT)
    public int updateBook(@RequestBody Book book) {
        return bookService.updateBook(book);
    }

    @RequestMapping(value="/api/books/{bookId}", method = RequestMethod.DELETE)
    public int deleteBook(@PathVariable String bookId) {
        return bookService.deleteBook(bookId);
    }
}
