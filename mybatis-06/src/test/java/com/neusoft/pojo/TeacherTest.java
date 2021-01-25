package com.neusoft.pojo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    @Test
    void testToString() {
        Teacher teacher = new Teacher();
        System.out.println(teacher);
    }
}