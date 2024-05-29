package org.example.springbootjdbcdemo.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean(name="dataSource")
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource HikariCPDataSource() {
        return new EncHikariDataSource();
    }
}
