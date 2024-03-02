package com.plx.admin_system.service.impl;

import com.plx.admin_system.entity.User;
import com.plx.admin_system.entity.dto.MyUserDetails;
import com.plx.admin_system.mapper.CommonMapper;
import com.plx.admin_system.service.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private CommonMapper commonMapper;

    @Override
    public UserDetails loadUserByUseridAndRole(Integer userId, String role) throws UsernameNotFoundException {
        User user;
        switch (role) {
            case "student":
                user = commonMapper.getOneStudentById(userId);
                return Objects.isNull(user) ? null : new MyUserDetails(user);
            case "teacher":
                user = commonMapper.getOneTeacherById(userId);
                return Objects.isNull(user) ? null : new MyUserDetails(user);
            case "admin":
                user = commonMapper.getOneAdminById(userId);
                return Objects.isNull(user) ? null : new MyUserDetails(user);
            default:
                return null;
        }
    }
}
