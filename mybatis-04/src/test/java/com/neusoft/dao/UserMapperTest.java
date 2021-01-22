package com.neusoft.dao;

import com.neusoft.pojo.User;
import com.neusoft.utils.MybatisUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserMapperTest {

    static Logger logger = Logger.getLogger(UserMapperTest.class);

    @Test
    public void getUserById() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        User user = userMapper.getUserById(1);
        System.out.println(user);

        sqlSession.close();
    }

    @Test
    public void testLog4j() {
        System.out.println();
        logger.info("info:进入了testLog4j");
        logger.debug("debug:进入了testLog4j");
        logger.error("error:进入了testLog4j");
    }

    @Test
    public void getUserByLimit() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        Map<String, Integer> map = new HashMap<>();
        map.put("startIndex",2);
        map.put("pageSize",3);

        List<User> userList = mapper.getUserByLimit(map);

        for (User user : userList) {
            System.out.println(user);
        }

        sqlSession.close();
    }

    @Test
    public void getUserByRowBounds() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        // rowBounds实现
        RowBounds rowBounds = new RowBounds(1,2);


        // 通过JAVA代码层面实现分页
        List<User> userList = sqlSession.selectList("com.neusoft.dao.UserMapper.getUserByRowBounds",null,rowBounds);
        for (User user : userList) {
            System.out.println(user);
        }

        mapper.getUserByRowBounds();

        sqlSession.close();
    }
}
