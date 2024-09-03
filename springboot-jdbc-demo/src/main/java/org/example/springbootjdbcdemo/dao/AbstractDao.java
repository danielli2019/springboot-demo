package org.example.springbootjdbcdemo.dao;

import org.example.springbootjdbcdemo.util.ClassUtil;
import org.example.springbootjdbcdemo.util.ColumnUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class AbstractDao<T, K> implements GenericDao<T, K> {

    private Connection connection;

    public T findById(K id) {
        String sql = getSelectSql(false);
        Connection connection = getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql);) {

            pstmt.setObject(1, id);
            System.out.println(pstmt.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
//                return rs.next() ? mapRowToEntity(rs) : null;

                return rs.next() ? (T) mapRowToEntity(rs) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public T findById(K id, Class<T> entityType) {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) superClass;
            Type[] argTypes = paramType.getActualTypeArguments();
            if (argTypes.length > 0) {
                Class<T> classType = (Class<T>) argTypes[0];
                System.out.println(classType);
            }
        }

        String sql = getSelectSql(false);
        Connection connection = getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql);) {

            pstmt.setObject(1, id);
            System.out.println(pstmt.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
//                return rs.next() ? mapRowToEntity(rs) : null;

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

    public int save(T entity) {
        String sql = getInsertSql();

        Connection connection = getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);) {
            Object[] args = getInsertArgs(entity);
            int rows = 0;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Date) {
                    args[i] = new Timestamp(((Date) args[i]).getTime());
                }
                if (args[i] instanceof ZonedDateTime) {
                    Timestamp timestamp = Timestamp.from(((ZonedDateTime) args[i]).toInstant());
                    pstmt.setObject(i + 1, timestamp, Types.TIMESTAMP);
                    // 不能转换一个 java.time.ZonedDateTime 实例到类型 Types.TIMESTAMP_WITH_TIMEZONE
//                    pstmt.setObject(i + 1, args[i], Types.TIMESTAMP_WITH_TIMEZONE);

                } else {
                    pstmt.setObject(i + 1, args[i]);
                }
            }
            System.out.println(pstmt.toString());

            rows = pstmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    setGeneratedKey(rs, entity);
                }
            }

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
                if (args[i] instanceof Date) {
                    args[i] = new Timestamp(((Date) args[i]).getTime());
                }
                if (args[i] instanceof ZonedDateTime) {
                    Timestamp timestamp = Timestamp.from(((ZonedDateTime) args[i]).toInstant());
                    pstmt.setObject(i + 1, timestamp, Types.TIMESTAMP);
                    // 不能转换一个 java.time.ZonedDateTime 实例到类型 Types.TIMESTAMP_WITH_TIMEZONE
//                    pstmt.setObject(i + 1, args[i], Types.TIMESTAMP_WITH_TIMEZONE);

                } else {
                    pstmt.setObject(i + 1, args[i]);
                }
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
                if (args[i] instanceof Date) {
                    args[i] = new Timestamp(((Date) args[i]).getTime());
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

    protected abstract String getInsertSql();

    protected abstract Object[] getInsertArgs(T entity);

    protected abstract String getUpdateSql();

    protected abstract Object[] getUpdateArgs(T entity);

    protected abstract String getDeleteSql();

    protected abstract Object[] getDeleteArgs(T entity);

    protected abstract String getSelectSql(boolean all);

//    protected abstract T mapRowToEntity(ResultSet rs);

    protected T mapRowToEntity(ResultSet rs) {
        Class<T> entityType = null;
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) superClass;
            Type[] argTypes = paramType.getActualTypeArguments();
            if (argTypes.length > 0) {
                entityType = (Class<T>) argTypes[0];
                System.out.println("entityTypes: " + Arrays.toString(argTypes));
                System.out.println("entityType: " + entityType);
            }
        }

//        Method method = null;
//        try {
//            method = AbstractDao.class.getMethod("findById", Object.class);
//        } catch (NoSuchMethodException e) {
//            throw new RuntimeException(e);
//        }
//        Type genericReturnType = method.getGenericReturnType();
//        if (genericReturnType instanceof ParameterizedType) {
//            ParameterizedType paramType = (ParameterizedType) genericReturnType;
//            Class<T> classType = (Class<T>) paramType.getActualTypeArguments()[0];
//            System.out.println("returnType for mapRowToEntity: " + classType);
//        }

        return ClassUtil.rsToEntity(rs, entityType);
    }

    protected abstract void setGeneratedKey(ResultSet rs, T entity) throws SQLException;

    public Connection getConnection() {
        if (connection == null) {
            System.out.println("Connection is null.");
            throw new RuntimeException("Connection is null.");
        }
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    protected Object[] buildArgs(T entity, String[] columns) {
        Object[] args = new Object[columns.length];
        Map<String, Object> columnMap = ClassUtil.entityToMap(entity);
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].trim().equals("udt_cnt_increment")) {
                args[i] = (int) columnMap.get(ColumnUtil.underlineToCamel(columns[i].trim())) + 1;
            } else {
                args[i] = columnMap.get(ColumnUtil.underlineToCamel(columns[i].trim()));
            }
        }
        return args;
    }

}
