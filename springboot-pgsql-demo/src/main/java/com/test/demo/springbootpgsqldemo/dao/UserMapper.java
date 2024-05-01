package com.test.demo.springbootpgsqldemo.dao;

import com.test.demo.springbootpgsqldemo.entity.User;
import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(User row);

    User selectByPrimaryKey(String userId);

    List<User> selectAll();

    int updateByPrimaryKey(User row);
}