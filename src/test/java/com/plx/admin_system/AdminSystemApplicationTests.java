package com.plx.admin_system;

import com.plx.admin_system.mapper.AdminMapper;
import com.plx.admin_system.service.IAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class AdminSystemApplicationTests {
    @Resource
    IAdminService adminService;
    @Resource
    AdminMapper adminMapper;
    @Test
    void contextLoads() {

    }
}
