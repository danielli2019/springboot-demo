package org.example.springbootjdbcdemo.dao;

import org.example.springbootjdbcdemo.common.DatabaseConnectionManager;
import org.example.springbootjdbcdemo.dao.AbstractRepository;
import org.example.springbootjdbcdemo.entity.Book;
import org.example.springbootjdbcdemo.util.ClassUtil;
import org.example.springbootjdbcdemo.util.ColumnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

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
        return "insert into books.book(book_id, book_name, create_date, create_by, origin)" +
                " values(?, ?, ?, ?, ?)";
    }

    @Override
    protected Object[] getInsertArgs(Book entity) {
        String[] columns = "book_id, book_name, create_date, create_by, origin".split(",");
        Object[] args = new Object[columns.length];
        Map<String, Object> columnMap = ClassUtil.entityToMap(entity);
        for (int i = 0; i < columns.length; i++) {
            args[i] = columnMap.get(ColumnUtil.underlineToCamel(columns[i].trim()));
        }
        return args;
    }

    @Override
    protected String getUpdateSql() {
        return "update books.book set book_name = ?, create_date = ?, create_by = ?, origin = ?" +
                " where book_id = ?";
    }

    @Override
    protected Object[] getUpdateArgs(Book entity) {
        String[] columns = "book_name, create_date, create_by, origin, book_id".split(",");
        Object[] args = new Object[columns.length];
        Map<String, Object> columnMap = ClassUtil.entityToMap(entity);
        for (int i = 0; i < columns.length; i++) {
            args[i] = columnMap.get(ColumnUtil.underlineToCamel(columns[i].trim()));
        }
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
    protected Book mapRowToEntity(ResultSet rs) {
        Book book = ClassUtil.rsToEntity(rs, Book.class);
        return book;
    }

    @Override
    protected void setGeneratedKey(ResultSet rs, Book entity) throws SQLException {

    }


}
