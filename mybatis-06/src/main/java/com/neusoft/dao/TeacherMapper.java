package com.neusoft.dao;

import com.neusoft.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TeacherMapper {

    @Select("select * from mybatis.teacher where id = #{tid}")
    Teacher getTeacherById(@Param("tid") int id);
}
