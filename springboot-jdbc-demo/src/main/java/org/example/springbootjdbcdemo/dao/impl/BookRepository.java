package org.example.springbootjdbcdemo.dao.impl;

import org.example.springbootjdbcdemo.common.DatabaseConnectionManager;
import org.example.springbootjdbcdemo.dao.AbstractRepository;
import org.example.springbootjdbcdemo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Implement method 2, choose this one
 */
@Repository
public class BookRepository extends AbstractRepository<Book, String> {
    @Autowired
    DatabaseConnectionManager databaseConnectionManager;

    @Override
    protected DatabaseConnectionManager databaseConnectionManager() {
        return databaseConnectionManager;
    }

    @Override
    protected String getInsertSql() {
        return "insert into books.book(book_id, book_name, create_date, create_by)" +
                " values(?, ?, ?, ?)";
    }

    @Override
    protected Object[] getInsertArgs(Book entity) {
        Object[] args = new Object[4];
        args[0] = entity.getBookId();
        args[1] = entity.getBookName();
        args[2] = new Date();
        args[3] = entity.getCreateBy();
        return args;
    }

    @Override
    protected String getUpdateSql() {
        return "update books.book set book_id = ?, book_name = ?, create_date = ?, create_by = ?" +
                " where book_id = ?";
    }

    @Override
    protected Object[] getUpdateArgs(Book entity) {
        Object[] args = new Object[5];
        args[0] = entity.getBookId();
        args[1] = entity.getBookName();
        args[2] = new Date();
        args[3] = entity.getCreateBy();
        args[4] = entity.getBookId();
        return args;
    }

    @Override
    protected String getDeleteSql() {
        return "update books.book set record_status = ?" +
                " where book_id = ?";
    }

    @Override
    protected Object[] getDeleteArgs(Book entity) {
        Object[] args = new Object[2];
        args[0] = "D";
        args[1] = entity.getBookId();
        return args;
    }

    @Override
    protected String getSelectSql(boolean all) {
        return all ? "select * from books.book" : "select * from books.book where book_id = ?";
    }

    @Override
    protected Book mapRowToEntity(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBookId(rs.getString("book_id"));
        book.setBookName(rs.getString("book_name"));
        book.setCreateDate(rs.getDate("create_date"));
        book.setCreateBy(rs.getString("create_by"));
        book.setRecordStatus(rs.getString("record_status"));
        return book;
    }
}
