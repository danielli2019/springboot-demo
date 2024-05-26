package org.example.springbootjdbcdemo.common;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * DBCP已被标记为不推荐在生产环境中使用
 * 依赖Commons-dbcp.jar, Commons-pool.jar
 * Apache License 2.0
 */
public class DBCPDataSource {
//    private static BasicDataSource ds = new BasicDataSource();
//
//    static {
//        ds.setUrl("jdbc:h2:mem:test");
//        ds.setUsername("user");
//        ds.setPassword("password");
//        ds.setMinIdle(5);
//        ds.setMaxIdle(10);
//        ds.setMaxOpenPreparedStatements(100);
//    }
//
//    public static Connection getConnection() throws SQLException {
//        return ds.getConnection();
//    }

    private DBCPDataSource(){ }
}
