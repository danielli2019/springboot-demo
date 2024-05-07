package org.example.springbootjdbcdemo;

import jakarta.annotation.PostConstruct;
import org.example.springbootjdbcdemo.dao.BookDao;
import org.example.springbootjdbcdemo.dao.impl.BookRepository;
import org.example.springbootjdbcdemo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class SpringbootJdbcDemoApplication {

    @Autowired
    BookDao bookDao;

    @Autowired
    BookRepository bookRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootJdbcDemoApplication.class, args);
    }

    @PostConstruct
    public void Test() throws SQLException {
        System.out.println("test jdbc()");

//        try {
        // Add book
        Book book1 = new Book();
        UUID uuid = UUID.randomUUID();
        String bookId = uuid.toString().substring(0, 8);
        System.out.println("bookId: " + bookId);
        book1.setBookId(bookId);
        book1.setBookName("test" + bookId);
        bookRepository.setIsTransaction(true);
        bookRepository.save(book1);

        book1.setCreateBy("daniel");
        bookRepository.update(book1);

        // Query book
//            Book book = bookDao.selectByPrimaryKey(bookId);
        Book book = bookRepository.findById(bookId);
        System.out.println(book.getCreateBy());

        List<Book> books = bookRepository.findAll();
        System.out.println(books.size());

        // Delete book
//            bookDao.deleteByPrimaryKey(bookId);
        bookRepository.delete(book1);

//        }
//        catch (SQLException ex) {
//            ex.printStackTrace();
//        }

    }
}
