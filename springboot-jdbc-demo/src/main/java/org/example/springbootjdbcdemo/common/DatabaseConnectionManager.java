package org.example.springbootjdbcdemo.common;

//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    private String url;

    private String userName;

    private String password;

//    private static HikariConfig config = new HikariConfig();
//
//    private static HikariDataSource ds;

    public DatabaseConnectionManager(String url, String userName, String password) {
        this.url = url;
        this.userName = userName;
        this.password = password;
//        initHikariDataSource();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, userName, password);
//        return ds.getConnection();
    }

//    private void initHikariDataSource() {
//        config.setJdbcUrl(url);
//        config.setUsername(userName);
//        config.setPassword(password);
//        config.addDataSourceProperty("cachePrepStmts", "true");
//        config.addDataSourceProperty("prepStmtCacheSize", "250");
//        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//        ds = new HikariDataSource(config);
//    }
}
