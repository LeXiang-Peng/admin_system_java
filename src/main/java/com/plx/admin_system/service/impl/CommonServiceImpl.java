package com.plx.admin_system.service.impl;

import com.plx.admin_system.entity.dto.MyUserDetails;
import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.entity.dto.UserDto;
import com.plx.admin_system.mapper.CommonMapper;
import com.plx.admin_system.security.password.UserAuthenticationToken;
import com.plx.admin_system.service.CommonService;
import com.plx.admin_system.utils.CommonUtils;
import com.plx.admin_system.utils.JwtUtil;
import com.plx.admin_system.utils.RedisCache;
import com.plx.admin_system.utils.pojo.Captcha;
import com.plx.admin_system.utils.pojo.MenuList;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author plx
 */
@Service
public class CommonServiceImpl implements CommonService {
    @Resource
    private CommonUtils commonUtils;
    @Resource
    private CommonMapper commonMapper;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisCache redisCache;

    private Map<String, Object> map = new HashMap<>();
    private static final Integer TIMES = 10;
    //map最大数量
    private static final Integer CURRENT_COUNT = 50;
    // 验证码过期时间 5min
    private static final double EXPIRED_TIME = 5;

    @Override
    public void createCaptchaImage(HttpServletResponse response, String sessionId) {
        Captcha captcha = (Captcha) map.get(sessionId);
        if (Objects.isNull(captcha)) {
            //是否过期
            new Thread(()->{
                this.isExpired();
            }).start();
            captcha = new Captcha();
        }
        Integer times = captcha.getVisits();
        if (Objects.equals(times, TIMES)) {
            captcha.reshapeToRandomGenerator();
        }
        if (times <= TIMES) {
            captcha.setVisits(times + 1);
            map.put(sessionId, captcha);
        }
        captcha.createCaptchaImage(response);
    }

    @Override
    public boolean verify(String code, String sessionId) {
        Captcha captcha = (Captcha) map.get(sessionId);
        return Objects.isNull(captcha) ? false : captcha.verify(code);
    }

    @Override
    public List<MenuList> getAdminMenu() {
        return commonUtils.generateMenu(commonMapper.getAdminMenuView());
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

    @Override
    public ResponseResult login(UserDto user) {
        //AuthenticationManager authenticate进行用户认证
        UserAuthenticationToken token = new UserAuthenticationToken(user.getId(), user.getPassword(), user.getRole());
        Authentication authenticate = authenticationManager.authenticate(token);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        } else {
            //认证成功，生成jwt，返回ResponseResult对象
            MyUserDetails loginUser = (MyUserDetails) authenticate.getPrincipal();
            String userId = loginUser.getUserId().toString();
            String jwt = JwtUtil.createJWT(userId);
            Map<String, String> map = new HashMap();
            map.put("token", jwt);
            //把完整的用户信息存入redis, userId 作为key
            redisCache.setCacheObject(CommonUtils.getRedisKey(userId), loginUser);
            return new ResponseResult(200, "登录成功", map);
        }
    }

    private synchronized void isExpired() {
        if (map.size() >= CURRENT_COUNT) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Captcha captcha = (Captcha) entry.getValue();
                if (captcha.getTTL() >= EXPIRED_TIME) {
                    map.remove(key);
                }
            }
        }
    }
}
