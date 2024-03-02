package com.plx.admin_system;

import com.plx.admin_system.utils.CommonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdminSystemApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(CommonUtils.getRedisKey("123"));
    }

}
