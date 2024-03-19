package com.plx.admin_system.service;

import com.plx.admin_system.entity.dto.UserDto;
import com.plx.admin_system.utils.pojo.MenuList;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author plx
 */
public interface CommonService {
    /**
     * create captcha image 创建一张验证码
     * @param response
     * @param sessionId
     */
    void createCaptchaImage(HttpServletResponse response, String sessionId);

    /**
     * verify code 验证验证码
     * @param code
     * @param sessionId
     * @return Boolean
     */
    Boolean verifyCode(String code, String sessionId);
    /**
     * log in 登录
     * @param user
     * @return Map
     */
    Map login(UserDto user);

    /**
     * log out 登出
     * @return boolean
     */
    Boolean logout();

    /**
     * get menu 获取当前角色的菜单
     * @param token
     * @return MenuList
     */
    List<MenuList> getMenu(String token);
}
