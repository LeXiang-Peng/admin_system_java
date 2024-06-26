package com.plx.admin_system.controller;

import com.plx.admin_system.entity.*;
import com.plx.admin_system.entity.dto.*;
import com.plx.admin_system.entity.views.*;
import com.plx.admin_system.service.CommonService;
import com.plx.admin_system.service.IAdminService;
import com.plx.admin_system.utils.CommonUtils;
import com.plx.admin_system.utils.pojo.schduledCourse.SchedulingCourse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

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
    @Resource
    private CommonService commonService;

    @PostMapping("/student/list/{pageSize}/{pageNum}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getStudentList(@RequestBody(required = false) StudentView queryParams,
                                         @PathVariable Integer pageSize, @PathVariable Integer pageNum) {
        return new ResponseResult(HttpStatus.OK.value(), "查询成功",
                adminService.getStudentList(queryParams, pageSize, (pageNum - 1) * pageSize));
    }

    @GetMapping("/options")
    @RolesAllowed({"admin", "teacher"})
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
        if (commonService.verifyIdentity(form.getPassword())) {
            return adminService.deleteStudents(form.getId()) ? new ResponseResult(HttpStatus.OK.value(), "删除成功") :
                    new ResponseResult(HttpStatus.FORBIDDEN.value(), "删除失败，请联系管理人员");
        }
        return new ResponseResult(HttpStatus.FORBIDDEN.value(), "密码错误，请重新输入");
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
        if (commonService.verifyIdentity(form.getPassword())) {
            String permission = adminService.getPermission();
            if (CommonUtils.IDENTITY_ADMIN.equals(permission)) {
                return adminService.deleteNonAdminTeachers(form.getId()) ? new ResponseResult(HttpStatus.OK.value(), "删除成功") :
                        new ResponseResult(HttpStatus.FORBIDDEN.value(), "删除失败，请联系管理人员");
            } else if (CommonUtils.IDENTITY_SUPER_ADMIN.equals(permission)) {
                return adminService.deleteNonSuperAdminTeachers(form.getId()) ? new ResponseResult(HttpStatus.OK.value(), "删除成功") :
                        new ResponseResult(HttpStatus.FORBIDDEN.value(), "删除失败，请联系管理人员");
            } else if (CommonUtils.IDENTITY_PERMANENT_ADMIN.equals(permission)) {
                return adminService.deleteTeachers(form.getId()) ? new ResponseResult(HttpStatus.OK.value(), "删除成功") :
                        new ResponseResult(HttpStatus.FORBIDDEN.value(), "删除失败，请联系管理人员");
            }
        }
        return new ResponseResult(HttpStatus.FORBIDDEN.value(), "密码错误，请重新输入");
    }

    @GetMapping("/teacher/permission")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getPermission() {
        String permission = adminService.getPermission();
        return Objects.isNull(permission) ? new ResponseResult(HttpStatus.FORBIDDEN.value(), "权限获取失败，请联系管理人员") :
                new ResponseResult(HttpStatus.OK.value(), "获取成功", permission);
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
    public ResponseResult grantTeacher(@PathVariable Integer id) {
        return adminService.grantTeacher(id) ? new ResponseResult(HttpStatus.OK.value(), "授权成功") :
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
    @PreAuthorize("hasAuthority('adminPlus')")
    public ResponseResult newOneAdmin(@RequestBody Admin admin) {
        return adminService.newOneAdmin(admin) ? new ResponseResult(HttpStatus.OK.value(), "新增成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "新增失败，请联系管理人员");
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('adminPlus')")
    public ResponseResult deleteAdmins(@RequestBody DeleteDto form) {
        if (commonService.verifyIdentity(form.getPassword())) {
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

    @GetMapping("/revoke/temporary/{id}")
    @PreAuthorize("hasAuthority('adminPlus')")
    public ResponseResult revokeAdminADay(@PathVariable Integer id) {
        return adminService.revokeAdminADay(id) ? new ResponseResult(HttpStatus.OK.value(), "禁权成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "禁权失败，请联系管理人员");
    }

    @GetMapping("/revoke/{id}")
    @PreAuthorize("hasAuthority('adminPlus')")
    public ResponseResult revokeAdmin(@PathVariable Integer id) {
        return adminService.revokeAdmin(id) ? new ResponseResult(HttpStatus.OK.value(), "禁权成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "禁权失败，请联系管理人员");
    }

    @GetMapping("/grant/{id}")
    @PreAuthorize("hasAuthority('adminPlus')")
    public ResponseResult grantAdmin(@PathVariable Integer id) {
        return adminService.grantAdmin(id) ? new ResponseResult(HttpStatus.OK.value(), "解除成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "解除失败，请联系管理人员");
    }

    @GetMapping("/grant/plus/{id}")
    @PreAuthorize("hasAuthority('adminPlus')")
    public ResponseResult privilegeEscalationADay(@PathVariable Integer id) {
        return adminService.privilegeEscalationADay(id) ? new ResponseResult(HttpStatus.OK.value(), "提权成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "提权失败，请联系管理人员");
    }

    @GetMapping("/demotion/{id}")
    @PreAuthorize("hasAuthority('adminPlus')")
    public ResponseResult demotionRights(@PathVariable Integer id) {
        return adminService.demotionRights(id) ? new ResponseResult(HttpStatus.OK.value(), "降权成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "降权失败，请联系管理人员");
    }

    @PostMapping("/teacher/granted/list/{pageSize}/{pageNum}")
    @PreAuthorize("hasAuthority('admin+')")
    public ResponseResult getGrantedTeacherList(@RequestBody(required = false) TeacherView queryParams,
                                                @PathVariable Integer pageSize, @PathVariable Integer pageNum) {
        return new ResponseResult(HttpStatus.OK.value(), "查询成功",
                adminService.getGrantedTeacherList(queryParams, pageSize, (pageNum - 1) * pageSize));
    }

    @GetMapping("/teacher/granted/revoke/{id}")
    @PreAuthorize("hasAuthority('adminPlus')")
    public ResponseResult revokeTeacher(@PathVariable Integer id) {
        return adminService.revokeTeacher(id) ? new ResponseResult(HttpStatus.OK.value(), "回收成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "回收失败，请联系管理人员");
    }

    @GetMapping("/teacher/granted/ban/temporary/{id}")
    @PreAuthorize("hasAuthority('adminPlus')")
    public ResponseResult banTeacherADay(@PathVariable Integer id) {
        return adminService.banTeacherADay(id) ? new ResponseResult(HttpStatus.OK.value(), "禁权成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "禁权失败，请联系管理人员");
    }

    @GetMapping("/teacher/granted/ban/{id}")
    @PreAuthorize("hasAuthority('adminPlus')")
    public ResponseResult banTeacher(@PathVariable Integer id) {
        return adminService.banTeacher(id) ? new ResponseResult(HttpStatus.OK.value(), "禁权成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "禁权失败，请联系管理人员");
    }

    @GetMapping("/teacher/granted/grant/plus/{id}")
    @PreAuthorize("hasAuthority('adminPlus')")
    public ResponseResult privilegeEscalationADay2(@PathVariable Integer id) {
        return adminService.privilegeEscalationADay2(id) ? new ResponseResult(HttpStatus.OK.value(), "提权成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "提权失败，请联系管理人员");
    }

    @PostMapping("/course/list/{pageSize}/{pageNum}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getPendingCourses(@RequestBody(required = false) PendingCourse queryParams,
                                            @PathVariable Integer pageSize, @PathVariable Integer pageNum) {
        return new ResponseResult(HttpStatus.OK.value(), "查询成功",
                adminService.getPendingCourses(queryParams, pageSize, (pageNum - 1) * pageSize));
    }

    @GetMapping("/course/reject/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult rejectCourseRequest(@PathVariable Integer id) {
        return adminService.rejectCourseRequest(id) ? new ResponseResult(HttpStatus.OK.value(), "否决成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "否决失败，请联系管理人员");
    }

    @PostMapping("/course/pass")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getPendingCourses(@RequestBody PendingCourse queryParams) {
        return adminService.passCourseRequest(queryParams);
    }

    @PostMapping("/course/to/be/scheduled/{pageSize}/{pageNum}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getToBeScheduledCourse(@RequestBody(required = false) ToBeScheduledCourses queryParams,
                                                 @PathVariable Integer pageSize, @PathVariable Integer pageNum) {
        return new ResponseResult(HttpStatus.OK.value(), "查询成功",
                adminService.getToBeScheduledCourses(queryParams, pageSize, (pageNum - 1) * pageSize));
    }

    @PostMapping("/course/scheduled/{pageSize}/{pageNum}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getScheduledCourse(@RequestBody(required = false) ScheduledCourseTable queryParams,
                                             @PathVariable Integer pageSize, @PathVariable Integer pageNum) {
        return new ResponseResult(HttpStatus.OK.value(), "查询成功",
                adminService.getScheduledCourse(queryParams, pageSize, (pageNum - 1) * pageSize));
    }

    @GetMapping("/course/clazz/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getClazzs(@PathVariable Integer id) {
        return new ResponseResult(HttpStatus.OK.value(), "获取成功",
                adminService.getClazzs(id));
    }

    @GetMapping("/course/generate/ga")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult generateCourseTableByGA() {
        return adminService.arrangeCourseTableByGA();
    }

    @GetMapping("/info")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getInfo() {
        return new ResponseResult(HttpStatus.OK.value(), "获取成功", adminService.getInfo());
    }

    @PostMapping("/info/modify")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult modifyInfo(@RequestBody InfoDto postForm) {
        return adminService.saveInfo(postForm) ? new ResponseResult(HttpStatus.OK.value(), "修改成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "修改失败，请联系管理员");

    }

    @GetMapping("/classroom")
    @RolesAllowed({"admin", "teacher"})
    public ResponseResult getClassroomInfo() {
        return new ResponseResult(HttpStatus.OK.value(), "获取成功", adminService.getClassroomInfo());
    }

    @PostMapping("/course/single/generate/ga")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult generateSingleCourseByGA(@RequestBody SchedulingCourse info) {
        return adminService.arrangeSingleCourseByGA(info);
    }

    @PostMapping("/student/save/info/{studentId}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult saveStudentInfo(@PathVariable Integer studentId, @RequestBody EditForm editForm) {
        return adminService.saveStudentInfo(studentId, editForm);
    }

    @PostMapping("/teacher/save/info/{teacherId}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult saveTeacherInfo(@PathVariable Integer teacherId, @RequestBody EditForm editForm) {
        return adminService.saveTeacherInfo(teacherId, editForm);
    }

    @PostMapping("/modify/password")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult modifyPassword(@RequestBody PasswordForm passwordForm) {
        if (commonService.verifyIdentity(passwordForm.getOldPassword())) {
            return adminService.modifyPassword(passwordForm) ? new ResponseResult(HttpStatus.OK.value(), "修改成功") :
                    new ResponseResult(HttpStatus.FORBIDDEN.value(), "修改失败，请联系管理员");
        }
        return new ResponseResult(HttpStatus.FORBIDDEN.value(), "密码错误，请重新输入");
    }

    @PostMapping("/course/rearrange")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult rearrange(@RequestBody ScheduledCourseTable form) {
        return adminService.rearrange(form) ? new ResponseResult(HttpStatus.OK.value(), "重新安排成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "重新安排失败");
    }

    @PostMapping("/department/{pageSize}/{pageNum}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getDepartments(@RequestBody Department queryParams, @PathVariable Integer pageSize,
                                         @PathVariable Integer pageNum) {
        return new ResponseResult(HttpStatus.OK.value(), "获取成功",
                adminService.getDepartments(queryParams, pageSize, (pageNum - 1) * pageSize));
    }

    @PostMapping("/profession/{pageSize}/{pageNum}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getProfessions(@RequestBody Profession queryParams, @PathVariable Integer pageSize,
                                         @PathVariable Integer pageNum) {
        return new ResponseResult(HttpStatus.OK.value(), "获取成功",
                adminService.getProfessions(queryParams, pageSize, (pageNum - 1) * pageSize));
    }

    @PostMapping("/clazz/{pageSize}/{pageNum}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getClazzs(@RequestBody Clazz queryParams, @PathVariable Integer pageSize,
                                    @PathVariable Integer pageNum) {
        return new ResponseResult(HttpStatus.OK.value(), "获取成功",
                adminService.getClazzs(queryParams, pageSize, (pageNum - 1) * pageSize));
    }

    @PostMapping("/department/edit")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult editDepartment(@RequestBody EditForm form) {
        return adminService.editDepartment(form);
    }

    @PostMapping("/department/delete")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult deleteDepartment(@RequestBody DeleteDto form) {
        if (commonService.verifyIdentity(form.getPassword())) {
            return adminService.deleteDepartment(form) ? new ResponseResult(HttpStatus.OK.value(), "删除成功") :
                    new ResponseResult(HttpStatus.FORBIDDEN.value(), "删除失败，请联系管理人员");
        }
        return new ResponseResult(HttpStatus.FORBIDDEN.value(), "密码错误，请重新输入");
    }

    @PostMapping("/department/new")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult newDepartment(@RequestParam String newDepartment) {
        return adminService.newDepartment(newDepartment) ? new ResponseResult(HttpStatus.OK.value(), "新增成功")
                : new ResponseResult(HttpStatus.FORBIDDEN.value(), "新增失败，请联系管理人员");
    }

    @PostMapping("/profession/new")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult newProfession(@RequestBody Profession form) {
        return adminService.newProfession(form) ? new ResponseResult(HttpStatus.OK.value(), "新增成功")
                : new ResponseResult(HttpStatus.FORBIDDEN.value(), "新增失败，请联系管理人员");
    }

    @PostMapping("/clazz/new")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult newClazz(@RequestBody Clazz form) {
        System.out.println(form);
        return adminService.newClazz(form) ? new ResponseResult(HttpStatus.OK.value(), "新增成功")
                : new ResponseResult(HttpStatus.FORBIDDEN.value(), "新增失败，请联系管理人员");
    }

    @GetMapping("/department/sample/export")
    @PreAuthorize("hasAuthority('admin')")
    public void exportSampleDepartmentExcel(HttpServletResponse response) {
        adminService.exportSampleDepartmentExcel(response);
    }

    @PostMapping("/department/import")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult importDepartments(MultipartFile file) {
        return adminService.importDepartments(file);
    }

    @PostMapping("/profession/delete")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult deleteProfession(@RequestBody DeleteDto form) {
        if (commonService.verifyIdentity(form.getPassword())) {
            return adminService.deleteProfession(form) ? new ResponseResult(HttpStatus.OK.value(), "删除成功") :
                    new ResponseResult(HttpStatus.FORBIDDEN.value(), "删除失败，请联系管理人员");
        }
        return new ResponseResult(HttpStatus.FORBIDDEN.value(), "密码错误，请重新输入");
    }

    @PostMapping("/clazz/delete")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult deleteClazz(@RequestBody DeleteDto form) {
        System.out.println(form);
        if (commonService.verifyIdentity(form.getPassword())) {
            return adminService.deleteClazz(form) ? new ResponseResult(HttpStatus.OK.value(), "删除成功") :
                    new ResponseResult(HttpStatus.FORBIDDEN.value(), "删除失败，请联系管理人员");
        }
        return new ResponseResult(HttpStatus.FORBIDDEN.value(), "密码错误，请重新输入");
    }

    @PostMapping("/clazz/edit")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult editClazz(@RequestBody EditForm form) {
        return adminService.editClazz(form);
    }

    @PostMapping("/profession/edit")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult editProfession(@RequestBody EditForm form) {
        return adminService.editProfession(form);
    }

    @GetMapping("/department/selected/list")
    public ResponseResult getAllDepartment() {
        return new ResponseResult(HttpStatus.OK.value(), "获取成功", adminService.getAllDepartments());
    }

    @GetMapping("/profession/sample/export")
    @PreAuthorize("hasAuthority('admin')")
    public void exportSampleProfessionExcel(HttpServletResponse response) {
        adminService.exportSampleProfessionExcel(response);
    }

    @GetMapping("/profession/export")
    @PreAuthorize("hasAuthority('admin')")
    public void exportProfessionExcel(HttpServletResponse response) {
        adminService.exportProfessionExcel(response);
    }

    @PostMapping("/profession/import")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult importProfessions(MultipartFile file) {
        return adminService.importProfessions(file);
    }

    @GetMapping("/profession/list")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult getAllProfessions() {
        return new ResponseResult(HttpStatus.OK.value(), "获取成功", adminService.getAllProfessions());
    }

    @GetMapping("/clazz/export")
    @PreAuthorize("hasAuthority('admin')")
    public void exportClazzExcel(HttpServletResponse response) {
        adminService.exportClazzExcel(response);
    }

    @GetMapping("/clazz/sample/export")
    @PreAuthorize("hasAuthority('admin')")
    public void exportSampleClazzExcel(HttpServletResponse response) {
        adminService.exportSampleClazzExcel(response);
    }

    @PostMapping("/clazz/import")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult importClazz(MultipartFile file) {
        return adminService.importClazz(file);
    }

    @GetMapping("/clazz/update/total")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseResult updateStudentTotal() {
        return adminService.updateStudentTotal();
    }
}
