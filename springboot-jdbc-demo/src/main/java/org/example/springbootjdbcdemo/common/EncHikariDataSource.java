package org.example.springbootjdbcdemo.common;

import com.zaxxer.hikari.HikariDataSource;

public class EncHikariDataSource extends HikariDataSource {
    @Override
    public String getPassword() {
        // Decrypt the password here
        System.out.println("#### Decrypt the password");
        return super.getPassword();
    }
}
