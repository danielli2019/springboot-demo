//package org.example.springbootjdbcdemo.common;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//
///**
// * Apache License 2.0
// */
//public class HikariCPDataSource {
//    private static HikariConfig config = new HikariConfig();
//    private static HikariDataSource ds;
//
//    public String getJdbcUrl() {
//        return jdbcUrl;
//    }
//
//    @Value("${spring.datasource.url}")
//    private String jdbcUrl;
//
//    static {
//        config.setJdbcUrl("jdbc:postgresql://localhost:5432/bookstore?stringtype=unspecified");
//        config.setUsername("postgres");
//        config.setPassword("password");
//        config.addDataSourceProperty("cachePrepStmts", "true");
//        config.addDataSourceProperty("prepStmtCacheSize", "250");
//        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//        ds = new HikariDataSource(config);
//    }
//
//    public static Connection getConnection() throws SQLException {
//        return ds.getConnection();
//    }
//
//    public HikariCPDataSource(){
//        System.out.println(jdbcUrl);
//    }
//
//    @PostConstruct
//    public void print() {
//        System.out.println(jdbcUrl);
//    }
//
//}
