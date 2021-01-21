package com.neusoft.dao;

import com.neusoft.pojo.User;

import java.util.List;

public interface UserMapper {

    // 查询全部用户
    List<User> getUserList();

    // 根据id查询用户
    User getUserById(int id);

    // insert语句
    int addUser(User user);

    int updateUserById(User user);

    int deleteUserById(int id);

}
