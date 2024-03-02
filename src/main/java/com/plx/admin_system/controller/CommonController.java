package com.plx.admin_system.controller;

import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.entity.dto.UserDto;
import com.plx.admin_system.service.CommonService;
import com.plx.admin_system.utils.pojo.MenuList;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author plx
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    @Resource
    CommonService commonService;

    @GetMapping("/captcha")
    public void getCaptchaImage(HttpServletResponse response, HttpSession session) {
        commonService.createCaptchaImage(response, session.getId());
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody UserDto user, HttpSession session) {
        if (!commonService.verify(user.getCode(), session.getId())) {
            return new ResponseResult(204, "验证码错误");
        } else {
            return commonService.login(user);
        }
    }

    @GetMapping("/getMenu/{role}")
    public List<MenuList> getMenu(@PathVariable String role) {
        switch (role) {
            case "admin":
                return commonService.getAdminMenu();
            case "superadmin":
                return commonService.getSuperAdminMenu();
            case "student":
                return commonService.getStudentMenu();
            case "teacher":
                return commonService.getTeacherMenu();
            default:
                return null;
        }
    }
}
