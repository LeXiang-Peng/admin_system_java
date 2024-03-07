package com.plx.admin_system.service;

import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.entity.dto.UserDto;
import com.plx.admin_system.utils.pojo.MenuList;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CommonService {
    void createCaptchaImage(HttpServletResponse response, String sessionId);

    boolean verify(String code, String sessionId);

    List<MenuList> getAdminMenu();

    List<MenuList> getSuperAdminMenu();

    List<MenuList> getStudentMenu();

    List<MenuList> getTeacherMenu();

    ResponseResult login(UserDto user);

    ResponseResult logout();
}
