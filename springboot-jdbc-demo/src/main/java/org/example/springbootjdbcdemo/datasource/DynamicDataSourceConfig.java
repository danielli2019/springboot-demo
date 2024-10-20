package org.example.springbootjdbcdemo.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Configuration
public class DynamicDataSourceConfig {
    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        Properties common = dataSourceProperties.getCommon();
        List<Properties> dataSourceList = dataSourceProperties.getConfig();
        DataSource dataSource;
        CustomHikariConfig hikariConfig;
        DataSource defaultTargetDataSource = null;
        for (Properties properties : dataSourceList) {
            common.forEach((key, value) -> {
                if (!properties.containsKey(key)) {
                    properties.setProperty(key.toString(), value.toString());
                }
            });
            hikariConfig = new CustomHikariConfig(properties);
            dataSource = new HikariDataSource(hikariConfig);
            if (null == defaultTargetDataSource) {
                defaultTargetDataSource = dataSource;
            }
            targetDataSources.put(hikariConfig.getId(), dataSource);
        }

        if (null == defaultTargetDataSource) {
            throw new RuntimeException("defaultTargetDataSource is null");
        }

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(defaultTargetDataSource);
        return dynamicDataSource;
    }
}
