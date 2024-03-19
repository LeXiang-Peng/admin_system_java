package com.plx.admin_system.controller;

import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.entity.views.StudentList;
import com.plx.admin_system.service.IAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author plx
 * @since 2024-03-13
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private IAdminService adminService;

    @PostMapping("/studentList/{pageSize}/{pageNum}")
    @PreAuthorize("hasAuthority('admin')")
    ResponseResult getStudentList(@RequestBody(required = false) StudentList queryParams,
                                  @PathVariable Integer pageSize, @PathVariable Integer pageNum) {
        return new ResponseResult(HttpStatus.OK.value(), "查询成功",
                adminService.getStudentList(queryParams, pageSize, (pageNum - 1) * pageSize));
    }

    @GetMapping("/options")
    @PreAuthorize("hasAuthority('admin')")
    ResponseResult getOptions() {
        return new ResponseResult(HttpStatus.OK.value(), "获取成功", adminService.getOptions());
    }

    @PostMapping("/saveStudent")
    public Boolean saveOneStudent(@RequestBody Student student) {
        return adminService.newOneStudent(student);
    }

    @DeleteMapping("/deleteOne/{id}")
    public Boolean deleteOne(@PathVariable Integer id) {
        return adminService.removeById(id);
    }

}
