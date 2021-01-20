package com.neusoft.dao;

import com.neusoft.pojo.User;
import com.neusoft.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UserMapperTest {
    @Test
    public void test() {

        // 获得sql session对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        // 方式1：getMapper
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = userMapper.getUserList();

        for (User user : userList) {
            System.out.println(user);
        }

        // 方式2：
//        List<User> userList1 = sqlSession.selectList("com.neusoft.dao.UserMapper.getUserList");
//
//        for (User user : userList1) {
//            System.out.println(user + "***");
//        }

        sqlSession.close();
    }
}
