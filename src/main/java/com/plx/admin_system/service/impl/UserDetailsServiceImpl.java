package com.plx.admin_system.service.impl;

import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.Teacher;
import com.plx.admin_system.entity.User;
import com.plx.admin_system.entity.dto.MyUserDetails;
import com.plx.admin_system.mapper.CommonMapper;
import com.plx.admin_system.service.UserDetailsService;
import com.plx.admin_system.utils.CommonUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author plx
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private CommonMapper commonMapper;

    @Override
    public MyUserDetails loadUserByUseridAndRole(Integer userId, String role) throws UsernameNotFoundException {
        List<String> list = new ArrayList<>();
        switch (role) {
            case CommonUtils.IDENTITY_STUDENT:
                Student student = commonMapper.getOneStudentById(userId);
                if (Objects.isNull(student)) {
                    return null;
                }
                list.add(CommonUtils.IDENTITY_STUDENT);
                return new MyUserDetails(student, list);
            case CommonUtils.IDENTITY_TEACHER:
                Teacher teacher = commonMapper.getOneTeacherById(userId);
                if (Objects.isNull(teacher)) {
                    return null;
                }
                list.add(CommonUtils.IDENTITY_TEACHER);
                if (teacher.getIsAuthorized() == 1) {
                    list.add(CommonUtils.IDENTITY_ADMIN);
                }
                if (teacher.getIsAuthorized() == 2) {
                    list.add(CommonUtils.IDENTITY_ADMIN);
                    list.add(CommonUtils.IDENTITY_SUPER_ADMIN);
                }
                return new MyUserDetails(teacher, list);
            case CommonUtils.IDENTITY_ADMIN:
                Admin admin = commonMapper.getOneAdminById(userId);
                if (Objects.isNull(admin)) {
                    return null;
                }
                if (admin.getAdminType() == 0) {
                    list.add(CommonUtils.IDENTITY_ADMIN);
                }
                if (admin.getAdminType() == 1) {
                    list.add(CommonUtils.IDENTITY_ADMIN);
                    list.add(CommonUtils.IDENTITY_SUPER_ADMIN);
                }
                if (admin.getAdminId() == 1) {
                    list.add(CommonUtils.IDENTITY_PERMANENT_ADMIN);
                }
                return new MyUserDetails(admin, list);
            default:
                return null;
        }
    }
}
