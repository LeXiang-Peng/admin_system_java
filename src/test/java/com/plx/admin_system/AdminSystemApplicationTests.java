package com.plx.admin_system;

import com.plx.admin_system.mapper.CommonMapper;
import com.plx.admin_system.service.CommonService;
import com.plx.admin_system.service.impl.CommonServiceImpl;
import com.plx.admin_system.utils.CommonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

@SpringBootTest
class AdminSystemApplicationTests {
    @Resource
    CommonService commonService;

    @Test
    void contextLoads() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3MjEwMTY4YTJmY2Q0MWFhODQwODNlZWUzNTZlZmVlNSIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTcxMDA1NzU5NiwiZXhwIjoxNzEwMDYxMTk2fQ.B8UYzZJO5gQDDvY50njEgHcWQewr75OQdeE1ay6mIjg";
        System.out.println(commonService.getMenu(token));
    }

}
