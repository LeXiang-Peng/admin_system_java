package com.plx.admin_system.filter;

import com.plx.admin_system.entity.dto.MyUserDetails;
import com.plx.admin_system.security.password.UserAuthenticationToken;
import com.plx.admin_system.utils.CommonUtils;
import com.plx.admin_system.utils.RedisCache;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author plx
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader(CommonUtils.HEADER_TOKEN_KEY);
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        //从redis中获取用户信息
        String redisKey = CommonUtils.parseJWT(token);
        MyUserDetails loginUser = redisCache.getCacheObject(redisKey);
        if (Objects.isNull(loginUser)) {
            throw new RuntimeException("请重新登录");
        }
        //存入SecurityContextHolder
        //获取权限信息
        UserAuthenticationToken userAuthenticationToken = new UserAuthenticationToken(loginUser, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(userAuthenticationToken);
        filterChain.doFilter(request, response);
    }
}
