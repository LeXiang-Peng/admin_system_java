package com.plx.admin_system.service;

import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.entity.dto.UserDto;
import com.plx.admin_system.utils.pojo.MenuList;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author plx
 */
public interface CommonService {
    /**
     * create captcha image 创建一张验证码
     *
     * @param key
     * @param response
     */
    void createCaptchaImage(String key, HttpServletResponse response);

    /**
     * verify code 验证验证码
     *
     * @param key
     * @param code
     * @return Boolean
     */
    Boolean verifyCode(String key, String code);

    /**
     * log in 登录
     *
     * @param user
     * @return Map
     */
    Map login(UserDto user);

    /**
     * log out 登出
     *
     * @return boolean
     */
    Boolean logout();

    /**
     * get menu 获取当前角色的菜单
     *
     * @param token
     * @return MenuList
     */
    List<MenuList> getMenu(String token);

    /**
     * verify identity 身份验证
     *
     * @param password
     * @return Boolean
     */
    Boolean verifyIdentity(String password);

    /**
     * upload avatar 上传头像
     *
     * @param file
     * @return
     */
    ResponseResult uploadAvatar(MultipartFile file);

    /**
     * get avatar 获取头像
     *
     * @param fileName
     * @return
     */
    ResponseEntity<Resource> getAvatar(String fileName);
}
