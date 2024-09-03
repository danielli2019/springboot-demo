package org.example.springbootjdbcdemo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.example.springbootjdbcdemo.common.CustomLocalDateDeserializer;
import org.example.springbootjdbcdemo.common.CustomLocalDateSerializer;
import org.example.springbootjdbcdemo.common.CustomLocalDateTimeDeserializer;
import org.example.springbootjdbcdemo.common.CustomLocalDateTimeSerializer;
import org.example.springbootjdbcdemo.entity.Book;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClassUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.findAndRegisterModules();
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new CustomLocalDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer());
        module.addSerializer(LocalDate.class, new CustomLocalDateSerializer());
        module.addDeserializer(LocalDate.class, new CustomLocalDateDeserializer());
        objectMapper.registerModule(module);
//        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public static <T> Map<String, Object> entityToMap(T entity) {
        Map<String, Object> map = objectMapper.convertValue(entity, Map.class);

//        Map<String, Object> map = new HashMap<>();
//        try {
//            Class<?> clazz = entity.getClass();
//            while (clazz != null) {
//                Field[] fields = clazz.getDeclaredFields();
//                for (int i = 0; i < fields.length; i++) {
//                    Field field = fields[i];
//                    boolean flag = field.canAccess(entity);
//                    field.setAccessible(true);
//                    map.put(field.getName(), field.get(entity));
//                    field.setAccessible(flag);
//                }
//                clazz = clazz.getSuperclass();
//            }
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }

        return map;
    }

    public static <T> T mapToEntity(Map map, Class<?> clazz) {
        T instance = (T) objectMapper.convertValue(map, clazz);
        return instance;
    }

    public static <T> T rsToEntity(ResultSet rs, Class clazz) {

        try {
            T obj = (T) clazz.newInstance();

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            Class<?> tmpClazz = clazz;
            boolean found = false;

            for (int i = 1; i <= columnCount; i++) {
                System.out.println(rsmd.getColumnName(i) + ": " + rs.getObject(i));

                // 遍历继承的基类属性
                while (tmpClazz != null) {
                    Field[] fields = tmpClazz.getDeclaredFields();
                    for (int j = 0; j < fields.length; j++) {
                        Field field = fields[j];

                        if (field.getName().equals(ColumnUtil.underlineToCamel(rsmd.getColumnName(i)))) {
                            Class<?> type = field.getType();
//                            Object value = rs.getObject(i, type);
                            Object value = rs.getObject(i);
                            boolean flag = field.isAccessible();
                            field.setAccessible(true);
                            System.out.println(rsmd.getColumnName(i) + ": " + field.getName() + ": " + value);
                            System.out.println(rsmd.getColumnType(i) + ": " + field.getType());

                            if (rsmd.getColumnType(i) == 93 && type.getName().equals("java.time.LocalDateTime")) {
                                Timestamp timestamp = (Timestamp) rs.getObject(i);

                                // 将Timestamp转换为LocalDateTime
                                // Cannot convert the column of type TIMESTAMPTZ to requested type java.time.LocalDateTime
                                // TODO
                                if (timestamp != null) {
                                    LocalDateTime localDateTime = timestamp.toLocalDateTime();
                                    field.set(obj, localDateTime);
                                }
                            } else if (rsmd.getColumnType(i) == 93 && type.getName().equals("java.time.ZonedDateTime")) {
                                // 将timestamptz转换为ZonedDateTime
                                // java.lang.IllegalArgumentException: Can not set java.time.ZonedDateTime field org.example.springbootjdbcdemo.entity.Book.createDate to java.sql.Timestamp
                                System.out.println("TimestampTZ to ZonedDateTime");
                                Timestamp ts = rs.getObject(i, java.sql.Timestamp.class);
                                if (ts != null) {
                                    ZonedDateTime zdt = ts.toInstant().atZone(ZoneId.systemDefault());
                                    field.set(obj, zdt);
                                } else {
                                    field.set(obj, null);
                                }
                            } else if (rsmd.getColumnType(i) == 91 && type.getName().equals("java.time.LocalDate")) {
                                // 将Date转换位LocalDate
                                // java.lang.IllegalArgumentException: Can not set java.time.LocalDate field org.example.springbootjdbcdemo.entity.BookBase.effectiveDate to java.sql.Date
                                System.out.println("Date to LocalDate");
                                Date dt = rs.getObject(i, java.sql.Date.class);
                                if (dt != null) {
//                                    LocalDate ld = Instant.ofEpochMilli(dt.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                                    LocalDate ld = dt.toLocalDate();
                                    field.set(obj, ld);
                                } else {
                                    field.set(obj, null);
                                }
                            } else {
                                field.set(obj, rs.getObject(i));
                            }
                            field.setAccessible(flag);
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        found = false;
                        break;
                    }
                    tmpClazz = tmpClazz.getSuperclass();
                }

                tmpClazz = clazz;
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

    public static void main(String[] args) throws JsonProcessingException {
//        Instant now = Instant.now();
//        System.out.println(now);
//
//        ZonedDateTime utcZone = now.atZone(ZoneId.of("UTC+8"));
//        System.out.println(utcZone.toInstant());
//
//        long utcTimeStamp = utcZone.toInstant().toEpochMilli();
//        System.out.println("UTC Time Stamp: " + utcTimeStamp);
//
//        // 获取当前的ZonedDateTime对象，时区为UTC
//        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
//
//        // 从ZonedDateTime对象获取Instant对象
//        Instant instant = zonedDateTime.toInstant();
//
//        // 现在instant代表的是UTC时间线上的一个点
//        System.out.println("UTC Instant: " + instant);
//
//        LocalDateTime localDateTime = LocalDateTime.now(); // 获取本地当前时间
//        System.out.println("Local Date Time: " + localDateTime);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
        Book book1 = new Book();
        UUID uuid = UUID.randomUUID();
        String bookId = uuid.toString().substring(0, 8);
        System.out.println("bookId: " + bookId);
        book1.setBookId(bookId);
        book1.setBookName("test" + bookId);
        book1.setAuthor("Daniel");
        book1.setTotalPages(100);
        System.out.println("#### Instant.now(): " + Instant.now());
        System.out.println("#### LocalDateTime.now(): " + LocalDateTime.now());
        System.out.println("#### ZonedDateTime.now(): " + ZonedDateTime.now());
//        book1.setCreateDate(ZonedDateTime.now());
        book1.setEffectiveDate(LocalDate.now());
        book1.setScheduledTime(LocalDateTime.now());
        Map<String, Object> map1 = new HashMap<>();
        map1.put("bookId", "11");
        map1.put("bookName", "test");
        book1.setOrigin(objectMapper.writeValueAsString(map1));
        Map<String, String> map = objectMapper.convertValue(book1, Map.class);
        System.out.println(map);

        Map<String, Object> map2 = ClassUtil.entityToMap(book1);
        System.out.println(map2);

        Book book3 = ClassUtil.mapToEntity(map2, Book.class);
        System.out.println(ClassUtil.entityToMap(book3));
    }
}
