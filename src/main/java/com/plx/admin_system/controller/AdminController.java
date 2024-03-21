package com.plx.admin_system.controller;

import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.dto.DeleteStudentsDto;
import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.entity.views.StudentList;
import com.plx.admin_system.service.IAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @PostMapping("/student/list/{pageSize}/{pageNum}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getStudentList(@RequestBody(required = false) StudentList queryParams,
                                         @PathVariable Integer pageSize, @PathVariable Integer pageNum) {
        return new ResponseResult(HttpStatus.OK.value(), "查询成功",
                adminService.getStudentList(queryParams, pageSize, (pageNum - 1) * pageSize));
    }

    @GetMapping("/options")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getOptions() {
        return new ResponseResult(HttpStatus.OK.value(), "获取成功", adminService.getOptions());
    }

    @PostMapping("/student/save")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult newOneStudent(@RequestBody Student student) {
        return adminService.newOneStudent(student) ? new ResponseResult(HttpStatus.OK.value(), "新增成功", null) :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "新增失败，请联系管理人员", null);
    }

    @PostMapping("/student/delete")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult deleteStudents(@RequestBody DeleteStudentsDto form) {
        if (adminService.verifyIdentity(form.getPassword())) {
            return adminService.deleteStudents(form.getId()) ? new ResponseResult(HttpStatus.OK.value(), "删除成功", null) :
                    new ResponseResult(HttpStatus.FORBIDDEN.value(), "删除失败，请联系管理人员", null);
        }
        return new ResponseResult(HttpStatus.FORBIDDEN.value(), "密码错误，请重新输入", null);
    }

    @PostMapping("/student/update")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult updateOneStudent(@RequestBody Student student) {
        return adminService.updateOneStudent(student) ? new ResponseResult(HttpStatus.OK.value(), "更新成功", null) :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "更新失败，请联系管理人员", null);
    }

    @GetMapping("/student/resetPassword/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult resetStudentPassword(@PathVariable Integer id) {
        return adminService.resetStudentPassword(id) ? new ResponseResult(HttpStatus.OK.value(), "重置成功", null) :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "重置失败，请联系管理人员", null);
    }

    @GetMapping("/student/export")
    @PreAuthorize("hasAuthority('admin')")
    public void exportAllStudents(HttpServletResponse response) {
        adminService.exportAllStudents(response);
    }
}
