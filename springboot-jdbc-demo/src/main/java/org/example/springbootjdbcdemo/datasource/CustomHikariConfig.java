package org.example.springbootjdbcdemo.datasource;

import com.zaxxer.hikari.HikariConfig;

import java.util.Properties;

public class CustomHikariConfig extends HikariConfig {
    public CustomHikariConfig(Properties properties) {
        super(properties);
    }
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
