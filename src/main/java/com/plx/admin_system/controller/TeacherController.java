 package com.plx.admin_system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import com.plx.admin_system.service.ITeacherService;
import com.plx.admin_system.entity.Teacher;

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
@RequestMapping("/teacher")
public class TeacherController {
    @Resource
    private ITeacherService teacherService;

    @GetMapping
    public List<Teacher> queryAll() {
        return teacherService.list();
    }
    @PostMapping("/saveOrUpdate")
    public Boolean saveOrUpdate(@RequestBody Teacher teacher) {
        return teacherService.saveOrUpdate(teacher);
    }
    @DeleteMapping("/deleteOne/{id}")
    public Boolean deleteOne(@PathVariable Integer id) {
        return teacherService.removeById(id);
    }
    @GetMapping("/queryOne/{id}")
    public Teacher queryOne(@PathVariable Integer id) {
        return teacherService.getById(id);
    }
    @GetMapping("/pagingQuery")
    public Page<Teacher> pagingQuery(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize){
        return teacherService.page(new Page<>(pageNum, pageSize));
    }
}
