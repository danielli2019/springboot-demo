package org.example.springbootjdbcdemo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public abstract class DbAbstract {
    public int insert(Connection connection, String sql, Object[] args) throws SQLException {
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            int rows = 0;
            for (int i = 0; i < args.length; i++) {
                if(args[i] instanceof java.util.Date) {
                    args[i] = new Timestamp(((java.util.Date) args[i]).getTime());
                }
                psmt.setObject(i + 1, args[i]);
            }
            rows = psmt.executeUpdate();
            return rows;
        }
    }

    public int update(Connection connection, String sql, Object[] args) throws SQLException {
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            int rows = 0;
            for (int i = 0; i < args.length; i++) {
                if(args[i] instanceof java.util.Date) {
                    args[i] = new Timestamp(((java.util.Date) args[i]).getTime());
                }
                psmt.setObject(i + 1, args[i]);
            }
            rows = psmt.executeUpdate();
            return rows;
        }
    }

    public int delete(Connection connection, String sql, Object[] args) throws SQLException {
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            int rows = 0;
            for (int i = 0; i < args.length; i++) {
                if(args[i] instanceof java.util.Date) {
                    args[i] = new Timestamp(((java.util.Date) args[i]).getTime());
                }
                psmt.setObject(i + 1, args[i]);
            }
            rows = psmt.executeUpdate();
            return rows;
        }
    }
}
