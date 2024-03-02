package com.plx.admin_system.securityFramework;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.parameters.P;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author plx
 */
public class UserAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final String USERID = "id";
    private final String PASSWORD = "password";
    private final String ROLE = "role";
    private boolean postOnly = true;

    public UserAuthenticationFilter() {
        super(new AntPathRequestMatcher("/common/login", "POST"));
    }

    protected UserAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    protected UserAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    protected UserAuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl, authenticationManager);
    }

    protected UserAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher, AuthenticationManager authenticationManager) {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String userId = Objects.isNull(getUserId(request)) ? "" : getUserId(request);
            String password = Objects.isNull(getPassword(request)) ? "" : getPassword(request);
            String role = Objects.isNull(getRole(request)) ? "" : getRole(request);
            UserAuthenticationToken token = new UserAuthenticationToken(userId, password, role);
            this.setDetails(request, token);
            return this.getAuthenticationManager().authenticate(token);
        }
    }

    public String getUserId(HttpServletRequest request) {
        return request.getParameter(USERID);
    }

    public String getPassword(HttpServletRequest request) {
        return request.getParameter(PASSWORD);
    }

    public String getRole(HttpServletRequest request) {
        return request.getParameter(ROLE);
    }

    public void setDetails(HttpServletRequest request, UserAuthenticationToken token) {
        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
