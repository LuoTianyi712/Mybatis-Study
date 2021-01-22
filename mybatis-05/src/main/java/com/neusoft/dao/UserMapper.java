package com.neusoft.dao;

import com.neusoft.pojo.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    @Select("select * from mybatis.user")
    List<User> getUser();
}
