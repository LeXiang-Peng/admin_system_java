package com.plx.admin_system;

import com.plx.admin_system.mapper.AdminMapper;
import com.plx.admin_system.service.IAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class AdminSystemApplicationTests {
    @Resource
    AdminMapper adminMapper;

    @Test
    void contextLoads() {
        System.out.println(adminMapper.getAllStudents());
    }

}
