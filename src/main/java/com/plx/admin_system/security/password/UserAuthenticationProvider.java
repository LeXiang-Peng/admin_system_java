package com.plx.admin_system.security.password;

import com.plx.admin_system.service.UserDetailsService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
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
        UserDetails user = userDetailsService.loadUserByUseridAndRole(userId, role);
        if (Objects.isNull(user) || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("用户ID或密码错误!");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return new UserAuthenticationToken(user, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UserAuthenticationToken.class);
    }

}
