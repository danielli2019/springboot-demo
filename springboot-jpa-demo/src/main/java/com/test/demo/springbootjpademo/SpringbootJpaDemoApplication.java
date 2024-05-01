package com.test.demo.springbootjpademo;

import com.test.demo.springbootjpademo.dao.BookRepository;
import com.test.demo.springbootjpademo.entity.Book;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
public class SpringbootJpaDemoApplication {
	@Autowired
	BookRepository bookRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootJpaDemoApplication.class, args);
	}

	@PostConstruct
	public void test() {
		System.out.println("test()");

		// Add book
		Book book1 = new Book();
		book1.setBookId("10");
		book1.setBookName("test");
		bookRepository.save(book1);

		// Query book
		List<Book> bookList = bookRepository.findAll();
		System.out.println(bookList);

		// Delete book
		bookRepository.delete(book1);
	}


}
