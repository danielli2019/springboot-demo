package org.example.springbootjdbcdemo.dao;

import org.example.springbootjdbcdemo.common.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRepository<T, K> {

    private Connection connection;

    public T findById(K id) {
        String sql = getSelectSql(false);
        Connection connection = getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql);) {

            pstmt.setObject(1, id);
            System.out.println(pstmt.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? mapRowToEntity(rs) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<T> findAll() {
        String sql = getSelectSql(true);
        Connection connection = getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery();) {
            List<T> entities = new ArrayList<>();
            while (rs.next()) {
                entities.add(mapRowToEntity(rs));
            }
            return entities;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int save(T entity) throws SQLException {
        String sql = getInsertSql();

        Connection connection = getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);) {
            Object[] args = getInsertArgs(entity);
            int rows = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof java.util.Date) {
                    args[i] = new Timestamp(((java.util.Date) args[i]).getTime());
                }
                pstmt.setObject(i + 1, args[i]);
            }
            System.out.println(pstmt.toString());

            rows= pstmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    setId(rs, entity);
                }
            }

            // simulate sql error
//            pstmt.setObject(1, null);
//            rows = pstmt.executeUpdate();

            return rows;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int update(T entity) {
        String sql = getUpdateSql();
        Connection connection = getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql);) {
            Object[] args = getUpdateArgs(entity);
            int rows = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof java.util.Date) {
                    args[i] = new Timestamp(((java.util.Date) args[i]).getTime());
                }
                pstmt.setObject(i + 1, args[i]);
            }
            System.out.println(pstmt.toString());
            rows = pstmt.executeUpdate();

            return rows;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int delete(T entity) {
        String sql = getDeleteSql();
        Connection connection = getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql);) {
            Object[] args = getDeleteArgs(entity);
            int rows = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof java.util.Date) {
                    args[i] = new Timestamp(((java.util.Date) args[i]).getTime());
                }
                pstmt.setObject(i + 1, args[i]);
            }
            System.out.println(pstmt.toString());
            rows = pstmt.executeUpdate();

            return rows;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected abstract DatabaseConnectionManager databaseConnectionManager();

    protected abstract String getInsertSql();

    protected abstract Object[] getInsertArgs(T entity);

    protected abstract String getUpdateSql();

    protected abstract Object[] getUpdateArgs(T entity);

    protected abstract String getDeleteSql();

    protected abstract Object[] getDeleteArgs(T entity);

    protected abstract String getSelectSql(boolean all);

    protected abstract T mapRowToEntity(ResultSet rs);

    protected abstract void setId(ResultSet rs, T entity) throws SQLException;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
