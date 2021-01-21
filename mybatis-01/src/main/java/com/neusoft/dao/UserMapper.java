package com.neusoft.dao;

import com.neusoft.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    // 查询全部用户
    List<User> getUserList();

    // 根据id查询用户
    User getUserById(int id);

    // insert语句
    int addUser(User user);

    int updateUserById(User user);

    // map使用实例
    int addUser2(Map<String,Object> map);
    User getUserById2(Map<String,Object> map);

    int deleteUserById(int id);

    List<User> getUserLike(String name);
}
