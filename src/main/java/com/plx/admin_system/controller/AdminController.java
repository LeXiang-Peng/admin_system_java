package com.plx.admin_system.controller;

import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.Teacher;
import com.plx.admin_system.entity.dto.DeleteDto;
import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.entity.views.AdminView;
import com.plx.admin_system.entity.views.StudentView;
import com.plx.admin_system.entity.views.TeacherView;
import com.plx.admin_system.service.IAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

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
    public ResponseResult getStudentList(@RequestBody(required = false) StudentView queryParams,
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
        return adminService.newOneStudent(student) ? new ResponseResult(HttpStatus.OK.value(), "新增成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "新增失败，请联系管理人员");
    }

    @PostMapping("/student/delete")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult deleteStudents(@RequestBody DeleteDto form) {
        if (adminService.verifyIdentity(form.getPassword())) {
            return adminService.deleteStudents(form.getId()) ? new ResponseResult(HttpStatus.OK.value(), "删除成功") :
                    new ResponseResult(HttpStatus.FORBIDDEN.value(), "删除失败，请联系管理人员");
        }
        return new ResponseResult(HttpStatus.FORBIDDEN.value(), "密码错误，请重新输入");
    }

    @PostMapping("/student/update/{id}")
    @PreAuthorize("hasAuthority('admin')")
    //TODO 编辑功能
    public ResponseResult updateOneStudent(@PathVariable Integer id, @RequestBody Student student) {
        return adminService.updateOneStudent(id, student) ? new ResponseResult(HttpStatus.OK.value(), "更新成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "更新失败，请联系管理人员");
    }

    @GetMapping("/student/resetPassword/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult resetStudentPassword(@PathVariable Integer id) {
        return adminService.resetStudentPassword(id) ? new ResponseResult(HttpStatus.OK.value(), "重置成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "重置失败，请联系管理人员");
    }

    @GetMapping("/student/export")
    @PreAuthorize("hasAuthority('admin+')")
    public void exportAllStudents(HttpServletResponse response) {
        adminService.exportAllStudents(response);
    }

    @GetMapping("/student/export/empty")
    @PreAuthorize("hasAuthority('admin')")
    public void exportEmptyStudentExcel(HttpServletResponse response) {
        adminService.exportEmptyStudentExcel(response);
    }

    @PostMapping("/student/import")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult importStudents(MultipartFile file) {
        return adminService.importStudents(file);
    }

    @PostMapping("/teacher/list/{pageSize}/{pageNum}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getTeacherList(@RequestBody(required = false) TeacherView queryParams,
                                         @PathVariable Integer pageSize, @PathVariable Integer pageNum) {
        return new ResponseResult(HttpStatus.OK.value(), "查询成功",
                adminService.getTeacherList(queryParams, pageSize, (pageNum - 1) * pageSize));
    }

    @GetMapping("/teacher/department")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getDepartmentList() {
        return new ResponseResult(HttpStatus.OK.value(), "获取成功", adminService.getDepartmentList());
    }

    @GetMapping("/teacher/resetPassword/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult resetTeacherPassword(@PathVariable Integer id) {
        return adminService.resetTeacherPassword(id) ? new ResponseResult(HttpStatus.OK.value(), "重置成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "重置失败，请联系管理人员");
    }

    @PostMapping("/teacher/save")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult newOneTeacher(@RequestBody Teacher teacher) {
        return adminService.newOneTeacher(teacher) ? new ResponseResult(HttpStatus.OK.value(), "新增成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "新增失败，请联系管理人员");
    }

    @GetMapping("/teacher/export")
    @PreAuthorize("hasAuthority('admin+')")
    public void exportAllTeachers(HttpServletResponse response) {
        adminService.exportAllTeachers(response);
    }

    @PostMapping("/teacher/delete")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult deleteTeachers(@RequestBody DeleteDto form) {
        if (adminService.verifyIdentity(form.getPassword())) {
            return adminService.deleteTeachers(form.getId()) ? new ResponseResult(HttpStatus.OK.value(), "删除成功") :
                    new ResponseResult(HttpStatus.FORBIDDEN.value(), "删除失败，请联系管理人员");
        }
        return new ResponseResult(HttpStatus.FORBIDDEN.value(), "密码错误，请重新输入");
    }

    @GetMapping("/teacher/export/empty")
    @PreAuthorize("hasAuthority('admin')")
    public void exportEmptyTeacherExcel(HttpServletResponse response) {
        adminService.exportEmptyTeacherExcel(response);
    }

    @PostMapping("/teacher/import")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult importTeachers(MultipartFile file) {
        return adminService.importTeachers(file);
    }

    @GetMapping("/teacher/grant/{id}")
    @PreAuthorize("hasAuthority('admin+')")
    public ResponseResult grant(@PathVariable Integer id) {
        return adminService.grant(id) ? new ResponseResult(HttpStatus.OK.value(), "授权成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "授权失败");
    }
    @PostMapping("/list/{pageSize}/{pageNum}")
    @PreAuthorize("hasAuthority('admin+')")
    public ResponseResult getAdminList(@RequestBody(required = false) AdminView queryParams,
                                         @PathVariable Integer pageSize, @PathVariable Integer pageNum) {
        return new ResponseResult(HttpStatus.OK.value(), "查询成功",
                adminService.getAdminList(queryParams, pageSize, (pageNum - 1) * pageSize));
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('admin+')")
    public ResponseResult newOneAdmin(@RequestBody Admin admin) {
        return adminService.newOneAdmin(admin) ? new ResponseResult(HttpStatus.OK.value(), "新增成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "新增失败，请联系管理人员");
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('admin+')")
    public ResponseResult deleteAdmins(@RequestBody DeleteDto form) {
        if (adminService.verifyIdentity(form.getPassword())) {
            return adminService.deleteAdmins(form.getId()) ? new ResponseResult(HttpStatus.OK.value(), "删除成功") :
                    new ResponseResult(HttpStatus.FORBIDDEN.value(), "删除失败，请联系管理人员");
        }
        return new ResponseResult(HttpStatus.FORBIDDEN.value(), "密码错误，请重新输入");
    }
    @GetMapping("/resetPassword/{id}")
    @PreAuthorize("hasAuthority('admin+')")
    public ResponseResult resetAdminPassword(@PathVariable Integer id) {
        return adminService.resetAdminPassword(id) ? new ResponseResult(HttpStatus.OK.value(), "重置成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "重置失败，请联系管理人员");
    }
}
