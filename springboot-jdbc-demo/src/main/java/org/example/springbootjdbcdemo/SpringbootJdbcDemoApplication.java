package org.example.springbootjdbcdemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.springbootjdbcdemo.common.DatabaseConnectionManager;
import org.example.springbootjdbcdemo.dao.BookDao;
import org.example.springbootjdbcdemo.dao.UserRepository;
import org.example.springbootjdbcdemo.dao.BookRepository;
import org.example.springbootjdbcdemo.entity.Book;
import org.example.springbootjdbcdemo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
public class SpringbootJdbcDemoApplication {

    @Autowired
    BookDao bookDao;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DatabaseConnectionManager databaseConnectionManager;

    @Autowired
    DataSource dataSource;

    private ObjectMapper MAPPER = new ObjectMapper();

    public static void main(String[] args) {
        SpringApplication.run(SpringbootJdbcDemoApplication.class, args);
    }

    public void Test() throws SQLException, JsonProcessingException {
        System.out.println("test jdbc()");

        Connection connection = null;
        try {
            connection = dataSource.getConnection();
//            connection = databaseConnectionManager.getConnection();
            connection.setAutoCommit(false);
            userRepository.setConnection(connection);
            bookRepository.setConnection(connection);

            User user1 = new User();
            int id = userRepository.getMaxId() + 1;
            System.out.println("id: " + id);
            user1.setUserId(id);
            user1.setUserName("daniel" + id);
            userRepository.save(user1);

            // Add book
            Book book1 = new Book();
            UUID uuid = UUID.randomUUID();
            String bookId = uuid.toString().substring(0, 8);
            System.out.println("bookId: " + bookId);
            book1.setBookId(bookId);
            book1.setBookName("test" + bookId);
            Map<String, Object> map = new HashMap<>();
            map.put("bookId", "11");
            map.put("bookName", "test");
            book1.setOrigin(MAPPER.writeValueAsString(map));
            bookRepository.save(book1);

            book1.setCreateBy("daniel");
            bookRepository.update(book1);

            connection.commit();

            // Query book
//            Book book = bookDao.selectByPrimaryKey(bookId);
//            Book book = bookRepository.findById(bookId);
//            System.out.println(book.getCreateBy());
//
//            List<Book> books = bookRepository.findAll();
//            System.out.println(books.size());

            // Delete book
//            bookDao.deleteByPrimaryKey(bookId);
//            bookRepository.delete(book1);

//        }
//        catch (SQLException ex) {
//            ex.printStackTrace();
//        }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("#### data is rollback");
            connection.rollback();
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

    }
}
