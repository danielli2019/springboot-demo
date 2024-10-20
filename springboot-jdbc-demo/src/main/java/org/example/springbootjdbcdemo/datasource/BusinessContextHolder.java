package org.example.springbootjdbcdemo.datasource;

public class BusinessContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setBusinessKey(String key) {
        contextHolder.set(key);
    }

    public static String getBusinessKey() {
        return contextHolder.get();
    }

    public static void clearBusinessKey() {
        contextHolder.remove();
    }
}
