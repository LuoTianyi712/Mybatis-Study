package com.neusoft.dao;

import com.neusoft.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    User selectUserById(@Param("id") int id);

    int updateUserById(User user);

}
