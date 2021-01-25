package com.neusoft.dao;

import com.neusoft.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    // 查询全部用户
    User getUserById(int id);

    // limit分页
    List<User> getUserByLimit(Map<String,Integer> map);

    // RowBounds分页
    List<User> getUserByRowBounds();
}
