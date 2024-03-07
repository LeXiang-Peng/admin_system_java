package com.plx.admin_system.security.password;

import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.dto.MyUserDetails;
import com.plx.admin_system.service.UserDetailsService;
import com.plx.admin_system.utils.CommonUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author plx
 */

@Getter
@Setter
public class UserAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserAuthenticationToken token = (UserAuthenticationToken) authentication;
        Integer userId = (Integer) token.getPrincipal();
        String password = (String) token.getCredentials();
        String role = token.getIdentity();
        MyUserDetails user = userDetailsService.loadUserByUseridAndRole(userId, role);
        if (Objects.isNull(user) || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("用户ID或密码错误!");
        }
        return new UserAuthenticationToken(user, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UserAuthenticationToken.class);
    }

}
