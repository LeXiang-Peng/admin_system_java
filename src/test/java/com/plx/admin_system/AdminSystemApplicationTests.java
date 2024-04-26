package com.plx.admin_system;

import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.User;
import com.plx.admin_system.mapper.AdminMapper;
import com.plx.admin_system.mapper.CommonMapper;
import com.plx.admin_system.mapper.StudentMapper;
import com.plx.admin_system.mapper.TeacherMapper;
import com.plx.admin_system.service.IAdminService;
import com.plx.admin_system.service.impl.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class AdminSystemApplicationTests {
    @Resource
    AdminMapper adminMapper;
    @Resource
    StudentMapper studentMapper;
    @Resource
    CommonMapper commonMapper;
    @Resource
    TeacherMapper teacherMapper;

    @Test
    void contextLoads() {
        commonMapper.getAvatarUrl("c6033e607518037c5da302b7107a9aba.jpg").stream().forEach(item-> System.out.println(item));
    }

}
