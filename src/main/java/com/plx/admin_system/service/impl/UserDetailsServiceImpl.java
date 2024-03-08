package com.plx.admin_system.service.impl;

import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.User;
import com.plx.admin_system.entity.dto.MyUserDetails;
import com.plx.admin_system.mapper.CommonMapper;
import com.plx.admin_system.service.UserDetailsService;
import com.plx.admin_system.utils.CommonUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        User user;
        switch (role) {
            case "student":
                user = commonMapper.getOneStudentById(userId);
                return Objects.isNull(user) ? null : new MyUserDetails(user, CommonUtils.IDENTITY_STUDENT);
            case "teacher":
                user = commonMapper.getOneTeacherById(userId);
                return Objects.isNull(user) ? null : new MyUserDetails(user, CommonUtils.IDENTITY_TEACHER);
            case "admin":
                Admin admin = commonMapper.getOneAdminById(userId);
                return Objects.isNull(admin) ? null : admin.getAdminType() == 1 ?
                        new MyUserDetails(admin, CommonUtils.IDENTITY_SUPER_ADMIN) : new MyUserDetails(admin, CommonUtils.IDENTITY_ADMIN);
            default:
                return null;
        }
    }
}
