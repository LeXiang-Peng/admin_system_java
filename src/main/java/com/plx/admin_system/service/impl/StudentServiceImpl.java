package com.plx.admin_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plx.admin_system.entity.ScheduledCourseTable;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.dto.ChangeCourseDto;
import com.plx.admin_system.entity.dto.InfoDto;
import com.plx.admin_system.entity.dto.MyUserDetails;
import com.plx.admin_system.entity.dto.PasswordForm;
import com.plx.admin_system.entity.views.SelectedCourse;
import com.plx.admin_system.mapper.StudentMapper;
import com.plx.admin_system.security.password.UserAuthenticationToken;
import com.plx.admin_system.service.IStudentService;
import com.plx.admin_system.utils.CommonUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author plx
 * @since 2024-03-13
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {
    @Resource
    StudentMapper studentMapper;

    @Override
    public HashMap<String, Object> getCourseList(Integer pageNum) {
        UserAuthenticationToken token = (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        List<List<?>> list = studentMapper.getAllCourseList(loginUser.getUserId(), pageNum);
        HashMap<String, Object> result = new HashMap<>();
        result.put("selected_course", list.get(0));
        result.put("available_course", list.get(1));
        result.put("total", list.get(2).get(0));
        return result;
    }

    @Override
    public Boolean cancelCourse(Integer id) {
        return studentMapper.cancelCourse(id);
    }

    @Override
    public Boolean selectCourse(SelectedCourse course) {
        UserAuthenticationToken token =
                (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        course.setStudentId(loginUser.getUserId());
        course.setStudent(loginUser.getUsername());
        return studentMapper.selectCourse(course);
    }

    @Override
    public List<Map> getCourseTable(Integer currentWeek) {
        UserAuthenticationToken token =
                (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        List<ScheduledCourseTable> courseTables = studentMapper.getCourseTable(loginUser.getUserId());
        List<List<ChangeCourseDto>> list = studentMapper.getRescheduledCourses(loginUser.getUserId(), currentWeek);
        List<ScheduledCourseTable> res = CommonUtils.integrate(courseTables, list, currentWeek);
        return CommonUtils.generateJsonCourse(res);
    }

    @Override
    public InfoDto getInfo() {
        UserAuthenticationToken token = (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        return studentMapper.getInfo(loginUser.getUserId());
    }

    @Override
    public Boolean saveInfo(InfoDto info) {
        UserAuthenticationToken token =
                (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        return studentMapper.saveInfo(info, loginUser.getUserId());
    }

    @Override
    public Boolean modifyPassword(PasswordForm passwordForm) {
        UserAuthenticationToken token =
                (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        //加密
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return studentMapper.modifyPassword(loginUser.getUserId(),
                passwordEncoder.encode(passwordForm.getNewPassword()));
    }

}
