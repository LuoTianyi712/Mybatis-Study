package com.neusoft.dao;

import com.neusoft.pojo.User;

import java.util.List;

public interface UserMapper {
    // 查询全部用户
    User getUserById(int id);
}
