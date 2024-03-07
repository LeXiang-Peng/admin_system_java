package com.plx.admin_system.security.password;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.security.auth.Subject;
import java.util.Collection;
import java.util.List;

/**
 * @author plx
 */
public class UserAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private Object credentials;
    private String identity;

    public UserAuthenticationToken(Object principal, Object credentials, String identity) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.identity = identity;
        setAuthenticated(false);
    }

    public UserAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public String getIdentity(){
        return this.identity;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }
}
