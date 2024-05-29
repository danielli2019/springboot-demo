package org.example.springbootjdbcdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.example.springbootjdbcdemo.dao.BookDao;
import org.example.springbootjdbcdemo.dao.UserDao;
import org.example.springbootjdbcdemo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class TestController {
    @Autowired
    BookDao bookRepository;

    @Autowired
    UserDao userRepository;

//    @Autowired
//    DatabaseConnectionManager databaseConnectionManager;

    private ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    DataSource dataSource;

    @RequestMapping(path = "/api/test", method = RequestMethod.GET)
    public String test() throws SQLException {
        System.out.println("test jdbc()");

        // 调用getInfo方法获取连接池状态
        HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
        System.out.println("HikariCP total connections: " + hikariDataSource.getHikariPoolMXBean().getTotalConnections());
        System.out.println("HikariCP idle connections: " + hikariDataSource.getHikariPoolMXBean().getIdleConnections());

        Connection connection = null;
        try {
            // using Hikari pool
            connection = dataSource.getConnection();
//            connection = databaseConnectionManager.getConnection();
            userRepository.setConnection(connection);
            bookRepository.setConnection(connection);

//            User user1 = new User();
//            int id = userRepository.getMaxId() + 1;
//            System.out.println("id: " + id);
//            user1.setUserId(id);
//            user1.setUserName("daniel" + id);
//            userRepository.save(user1);

            // Add book
            Book book1 = new Book();
            UUID uuid = UUID.randomUUID();
            String bookId = uuid.toString().substring(0, 8);
            System.out.println("bookId: " + bookId);
            book1.setBookId(bookId);
            book1.setBookName("test" + bookId);
            book1.setAuthor("Daniel");
            book1.setTotalPages(100);
            System.out.println("#### Instant.now(): " + Instant.now());
            System.out.println("#### LocalDateTime.now(): " + LocalDateTime.now());
            System.out.println("#### ZonedDateTime.now(): " + ZonedDateTime.now());
            book1.setCreateDate(ZonedDateTime.now());
            book1.setEffectiveDate(LocalDate.now());
            book1.setScheduledTime(LocalDateTime.now());
            Map<String, Object> map = new HashMap<>();
            map.put("bookId", "11");
            map.put("bookName", "test");
            book1.setOrigin(MAPPER.writeValueAsString(map));
            bookRepository.save(book1);

            // Query book
            Book book = bookRepository.findById(book1.getBookId());
            // com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.ZonedDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (through reference chain: org.example.springbootjdbcdemo.entity.Book["createDate"])
            // add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to pom
            MAPPER.findAndRegisterModules();
            System.out.println(MAPPER.writeValueAsString(book));

            // Update book
            book.setCreateBy("daniel");
            bookRepository.update(book);

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
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
            return "DONE";
        }
    }
}
