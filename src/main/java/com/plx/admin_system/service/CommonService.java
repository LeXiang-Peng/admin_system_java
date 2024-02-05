package com.plx.admin_system.service;

import com.plx.admin_system.utils.pojo.MenuList;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CommonService {
    void createCaptchaImage(HttpServletResponse response);
    void reshapeToRandomGenerator();
    void reshapeToMathGenerator();
    Boolean verifyCaptcha(String code);
    List<MenuList> getAdminMenuView();
}
