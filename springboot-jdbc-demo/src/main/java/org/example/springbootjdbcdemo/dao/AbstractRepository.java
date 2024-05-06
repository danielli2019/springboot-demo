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

    public T findById(K id) {
        String sql = getSelectSql(false);

        try (Connection connection = databaseConnectionManager().getConnection();
             PreparedStatement psmt = connection.prepareStatement(sql);) {

            psmt.setObject(1, id);
            try (ResultSet rs = psmt.executeQuery()) {
                return rs.next() ? mapRowToEntity(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> findAll() {
        String sql = getSelectSql(true);

        try (Connection connection = databaseConnectionManager().getConnection();
             PreparedStatement psmt = connection.prepareStatement(sql);
             ResultSet rs = psmt.executeQuery();) {
            List<T> entities = new ArrayList<>();
            while (rs.next()) {
                entities.add(mapRowToEntity(rs));
            }
            return entities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int save(T entity) {
        String sql = getInsertSql();

        try (Connection connection = databaseConnectionManager().getConnection();
             PreparedStatement psmt = connection.prepareStatement(sql);) {
            Object[] args = getInsertArgs(entity);
            int rows = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof java.util.Date) {
                    args[i] = new Timestamp(((java.util.Date) args[i]).getTime());
                }
                psmt.setObject(i + 1, args[i]);
            }
            rows = psmt.executeUpdate();
            return rows;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int update(T entity) {
        String sql = getUpdateSql();
        try (Connection connection = databaseConnectionManager().getConnection();
             PreparedStatement psmt = connection.prepareStatement(sql);) {
            Object[] args = getUpdateArgs(entity);
            int rows = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof java.util.Date) {
                    args[i] = new Timestamp(((java.util.Date) args[i]).getTime());
                }
                psmt.setObject(i + 1, args[i]);
            }
            rows = psmt.executeUpdate();

            psmt.close();
            return rows;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int delete(T entity) {
        String sql = getDeleteSql();
        try (Connection connection = databaseConnectionManager().getConnection();
             PreparedStatement psmt = connection.prepareStatement(sql);) {
            Object[] args = getDeleteArgs(entity);
            int rows = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof java.util.Date) {
                    args[i] = new Timestamp(((java.util.Date) args[i]).getTime());
                }
                psmt.setObject(i + 1, args[i]);
            }
            rows = psmt.executeUpdate();

            psmt.close();
            return rows;
        } catch (SQLException e) {
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

    protected abstract T mapRowToEntity(ResultSet rs) throws SQLException;
}
