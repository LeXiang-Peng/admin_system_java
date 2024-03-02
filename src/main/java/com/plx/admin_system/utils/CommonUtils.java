package com.plx.admin_system.utils;

import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import com.plx.admin_system.entity.views.Menu;
import com.plx.admin_system.utils.pojo.Captcha;
import com.plx.admin_system.utils.pojo.MenuList;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author plx
 */
@Component
public class CommonUtils {
    @Resource
    Captcha captcha;
    //将验证码图片输送到前端
    public void createCaptchaImage(HttpServletResponse response) {
        try {
            captcha.getLineCaptcha().write(response.getOutputStream());
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //将验证码调整为字母+数字，默认为数学运算
    public void reshapeToRandomGenerator() {
        captcha.getLineCaptcha().setGenerator(new RandomGenerator(captcha.getCount()));
        captcha.getLineCaptcha().createCode();
        captcha.setCaptcha(captcha.getLineCaptcha().getCode());
    }
    //将验证码调整为数学运算
    public void reshapeToMathGenerator(){
        captcha.getLineCaptcha().setGenerator(new MathGenerator());
        captcha.getLineCaptcha().createCode();
        captcha.setCaptcha(captcha.getLineCaptcha().getCode());
    }
    //验证前端传过来的验证码的正确性
    public Boolean verifyCaptcha(String code){
        return captcha.getLineCaptcha().verify(code);
    }
    //生成菜单
    public List<MenuList> generateMenu(HashMap<Integer,Menu> menuView){
        //将List索引和ParentMenuId建立hash映射
        HashMap<Integer,Integer> index=new HashMap<>();
        List<MenuList> menuListArrayList =new ArrayList<>();
        for(Menu menu : menuView.values()){
            Integer parentMenuId = menu.getParentMenuId(); //父菜单的id，规定父菜单id为0，则为根菜单
            MenuList current_menu = new MenuList(menu);
            if(parentMenuId == 0 && index.get(menu.getMenuId()) == null){ //根菜单如果没有加入list中，则将其加入，
                menuListArrayList.add(current_menu);           // 并把根菜单的id与list中的index形成hash映射，方便取值
                index.put(menu.getMenuId(),menuListArrayList.indexOf(current_menu));
            }else{
                Integer getIndex = index.get(parentMenuId); //将父菜单的index从hash映射中取出
                MenuList parentMenu = getIndex == null? null : menuListArrayList.get(getIndex);
                if(parentMenu != null){ //如果父菜单已经加入到list中，只需将当前菜单添加至父菜单的chirldrenMenu的list属性中
                    parentMenu.getMenuChildren().add(current_menu);
                    menuListArrayList.set(getIndex,parentMenu);
                }else{  //如果父菜单还未加入到list中，则将需要将父菜单从菜单列表中取出，并将当前菜单加入父菜单的chirldrenMenu属性中
                    parentMenu = new MenuList(menuView.get(parentMenuId));//然后将父菜单加入list中，并建立将根菜单id与index建立hash映射
                    parentMenu.getMenuChildren().add(current_menu);
                    menuListArrayList.add(parentMenu);
                    index.put(parentMenuId,menuListArrayList.indexOf(parentMenu));
                }
            }
        }
        return menuListArrayList;
    }
}
