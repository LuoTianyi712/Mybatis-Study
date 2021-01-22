package com.neusoft.dao;

import com.neusoft.pojo.User;
import com.neusoft.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UserMapperTest {

    @Test
    public void getAllUser() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        User user = userMapper.getUserById(1);

        System.out.println(user);

        sqlSession.close();
    }
}
