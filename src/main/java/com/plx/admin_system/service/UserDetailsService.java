package com.plx.admin_system.service;

import com.plx.admin_system.entity.dto.MyUserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author plx
 */
public interface UserDetailsService {
    /**
     * 从数据库中加载用户信息 load user by userId and role
     *
     * @param userId 用户ID
     * @param role   用户角色
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    MyUserDetails loadUserByUseridAndRole(Integer userId, String role) throws UsernameNotFoundException;
}
