package com.plx.admin_system;

import com.plx.admin_system.mapper.StudentMapper;
import com.plx.admin_system.service.IAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class AdminSystemApplicationTests {
    @Resource
    IAdminService adminService;
    @Resource
    StudentMapper studentMapper;
    @Test
    void contextLoads() {

    }
}
