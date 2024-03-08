package com.plx.admin_system.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.plx.admin_system.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author plx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyUserDetails implements UserDetails {
    private User user;
    private String role;

    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;

    public MyUserDetails(User user, String role) {
        this.user = user;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (Objects.nonNull(authorities)) {
            return authorities;
        }
        authorities = new ArrayList<>();
        if(Objects.nonNull(role)) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    public Integer getUserId() {
        return user.getUserId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
