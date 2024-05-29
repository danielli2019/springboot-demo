package org.example.springbootjdbcdemo;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
public class SpringbootJdbcDemoApplication {

    @Autowired
    DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootJdbcDemoApplication.class, args);
    }

    @PostConstruct
    public void init() throws SQLException {
        // Init Hikari pool
        Connection conn = dataSource.getConnection();
        conn.close();
    }
}
