package com.plx.admin_system.service.impl;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;
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
import io.swagger.models.auth.In;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private static final Integer MAP_COUNT = 100;
    private static final Integer VISISTS_TIMES = 10;

    /**
     * 基于LRU策略的缓存，即 最近最少使用
     */
    private ConcurrentLinkedHashMap<String, Object> map = new ConcurrentLinkedHashMap.Builder<String, Object>()
            .maximumWeightedCapacity(MAP_COUNT)
            .weigher(Weighers.singleton())
            .build();

    @Override
    public void createCaptchaImage(HttpServletResponse response, String sessionId) {
        Captcha captcha = (Captcha) map.get(sessionId);
        if (Objects.isNull(captcha)) {
            captcha = new Captcha();
        }
        Integer visits = captcha.getVisits();
        if (Objects.equals(visits, VISISTS_TIMES)) {
            captcha.reshapeToRandomGenerator();
        }
        if (visits <= VISISTS_TIMES) {
            captcha.setVisits(visits + 1);
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
            String userId = String.valueOf(loginUser.getUserId());
            String jwt = JwtUtil.createJWT(userId);
            Map<String, String> map = new HashMap();
            map.put("token", jwt);
            //把完整的用户信息存入redis, userId 作为key
            redisCache.setCacheObject(CommonUtils.getRedisUserKey(userId), loginUser);
            return new ResponseResult(200, "登录成功", map);
        }
    }

    @Override
    public ResponseResult logout() {
        //从SecurityContextHolder中获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) authentication.getPrincipal();
        String userId = String.valueOf(loginUser.getUserId());
        //删除Redis删除userId
        redisCache.deleteObject(CommonUtils.getRedisUserKey(userId));
        return new ResponseResult(200, "登出成功");
    }

}
