package com.plx.admin_system.service.impl;

import com.plx.admin_system.entity.views.Menu;
import com.plx.admin_system.mapper.CommonMapper;
import com.plx.admin_system.utils.CommonUtils;
import com.plx.admin_system.service.CommonService;
import com.plx.admin_system.utils.pojo.MenuList;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * @author plx
 */
@Service
public class CommonServiceImpl implements CommonService {
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
    public List<MenuList> getAdminMenuView() {
        return  commonUtils.generateMenu(commonMapper.getAdminMenuView());
    }

}
