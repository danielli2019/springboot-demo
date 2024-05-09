package org.example.springbootjdbcdemo.dao.impl;

import org.example.springbootjdbcdemo.common.DatabaseConnectionManager;
import org.example.springbootjdbcdemo.dao.BookDao;
import org.example.springbootjdbcdemo.dao.DbAbstract;
import org.example.springbootjdbcdemo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Implement method 1
 */
@Repository
public class BookDaoImpl extends DbAbstract implements BookDao {
    @Autowired
    DatabaseConnectionManager databaseConnectionManager;

    @Override
    public int insert(Book book) throws SQLException {
        Connection connection = databaseConnectionManager.getConnection();
        String sql = "insert into books.book(book_id, book_name, create_date, create_by)" +
                " values(?, ?, ?, ?)";
        Object[] args = new Object[4];
        args[0] = book.getBookId();
        args[1] = book.getBookName();
        args[2] = new Date();
        args[3] = book.getCreateBy();

        int rows = insert(connection, sql, args);

        connection.close();

        return rows;
    }

    @Override
    public int deleteByPrimaryKey(String id) throws SQLException {
        Connection connection = databaseConnectionManager.getConnection();
        String sql = "update books.book set record_status = ?" +
                " where book_id = ?";
        Object[] args = new Object[2];
        args[0] = "D";
        args[1] = id;

        int rows = update(connection, sql, args);

        connection.close();

        return rows;
    }

    @Override
    public int updateByPrimaryKey(Book book) throws SQLException {
        Connection connection = databaseConnectionManager.getConnection();
        String sql = "update books.book set book_id = ?, book_name = ?, create_date = ?, create_by = ?" +
                " where book_id = ?";
        Object[] args = new Object[5];
        args[0] = book.getBookId();
        args[1] = book.getBookName();
        args[2] = new Date();
        args[3] = book.getCreateBy();
        args[4] = book.getBookId();

        int rows = update(connection, sql, args);

        connection.close();

        return rows;
    }

    public Book selectByPrimaryKey(String id) throws SQLException {
        String sql = "select * from books.book" +
                " where book_id = ?";
        Object[] args = new Object[1];
        args[0] = id;

        try (Connection connection = databaseConnectionManager.getConnection();
             PreparedStatement psmt = connection.prepareStatement(sql);
             ) {
            for (int i = 0; i < args.length; i++) {
                if(args[i] instanceof java.util.Date) {
                    args[i] = new Timestamp(((java.util.Date) args[i]).getTime());
                }
                psmt.setObject(i + 1, args[i]);
            }
            ResultSet resultSet = psmt.executeQuery();

            Book book = new Book();

            if (resultSet.next()) {
                book.setBookId(resultSet.getString("book_id"));
                book.setBookName(resultSet.getString("book_name"));
//                book.setCreateDate(resultSet.getDate("create_date"));
                book.setCreateBy(resultSet.getString("create_by"));
                book.setRecordStatus(resultSet.getString("record_status"));
            }

            return book;
        }
    }
}
