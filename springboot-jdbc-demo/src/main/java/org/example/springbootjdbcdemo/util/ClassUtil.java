package org.example.springbootjdbcdemo.util;

import net.sf.jsqlparser.schema.Column;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
                        Class<?> type = field.getType();
                        Object value = rs.getObject(i, type);
                        boolean flag = field.isAccessible();
                        field.setAccessible(true);
//                        if (type.getName().equals("java.util.sql") || type.getName().equals("java.lang.Object")) {
//                            field.set(obj, rs.getObject(i));
//                        } else {
//                            field.set(obj, value);
//                        }
                        if(type.getName().equals("java.time.LocalDateTime")) {
//                            Timestamp timestamp = Timestamp.from(
//                                    LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
//                            );

                            Timestamp timestamp = (Timestamp) rs.getObject(i);

                            // 将Timestamp转换为LocalDateTime
                            // Cannot convert the column of type TIMESTAMPTZ to requested type java.time.LocalDateTime
                            // TODO
                            LocalDateTime localDateTime = timestamp.toLocalDateTime();
                            field.set(obj, localDateTime);
                        } else {
                            field.set(obj, rs.getObject(i));
                        }
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

    public static <T> void mergeEntity(T oldEntity, T newEntity) throws IllegalAccessException {
        Class clazz = oldEntity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            System.out.println(field.getName() + "=" + field.get(oldEntity));
        }
    }

    public static void main(String[] args) {
        Instant now = Instant.now();
        System.out.println(now);

        ZonedDateTime utcZone = now.atZone(ZoneId.of("UTC+8"));
        System.out.println(utcZone.toInstant());

        long utcTimeStamp = utcZone.toInstant().toEpochMilli();
        System.out.println("UTC Time Stamp: " + utcTimeStamp);

        // 获取当前的ZonedDateTime对象，时区为UTC
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));

        // 从ZonedDateTime对象获取Instant对象
        Instant instant = zonedDateTime.toInstant();

        // 现在instant代表的是UTC时间线上的一个点
        System.out.println("UTC Instant: " + instant);

        LocalDateTime localDateTime = LocalDateTime.now(); // 获取本地当前时间
        System.out.println("Local Date Time: " + localDateTime);
    }
}
