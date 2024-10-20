package org.example.springbootjdbcdemo.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        String key = BusinessContextHolder.getBusinessKey();
        System.out.println("### key: " + key);
        return key;
    }
}
