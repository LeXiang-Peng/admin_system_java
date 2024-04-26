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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private CommonMapper commonMapper;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisCache redisCache;
    @Value("${files.upload.path}")
    private String filePath;

    private static final Integer MAP_COUNT = 100;
    private static final Integer VISISTS_TIMES = 10;
    private static final String URL_SUFFIX = "http://47.96.157.155:9090/common/image/";

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
    public Boolean verifyCode(String code, String sessionId) {
        Captcha captcha = (Captcha) map.get(sessionId);
        return Objects.isNull(captcha) ? false : captcha.verifyCode(code);
    }

    @Override
    public Map login(UserDto user) {
        //AuthenticationManager authenticate进行用户认证
        UserAuthenticationToken token = new UserAuthenticationToken(user.getId(), user.getPassword(), user.getRole());
        Authentication authenticate = authenticationManager.authenticate(token);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        } else {
            //认证成功，生成jwt，返回ResponseResult对象
            MyUserDetails loginUser = (MyUserDetails) authenticate.getPrincipal();
            String userId = String.valueOf(loginUser.getUserId());
            String userName = loginUser.getUsername();
            String jwt = JwtUtil.createJWT(userId + userName);
            Map<String, String> map = new HashMap();
            map.put("token", jwt);
            map.put("userName", userName);
            //把完整的用户信息存入redis, userId+userName 作为key
            redisCache.setCacheObject(CommonUtils.getRedisUserKey(userId, userName), loginUser);
            return map;
        }
    }

    @Override
    public Boolean logout() {
        //从SecurityContextHolder中获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) authentication.getPrincipal();
        String userId = String.valueOf(loginUser.getUserId());
        //删除Redis删除userId
        return redisCache.deleteObject(CommonUtils.getRedisUserKey(userId, loginUser.getUsername()));
    }

    @Override
    public List<MenuList> getMenu(String token) {
        MyUserDetails user = redisCache.getCacheObject(CommonUtils.parseJWT(token));
        if (Objects.isNull(user)) {
            return null;
        }
        List<String> list = user.getPermission();
        System.out.println(list);
        if (list.size() == 0) {
            return CommonUtils.generateMenu(commonMapper.getRevokedAdminMenu());
        }
        switch (list.get(0)) {
            case CommonUtils.IDENTITY_STUDENT:
                return CommonUtils.generateMenu(commonMapper.getStudentMenu());
            case CommonUtils.IDENTITY_TEACHER:
                if (list.size() == 3) {
                    return CommonUtils.generateMenu(commonMapper.getTeacherSuperAdminMenu());
                } else if (list.size() == 2) {
                    return CommonUtils.generateMenu(commonMapper.getTeacherAdminMenu());
                } else {
                    return CommonUtils.generateMenu(commonMapper.getTeacherMenu());
                }
            case CommonUtils.IDENTITY_ADMIN:
                if (list.size() == 2 || list.size() == 3) {
                    return CommonUtils.generateMenu(commonMapper.getSuperAdminMenu());
                } else {
                    return CommonUtils.generateMenu(commonMapper.getAdminMenu());
                }
            default:
                return null;
        }
    }

    @Override
    public Boolean verifyIdentity(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserAuthenticationToken token = (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        if (passwordEncoder.matches(password, loginUser.getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public ResponseResult uploadAvatar(MultipartFile file) {
        UserAuthenticationToken token =
                (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        CommonUtils.initParentFile(filePath);
        try {
            String url = CommonUtils.getMd5Url(file);
            List<String> urlList = commonMapper.getAvatarUrl(URL_SUFFIX + url);
            if (urlList.size() == 0) {
                if (uploadAvatar(loginUser.getPermission().get(0), loginUser.getUserId(), URL_SUFFIX + url)) {
                    fileTransfer(file, url);
                    return new ResponseResult(HttpStatus.OK.value(), "上传成功", URL_SUFFIX + url);
                } else {
                    return new ResponseResult(HttpStatus.FORBIDDEN.value(), "上传失败");
                }
            } else {
                return uploadAvatar(loginUser.getPermission().get(0), loginUser.getUserId(), URL_SUFFIX + url) ?
                        new ResponseResult(HttpStatus.OK.value(), "上传成功", URL_SUFFIX + url) :
                        new ResponseResult(HttpStatus.FORBIDDEN.value(), "请勿重复上传");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseResult(HttpStatus.FORBIDDEN.value(), "出现异常");
        }
    }

    @Override
    public ResponseEntity<org.springframework.core.io.Resource> getAvatar(String fileName) {
        String fullPath = filePath + fileName;
        Path filePath = Paths.get(fullPath);
        org.springframework.core.io.Resource file = new FileSystemResource(filePath);
        if (file.exists() && file.isReadable()) {
            String contentType = null;
            // 根据文件扩展名设置Content-Type
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            switch (extension) {
                case "png":
                    contentType = MediaType.IMAGE_PNG_VALUE;
                    break;
                case "jpg":
                case "jpeg":
                    contentType = MediaType.IMAGE_JPEG_VALUE;
                    break;
                // 添加其他图片格式支持
                default:
                    try {
                        throw new IOException("Unsupported image format: " + extension);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private Boolean uploadAvatar(String role, Integer id, String url) {
        switch (role) {
            case "admin":
                return commonMapper.uploadAdminAvatar(url, id);
            case "teacher":
                return commonMapper.uploadTeacherAvatar(url, id);
            case "student":
                return commonMapper.uploadStudentAvatar(url, id);
            default:
                return false;
        }
    }

    private void fileTransfer(MultipartFile file, String url) throws IOException {
        File[] files = new File(filePath).listFiles();
        Boolean isExisted = false;
        for (File file_ : files) {
            if (Objects.equals(url, file_.getName())) {
                isExisted = true;
                break;
            }
        }
        if (!isExisted) {
            file.transferTo(new File(filePath + url));
        }
    }
}
