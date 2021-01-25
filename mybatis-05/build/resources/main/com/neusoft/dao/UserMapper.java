package com.neusoft.dao;

import com.neusoft.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {

    @Select("select * from mybatis.user")
    List<User> getUser();

    @Select("select * from mybatis.user where id=#{id}")
    User getUserById(@Param("id") int id);

    @Insert("insert into mybatis.user(id,name,pwd) values (#{id},#{name},#{pwd})")
    int addUser(User user);

    @Update("update mybatis.user set name=#{name},pwd=#{pwd} where id=#{id}")
    int updateUserById(User user);

    @Delete("delete from mybatis.user where id=#{id}")
    int deleteUserById(@Param("id") int id);
}
