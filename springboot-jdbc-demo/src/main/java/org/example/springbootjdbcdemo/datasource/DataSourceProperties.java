package org.example.springbootjdbcdemo.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

@Component
@ConfigurationProperties(prefix = "demo.datasource")
public class DataSourceProperties {
    private Properties common;

    private List<Properties> config;

    public Properties getCommon() {
        return common;
    }

    public void setCommon(Properties common) {
        this.common = common;
    }

    public List<Properties> getConfig() {
        return config;
    }

    public void setConfig(List<Properties> config) {
        this.config = config;
    }
}
