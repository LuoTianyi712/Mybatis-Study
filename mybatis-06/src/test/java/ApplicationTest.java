import com.neusoft.dao.StudentMapper;
import com.neusoft.dao.TeacherMapper;
import com.neusoft.pojo.Student;
import com.neusoft.pojo.Teacher;
import com.neusoft.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class ApplicationTest {
    public static void main(String[] args) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);

        Teacher teacher = mapper.getTeacherById(1);
        System.out.println(teacher);

        sqlSession.close();
    }

    @Test
    public void testStudent() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);

        List<Student> studentList = mapper.getAllStudent();

        System.out.println("----------------------");
        for (Student student : studentList) {
            System.out.println(student);
        }
        System.out.println("----------------------");
        sqlSession.close();
    }
}
