import com.neusoft.dao.StudentMapper;
import com.neusoft.dao.TeacherMapper;
import com.neusoft.pojo.Student;
import com.neusoft.pojo.Teacher;
import com.neusoft.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class ApplicationTest {

    @Test
    public void testGetAllTeacher() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);

        List<Teacher> teacher = mapper.getAllTeacher();

        System.out.println("----------------------");
        System.out.println(teacher);
        System.out.println("----------------------");

        sqlSession.close();
    }

    @Test
    public void testTeacher() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);

        Teacher teacher = mapper.getTeacherById(1);

        System.out.println("----------------------");
        System.out.println(teacher);
        System.out.println("----------------------");
        sqlSession.close();
    }

    @Test
    public void testTeacher2() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        Teacher teacher = mapper.getTeacherById2(1);

        System.out.println("----------------------");
        System.out.println(teacher);
        System.out.println("----------------------");
        sqlSession.close();
    }
}
