package com.plx.admin_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plx.admin_system.entity.ApprovalingCourse;
import com.plx.admin_system.entity.ScheduledCourseTable;
import com.plx.admin_system.entity.Teacher;
import com.plx.admin_system.entity.dto.*;
import com.plx.admin_system.mapper.TeacherMapper;
import com.plx.admin_system.security.password.UserAuthenticationToken;
import com.plx.admin_system.service.ITeacherService;
import com.plx.admin_system.utils.CommonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author plx
 * @since 2024-03-13
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {
    @Resource
    TeacherMapper teacherMapper;

    @Override
    public HashMap<String, Object> getCourseList(ApprovalingCourse queryParams, Integer pageSize, Integer pageNum) {
        UserAuthenticationToken token = (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        List<List<?>> list = teacherMapper.getCourseList(queryParams, pageSize, pageNum, loginUser.getUserId());
        HashMap<String, Object> result = new HashMap<>();
        //申请课程列表
        result.put("list", list.get(0));
        //数据条数
        result.put("total", list.get(1).get(0));
        return result;
    }

    @Override
    public Boolean declareOneCourse(ApprovalingCourse course) {
        UserAuthenticationToken token =
                (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        course.setLecturer(loginUser.getUsername());
        course.setLecturerId(loginUser.getUserId());
        return teacherMapper.declareOneCourse(course);
    }

    @Override
    public Boolean cancelCourse(Integer id) {
        return teacherMapper.cancelCourse(id);
    }

    @Override
    public Boolean editCourse(ApprovalingCourse course) {
        return teacherMapper.editCourse(course);
    }

    @Override
    public Boolean deleteCourseInfo(List<Integer> id) {
        return teacherMapper.deleteCourseInfo(id);
    }

    @Override
    public List<String> getCategoryList() {
        return teacherMapper.getCategoryList();
    }

    @Override
    public List<Map> getCourseTable(Integer currentWeek) {
        UserAuthenticationToken token =
                (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        List<ScheduledCourseTable> courseTables = teacherMapper.getCourseTable(loginUser.getUserId());
        List<List<ChangeCourseDto>> list = teacherMapper.getChangeCourseTable(loginUser.getUserId(), currentWeek);
        List<ScheduledCourseTable> res = CommonUtils.integrate(courseTables, list, currentWeek);
        return CommonUtils.generateJsonCourse(res);
    }

    @Override
    public InfoDto getInfo() {
        UserAuthenticationToken token = (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        return teacherMapper.getInfo(loginUser.getUserId());
    }

    @Override
    public Boolean saveInfo(InfoDto info) {
        UserAuthenticationToken token = (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        return teacherMapper.saveInfo(info, loginUser.getUserId());
    }

    @Override
    public Boolean modifyPassword(PasswordForm passwordForm) {
        UserAuthenticationToken token = (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        //加密
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return teacherMapper.modifyPassword(loginUser.getUserId(), passwordEncoder.encode(passwordForm.getNewPassword()));
    }

    @Override
    public ResponseResult changeCourseTime(ChangeCourseDto form) {
        UserAuthenticationToken token =
                (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        form.setLecturerId(loginUser.getUserId());
        form.setLecturer(loginUser.getUsername());
        return teacherMapper.changeCourrseTime(form) ? new ResponseResult(HttpStatus.OK.value(), "调课成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "调课失败，请联系管理员");
    }

    @Override
    public ResponseResult getRecords(ChangeCourseDto form, Integer pageSize, Integer pageNum) {
        UserAuthenticationToken token = (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        List<ChangeCourseDto> records = teacherMapper.getRecords(loginUser.getUserId(), form, pageSize, pageNum);
        return new ResponseResult(HttpStatus.OK.value(), "获取成功", records);
    }

    @Override
    public ResponseResult deleteRecord(Integer id) {
        return teacherMapper.deleteRecord(id) ? new ResponseResult(HttpStatus.OK.value(), "删除成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "删除失败，请联系管理员");
    }

    @Override
    public ResponseResult editRecord(ChangeCourseDto form) {
        return teacherMapper.editRecord(form) ? new ResponseResult(HttpStatus.OK.value(), "编辑成功") :
                new ResponseResult(HttpStatus.FORBIDDEN.value(), "编辑失败，请联系管理员");
    }
}
