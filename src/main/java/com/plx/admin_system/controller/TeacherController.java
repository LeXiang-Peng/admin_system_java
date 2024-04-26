package com.plx.admin_system.controller;

import com.plx.admin_system.entity.ApprovalingCourse;
import com.plx.admin_system.entity.dto.ChangeCourseDto;
import com.plx.admin_system.entity.dto.InfoDto;
import com.plx.admin_system.entity.dto.PasswordForm;
import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.service.CommonService;
import com.plx.admin_system.service.ITeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
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
@RequestMapping("/teacher")
public class TeacherController {
    @Resource
    private ITeacherService teacherService;
    @Resource
    private CommonService commonService;

    @PostMapping("/course/approving/{pageSize}/{pageNum}")
    @PreAuthorize("hasAuthority('teacher')")
    public ResponseResult getCourseList(@RequestBody(required = false) ApprovalingCourse queryParams,
                                        @PathVariable Integer pageSize, @PathVariable Integer pageNum) {
        return new ResponseResult(HttpStatus.OK.value(), "查询成功",
                teacherService.getCourseList(queryParams, pageSize, (pageNum - 1) * pageSize));
    }

    @PostMapping("/course/declare")
    @PreAuthorize("hasAuthority('teacher')")
    public ResponseResult declareOneCourse(@RequestBody ApprovalingCourse course) {
        return teacherService.declareOneCourse(course) ? new ResponseResult(HttpStatus.OK.value(), "申请成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "申请失败，该班级已经开设了该课程");
    }

    @GetMapping("/course/cancel/{id}")
    @PreAuthorize("hasAuthority('teacher')")
    public ResponseResult cancelCourse(@PathVariable Integer id) {
        return teacherService.cancelCourse(id) ? new ResponseResult(HttpStatus.OK.value(), "取消成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "取消失败，请联系管理人员");
    }

    @PostMapping("/course/edit")
    @PreAuthorize("hasAuthority('teacher')")
    public ResponseResult editCourse(@RequestBody ApprovalingCourse course) {
        return teacherService.editCourse(course) ? new ResponseResult(HttpStatus.OK.value(), "操作成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "操作失败，请联系管理人员");
    }

    @PostMapping("/course/delete/info")
    @PreAuthorize("hasAuthority('teacher')")
    public ResponseResult deleteCourseInfo(@RequestBody List<Integer> id) {
        return teacherService.deleteCourseInfo(id) ? new ResponseResult(HttpStatus.OK.value(), "删除成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "删除失败，请联系管理人员");
    }

    @GetMapping("/category")
    @RolesAllowed({"admin", "teacher"})
    public ResponseResult getCategoryList() {
        return new ResponseResult(HttpStatus.OK.value(), "获取成功", teacherService.getCategoryList());
    }

    @GetMapping("/course/table/{current_week}")
    @PreAuthorize("hasAuthority('teacher')")
    public ResponseResult getCourseTable(@PathVariable("current_week") Integer currentWeek) {
        return new ResponseResult(HttpStatus.OK.value(), "获取成功", teacherService.getCourseTable(currentWeek));
    }

    @GetMapping("/info")
    @PreAuthorize("hasAuthority('teacher')")
    public ResponseResult getInfo() {
        return new ResponseResult(HttpStatus.OK.value(), "获取成功", teacherService.getInfo());
    }

    @PostMapping("/info/modify")
    @PreAuthorize("hasAuthority('teacher')")
    public ResponseResult modifyInfo(@RequestBody InfoDto postForm) {
        return teacherService.saveInfo(postForm) ? new ResponseResult(HttpStatus.OK.value(), "修改成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "修改失败，请联系管理员");

    }

    @PostMapping("/modify/password")
    @PreAuthorize("hasAuthority('teacher')")
    public ResponseResult modifyPassword(@RequestBody PasswordForm passwordForm) {
        if (commonService.verifyIdentity(passwordForm.getOldPassword())) {
            return teacherService.modifyPassword(passwordForm) ? new ResponseResult(HttpStatus.OK.value(), "修改成功") :
                    new ResponseResult(HttpStatus.FORBIDDEN.value(), "修改失败，请联系管理员");
        }
        return new ResponseResult(HttpStatus.FORBIDDEN.value(), "密码错误，请重新输入");
    }

    @PostMapping("/change/course/time")
    @PreAuthorize("hasAuthority('teacher')")
    public ResponseResult changeCourseTime(@RequestBody ChangeCourseDto form) {
        return teacherService.changeCourseTime(form);
    }

    @PostMapping("/course/reschedule/record/{pageSize}/{pageNum}")
    @PreAuthorize("hasAuthority('teacher')")
    public ResponseResult getRecords(@RequestBody ChangeCourseDto form,
                                     @PathVariable Integer pageSize, @PathVariable Integer pageNum) {
        return teacherService.getRecords(form, pageSize, (pageNum - 1) * pageSize);
    }

    @DeleteMapping("/delete/record/{id}")
    @PreAuthorize("hasAuthority('teacher')")
    public ResponseResult deleteRecord(@PathVariable Integer id){
        return teacherService.deleteRecord(id);
    }

    @PostMapping("/course/record/edit")
    @PreAuthorize("hasAuthority('teacher')")
    public ResponseResult editRecord(@RequestBody ChangeCourseDto form){
        return teacherService.editRecord(form);
    }
}
