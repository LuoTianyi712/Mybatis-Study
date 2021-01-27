import com.neusoft.dao.BlogMapper;
import com.neusoft.pojo.Blog;
import com.neusoft.utils.IdUtils;
import com.neusoft.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationTest {

    @Test
    public void addInitBlog() {

        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

        Blog blog1 = addBlog(IdUtils.getId(), "Mybatis", "apache", new Date(), 9999);
        if (mapper.addBlog(blog1) > 0){
            System.out.println("博客创建成功：" + blog1.getTitle());
        }

        Blog blog2 = addBlog(IdUtils.getId(), "JAVA", "Oracle", new Date(), 9999);
        if (mapper.addBlog(blog2) > 0){
            System.out.println("博客创建成功：" + blog2.getTitle());
        }

        Blog blog3 = addBlog(IdUtils.getId(), "Spring", "Rod", new Date(), 8888);
        if (mapper.addBlog(blog3) > 0){
            System.out.println("博客创建成功：" + blog3.getTitle());
        }

        Blog blog4 = addBlog(IdUtils.getId(), "微服务", "James", new Date(), 9999);
        if (mapper.addBlog(blog4) > 0){
            System.out.println("博客创建成功：" + blog4.getTitle());
        }


        sqlSession.close();
    }

    protected Blog addBlog(String id, String title, String author, Date createTime, int views){
        Blog blog = new Blog();

        blog.setId(id);
        blog.setTitle(title);
        blog.setAuthor(author);
        blog.setCreateTime(createTime);
        blog.setViews(views);

        return blog;
    }

    @Test
    public void testIf(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);


        HashMap<Object, Object> map = new HashMap<>();

        map.put("title","JAVA");
        map.put("author","Oracle");


        List<Blog> blogList = mapper.queryBlogIf(map);
        System.out.println("----------------");
        for (Blog blog : blogList) {
            System.out.println(blog);
        }
        System.out.println("----------------");

        sqlSession.close();
    }

    @Test
    public void testChoose(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);


        HashMap<Object, Object> map = new HashMap<>();

        map.put("title","JAVA");
        map.put("author","sam");


        List<Blog> blogList = mapper.queryBlogChoose(map);
        System.out.println("----------------");
        for (Blog blog : blogList) {
            System.out.println(blog);
        }
        System.out.println("----------------");

        sqlSession.close();
    }

    @Test
    public void testSet(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

        HashMap<Object, Object> map = new HashMap<>();
//        map.put("title","jvav");
        map.put("author", "ORACLE");
        map.put("id","29c484262cbc45fe9de782684d006cb0");


        if (mapper.updateBlogById(map) > 0){
            System.out.println("update success");
        }



        sqlSession.close();
    }

}
