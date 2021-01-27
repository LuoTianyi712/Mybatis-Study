package com.neusoft.dao;

import com.neusoft.pojo.Blog;

import java.util.List;
import java.util.Map;

public interface BlogMapper {

    // 插入数据
    int addBlog(Blog blog);

    List<Blog> queryBlogIf(Map map);

    List<Blog> queryBlogChoose(Map map);

    // 更新博客
    int updateBlogById(Map map);
}
