package org.example.springbootjdbcdemo.util;

import net.sf.jsqlparser.schema.Column;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ClassUtil {
    public static <T> Map<String, Object> entityToMap(T entity) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = entity.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                map.put(field.getName(), field.get(entity));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return map;
    }

    public static <T> T rsToEntity(ResultSet rs, Class clazz) {

        try {
            T obj = (T) clazz.newInstance();

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            Field[] fields = clazz.getDeclaredFields();

            for (int i = 1; i <= columnCount; i++) {
                System.out.println(rsmd.getColumnName(i) + ": " + rs.getObject(i));
                for (int j = 0; j < fields.length; j++) {
                    Field field = fields[j];
                    if (field.getName().equals(ColumnUtil.underlineToCamel(rsmd.getColumnName(i)))) {
//                        Class<?> type = field.getType();
//                        Object value = rs.getObject(i, type);
                        boolean flag = field.isAccessible();
                        field.setAccessible(true);
//                        if (type.getName().equals("java.util.sql") || type.getName().equals("java.lang.Object")) {
//                            field.set(obj, rs.getObject(i));
//                        } else {
//                            field.set(obj, value);
//                        }
                        field.set(obj, rs.getObject(i));
                        field.setAccessible(flag);
                        break;
                    }
                }

            }
            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
