package com.plx.admin_system;

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
        int count = 0;
        for (int i = 0; i < 120; i++) {
            count++;
            int finalI = i;
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                commonService.createCaptchaImage(null, "sessionId:" + finalI);
            }).start();

        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
