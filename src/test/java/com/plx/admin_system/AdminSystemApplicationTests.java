package com.plx.admin_system;

import com.plx.admin_system.mapper.CommonMapper;
import com.plx.admin_system.utils.CommonUtils;
import com.plx.admin_system.utils.GeneticAlgorithm;
import com.plx.admin_system.utils.pojo.CourseTask;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class AdminSystemApplicationTests {
    @Resource
    CommonMapper commonMapper;


    @Test
    void contextLoads() {
        GeneticAlgorithm algorithm = new GeneticAlgorithm();
        List<CourseTask> res = algorithm.evolute(CommonUtils.initTasks(commonMapper.getToBeScheduledCourse()), 1);
        res.stream().forEach(item -> {
            System.out.println(item);
        });
    }
}
