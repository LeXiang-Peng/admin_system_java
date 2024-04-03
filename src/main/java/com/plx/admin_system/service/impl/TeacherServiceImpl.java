package com.plx.admin_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plx.admin_system.entity.ApprovalingCourse;
import com.plx.admin_system.entity.ScheduledCourseTable;
import com.plx.admin_system.entity.Teacher;
import com.plx.admin_system.entity.dto.InfoDto;
import com.plx.admin_system.entity.dto.MyUserDetails;
import com.plx.admin_system.mapper.TeacherMapper;
import com.plx.admin_system.security.password.UserAuthenticationToken;
import com.plx.admin_system.service.ITeacherService;
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
        UserAuthenticationToken token = (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
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
        UserAuthenticationToken token = (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        List<ScheduledCourseTable> courseTables = teacherMapper.getCourseTable(loginUser.getUserId());
        List<ScheduledCourseTable> res = new ArrayList<>();
        for (ScheduledCourseTable courseTable : courseTables) {
            if (courseTable.getWeeksTotal() < currentWeek) {
                continue;
            }
            if (Objects.equals(courseTable.getWeeksTotal(), currentWeek)) {
                //前n-1周上的课次加上这周的课次大于总课次，说明已结课
                if ((currentWeek - 1) * courseTable.getTimesOnceAWeek() + courseTable.getCurrentTimes()
                        > courseTable.getTotalTimes()) {
                    continue;
                }
            }
            res.add(courseTable);
        }
        return CommonUtils.generateJsonCourse(res);
    }

    @Override
    public HashMap getInfo() {
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
}
