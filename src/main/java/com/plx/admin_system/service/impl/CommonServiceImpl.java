package com.plx.admin_system.service.impl;

import com.plx.admin_system.mapper.CommonMapper;
import com.plx.admin_system.service.CommonService;
import com.plx.admin_system.utils.CommonUtils;
import com.plx.admin_system.utils.pojo.MenuList;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author plx
 */
@Service
public class CommonServiceImpl implements CommonService{
    @Resource
    CommonUtils commonUtils;
    @Resource
    CommonMapper commonMapper;

    @Override
    public void createCaptchaImage(HttpServletResponse response) {
        commonUtils.createCaptchaImage(response);
    }

    @Override
    public void reshapeToRandomGenerator() {
        commonUtils.reshapeToRandomGenerator();
    }

    @Override
    public void reshapeToMathGenerator() {
        commonUtils.reshapeToMathGenerator();
    }

    @Override
    public Boolean verifyCaptcha(String code) {
        return commonUtils.verifyCaptcha(code);
    }

    @Override
    public List<MenuList> getAdminMenu() {
        return  commonUtils.generateMenu(commonMapper.getAdminMenuView());
    }

    @Override
    public List<MenuList> getSuperAdminMenu() {
        return commonUtils.generateMenu(commonMapper.getSuperAdminMenuView());
    }

    @Override
    public List<MenuList> getStudentMenu() {
        return commonUtils.generateMenu(commonMapper.getStudentMenuView());
    }

    @Override
    public List<MenuList> getTeacherMenu() {
        return commonUtils.generateMenu(commonMapper.getTeacherMenuView());
    }

}
