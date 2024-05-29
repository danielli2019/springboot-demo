package org.example.springbootjdbcdemo.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springbootjdbcdemo.entity.Book;
import org.example.springbootjdbcdemo.util.ClassUtil;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;

/**
 * Implement method 2, choose this one
 */
@Repository
public class BookDao extends AbstractDao<Book, String> {

    @Override
    protected String getInsertSql() {
        return "insert into books.book(book_id, book_name, create_date, create_by, origin, author, total_pages, effective_date, scheduled_time)" +
                " values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected Object[] getInsertArgs(Book entity) {
        String[] columns = "book_id, book_name, create_date, create_by, origin, author, total_pages, effective_date, scheduled_time".split(",");

        return buildArgs(entity, columns);
    }

    @Override
    protected String getUpdateSql() {
        return "update books.book set book_name = ?, create_date = ?, create_by = ?, origin = ?" +
                " where book_id = ?";
    }

    @Override
    protected Object[] getUpdateArgs(Book entity) {
        String[] columns = "book_name, create_date, create_by, origin, book_id".split(",");

        return buildArgs(entity, columns);
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

    @Override
    public void process(JsonNode data, String userId) throws JsonProcessingException {
        String action = data.get("action").asText();
        JsonNode oldValues = data.get("oldValues");
        JsonNode newValues = data.get("newValues");
        ObjectMapper objectMapper = new ObjectMapper();
        Book oldBook = objectMapper.treeToValue(oldValues, Book.class);
        Book newBook = objectMapper.treeToValue(oldValues, Book.class);

        switch (action) {
            case "ADD":
                save(newBook);
                break;
            case "UPDATE":
                newBook.setModifyBy(userId);
                newBook.setModifyDate(ZonedDateTime.now());
                update(newBook);
                break;
            case "DELETE":
                delete(oldBook);
                break;
            default:
                System.out.println("Not supported action " + action);
                break;
        }
    }
}
