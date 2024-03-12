package com.plx.admin_system.handler;

import com.alibaba.fastjson.JSON;
import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.utils.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author plx
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseResult result = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "请重新登录");
        String resultJson = JSON.toJSONString(result);
        //处理异常
        WebUtil.renderString(response, resultJson);
    }
}
