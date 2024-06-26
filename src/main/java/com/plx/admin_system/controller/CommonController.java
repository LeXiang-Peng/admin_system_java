package com.plx.admin_system.controller;

import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.entity.dto.UserDto;
import com.plx.admin_system.service.CommonService;
import com.plx.admin_system.utils.CommonUtils;
import com.plx.admin_system.utils.pojo.MenuList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author plx
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Resource
    CommonService commonService;


    @GetMapping("/captcha")
    public void getCaptchaImage(@RequestParam("key") String key, HttpServletResponse response) {
        commonService.createCaptchaImage(key, response);
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestParam("key") String key, @RequestBody UserDto user) {
        if (!commonService.verifyCode(key, user.getCaptcha())) {
            return new ResponseResult(HttpStatus.NO_CONTENT.value(), "验证码错误");
        } else {
            Map map = commonService.login(user);
            return Objects.isNull(map) ? new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "出现错误，请联系管理人员")
                    : new ResponseResult(HttpStatus.OK.value(), "登录成功", map);
        }
    }

    @GetMapping("/getMenu")
    public ResponseResult getMenu(HttpServletRequest request) {
        List<MenuList> menu = commonService.getMenu(request.getHeader(CommonUtils.HEADER_TOKEN_KEY));
        return Objects.isNull(menu) ? new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "出现错误，请联系管理人员")
                : new ResponseResult(HttpStatus.OK.value(), "获取成功", menu);
    }

    @GetMapping("/logout")
    public ResponseResult logout() {
        return commonService.logout() ? new ResponseResult(HttpStatus.OK.value(), "登出成功")
                : new ResponseResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "出现错误，请联系管理人员");
    }

    @PostMapping("/upload")
    public ResponseResult uploadAvatar(MultipartFile file) {
        return commonService.uploadAvatar(file);
    }

    @GetMapping("/image/{fileName}")
    public ResponseEntity<org.springframework.core.io.Resource> getAvatar(@PathVariable String fileName) {
        return commonService.getAvatar(fileName);
    }
}
