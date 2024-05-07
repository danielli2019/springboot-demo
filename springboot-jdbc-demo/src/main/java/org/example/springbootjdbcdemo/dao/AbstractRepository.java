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
    private boolean isTransaction = false;

    public T findById(K id) {
        String sql = getSelectSql(false);

        try (Connection connection = databaseConnectionManager().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);) {

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

        try (Connection connection = databaseConnectionManager().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
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
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = databaseConnectionManager().getConnection();
            connection.setAutoCommit(!isTransaction);

            pstmt = connection.prepareStatement(sql);
            Object[] args = getInsertArgs(entity);
            int rows = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof java.util.Date) {
                    args[i] = new Timestamp(((java.util.Date) args[i]).getTime());
                }
                pstmt.setObject(i + 1, args[i]);
            }
            System.out.println(pstmt.toString());
            rows = pstmt.executeUpdate();

            // simulate sql error
            pstmt.setObject(1, null);
            rows = pstmt.executeUpdate();

            if(isTransaction) {
                System.out.println("#### data is commit");
                connection.commit();
            }

            return rows;
        } catch (SQLException e) {
            e.printStackTrace();
            if(isTransaction) {
                System.out.println("#### data is rollback");
                connection.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public int update(T entity) {
        String sql = getUpdateSql();
        try (Connection connection = databaseConnectionManager().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);) {
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
        try (Connection connection = databaseConnectionManager().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);) {
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

    public boolean getIsTransaction() {
        return isTransaction;
    }

    public void setIsTransaction(boolean transaction) {
        isTransaction = transaction;
    }
}
