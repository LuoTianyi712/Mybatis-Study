import com.neusoft.dao.TeacherMapper;
import com.neusoft.pojo.Teacher;
import com.neusoft.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

public class ApplicationTest {
    public static void main(String[] args) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);

        Teacher teacher = mapper.getTeacherById(1);
        System.out.println(teacher);

        sqlSession.close();
    }
}
