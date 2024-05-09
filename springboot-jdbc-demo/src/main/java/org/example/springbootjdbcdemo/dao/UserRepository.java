package org.example.springbootjdbcdemo.dao;

import org.example.springbootjdbcdemo.common.DatabaseConnectionManager;
import org.example.springbootjdbcdemo.entity.Book;
import org.example.springbootjdbcdemo.entity.User;
import org.example.springbootjdbcdemo.util.ClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepository extends AbstractRepository<User, Integer> {
    @Autowired
    DatabaseConnectionManager databaseConnectionManager;

    private Connection connection;

    @Override
    protected DatabaseConnectionManager databaseConnectionManager() {
        return databaseConnectionManager;
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO books.t_user1" +
                "(user_name, user_id) " +
                "VALUES (?, ?);";
    }

    @Override
    protected Object[] getInsertArgs(User entity) {
        Object[] args = new Object[2];
        args[0] = entity.getUserName();
        args[1] = entity.getUserId();
        return args;
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE books.t_user1 " +
                "SET user_name=? " +
                "WHERE user_id=?";
    }

    @Override
    protected Object[] getUpdateArgs(User entity) {
        Object[] args = new Object[2];
        args[0] = entity.getUserName();
        args[1] = entity.getUserId();
        return args;
    }

    @Override
    protected String getDeleteSql() {
        return "DELETE FROM books.t_user1 where user_id=?";
    }

    @Override
    protected Object[] getDeleteArgs(User entity) {
        Object[] args = new Object[1];
        args[0] = entity.getUserId();
        return args;
    }

    @Override
    protected String getSelectSql(boolean all) {
        return all ? "SELECT user_name, user_id\n" +
                "\tFROM books.t_user1" : "SELECT user_name, user_id\n" +
                "\tFROM books.t_user1 where user_id = ?";
    }

    @Override
    protected User mapRowToEntity(ResultSet rs) {
        User user = ClassUtil.rsToEntity(rs, User.class);
        return user;
    }

    @Override
    protected Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public int getMaxId() {
        String sql = "select max(user_id) from books.t_user1";

        try (Connection connection = databaseConnectionManager().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);) {

            System.out.println(pstmt.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
