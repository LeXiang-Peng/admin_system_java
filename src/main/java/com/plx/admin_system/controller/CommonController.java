package com.plx.admin_system.controller;

import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.entity.dto.UserDto;
import com.plx.admin_system.service.CommonService;
import com.plx.admin_system.utils.pojo.MenuList;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public static final int TIMES = 10;

    @GetMapping("/captcha")
    public void getCaptchaImage(HttpServletResponse response, HttpServletRequest request){
        HttpSession session = request.getSession(true);
        /*
         * 如果多次请求，超过次数TIMES，就转换构造器，将算数构造器转换为随机构造器，
         * 即从算数表达验证码转换为随机字母数字验证码
         */
        Integer count = session.getAttribute("count") == null ? 1 : (Integer) session.getAttribute("count");
        if (count <= TIMES) {
            session.setAttribute("count", count + 1);
            /*
             * 创建验证码，将验证码以数据流传输到前端，
             */
            commonService.reshapeToMathGenerator();
            commonService.createCaptchaImage(response);
        } else {
            commonService.reshapeToRandomGenerator();
            commonService.createCaptchaImage(response);
        }
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody UserDto user){
        if(!commonService.verifyCaptcha(user.getCode())){
            return new ResponseResult(403,"验证码错误");
        }else{

        }
        return null;
    }

    @GetMapping("/getMenu/{role}")
    public List<MenuList> getMenu(@PathVariable String role){
        switch (role) {
            case "admin" : return commonService.getAdminMenu();
            case "superadmin" : return commonService.getSuperAdminMenu();
            case "student" : return commonService.getStudentMenu();
            case "teacher" : return commonService.getTeacherMenu();
            default: return null;
        }
    }
}
