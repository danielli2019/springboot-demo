package com.test.demo.springbootjpademo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookDao {
    public int insert(Connection connection, Object[] args) {
        try {
            String sql = "insert into books.book(book_id, book_name, create_date, create_by) " +
                    " values (?, ?, now(), ?)";
            return execute(connection, args, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int update(Connection connection, Object[] args) {
        try {
            String sql = "update books.book set book_name = ? where book_id = ?";
            return execute(connection, args, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int delete(Connection connection, Object[] args) {
        try {
            String sql = "delete from books.book where book_id = ?";
            return execute(connection, args, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private int execute(Connection connection, Object[] args, String sql) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            pstmt.setObject(i + 1, args[i]);
        }
        int result = pstmt.executeUpdate();
        pstmt.close();
        return result;
    }
}