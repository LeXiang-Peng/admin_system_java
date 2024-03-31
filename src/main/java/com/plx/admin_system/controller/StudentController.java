package com.plx.admin_system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.entity.views.SelectedCourse;
import com.plx.admin_system.service.IStudentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author plx
 * @since 2024-03-13
 */
@RestController
@RequestMapping("/student")
public class StudentController {
    @Resource
    private IStudentService studentService;

    @GetMapping("/course/list/{pageNum}")
    public ResponseResult getCourseList(@PathVariable Integer pageNum) {
        return new ResponseResult(HttpStatus.OK.value(), "获取成功",
                studentService.getCourseList((pageNum - 1) * 5));
    }

    @GetMapping("/course/cancel/{id}")
    public ResponseResult cancelCourse(@PathVariable Integer id) {
        return studentService.cancelCourse(id) ? new ResponseResult(HttpStatus.OK.value(),
                "取消成功", studentService.cancelCourse(id)) :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "取消失败，请联系管理人员");
    }

    @PostMapping("/course/select")
    public ResponseResult selectCourse(@RequestBody SelectedCourse course) {
        return studentService.selectCourse(course) ? new ResponseResult(HttpStatus.OK.value(),
                "选择成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "选择失败，请联系管理人员");
    }
}
