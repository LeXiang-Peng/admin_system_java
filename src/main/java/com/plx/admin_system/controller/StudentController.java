 package com.plx.admin_system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import com.plx.admin_system.service.IStudentService;
import com.plx.admin_system.entity.Student;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author plx
 * @since 2024-02-27
 */
@RestController
@RequestMapping("/student")
public class StudentController {
    @Resource
    private IStudentService studentService;

    @GetMapping
    public List<Student> queryAll() {
        return studentService.list();
    }
    @PostMapping("/saveOrUpdate")
    public Boolean saveOrUpdate(@RequestBody Student student) {
        return studentService.saveOrUpdate(student);
    }
    @DeleteMapping("/deleteOne/{id}")
    public Boolean deleteOne(@PathVariable Integer id) {
        return studentService.removeById(id);
    }
    @GetMapping("/queryOne/{id}")
    public Student queryOne(@PathVariable Integer id) {
        return studentService.getById(id);
    }
    @GetMapping("/pagingQuery")
    public Page<Student> pagingQuery(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize){
        return studentService.page(new Page<>(pageNum, pageSize));
    }
}
