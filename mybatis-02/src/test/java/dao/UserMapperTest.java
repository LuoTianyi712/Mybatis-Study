package dao;

import com.neusoft.dao.UserMapper;
import com.neusoft.pojo.User;
import com.neusoft.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

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
        sqlSession.close();
    }
}
