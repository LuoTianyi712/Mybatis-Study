package com.neusoft.dao;

import com.neusoft.pojo.User;
import com.neusoft.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserMapperTest {

    @Test
    public void getAllUser() {

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

    @Test
    public void getUserById() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        User user = mapper.getUserById(6);
        System.out.println(user);

        sqlSession.close();
    }

    //增删改需要提交事务
    @Test
    public void addUser() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        int res = mapper.addUser(new User(7, "Ore", "789"));
        if (res > 0)
        {
            System.out.println("insert success!");
        }
        sqlSession.commit();
        sqlSession.close();
    }

    // 修改用户
    @Test
    public void updateUserById() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        int res = mapper.updateUserById(new User(6, "OREA", "987"));
        if (res > 0) {
            System.out.println("update success!");
        }

        sqlSession.commit();
        sqlSession.close();
    }

    // 删除用户
    @Test
    public void deleteUserById() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int res = mapper.deleteUserById(7);
        if (res > 0){
            System.out.println("delete success!");
        }

        sqlSession.commit();
        sqlSession.close();

    }

    // map查询
    @Test
    public void addUser2() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        Map<String, Object> map = new HashMap<>();
        map.put("userid",7);
        map.put("username","fff");
        map.put("userpwd","2333");

        int res = mapper.addUser2(map);
        if (res > 0){
            System.out.println("insert2 success!");
        }

        sqlSession.commit();
        sqlSession.close();
    }
    @Test
    public void getUserById2() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        Map<String, Object> map = new HashMap<>();
        map.put("UserId",3);

        mapper.addUser2(map);

        sqlSession.close();
    }

    @Test
    public void getUserLike() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        List<User> userList = mapper.getUserLike("%J%");

        for (User user : userList) {
            System.out.println(user);
        }

        sqlSession.close();
    }

}
