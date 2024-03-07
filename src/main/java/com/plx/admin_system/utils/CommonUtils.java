package com.plx.admin_system.utils;


import com.plx.admin_system.entity.views.Menu;
import com.plx.admin_system.utils.pojo.MenuList;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author plx
 */

@Component
public class CommonUtils {
    private static final String PREFIX = "login:";
    public static final String HEADER_KEY = "token";

    public static String getRedisUserKey(String userId) {
        return PREFIX.concat(userId);
    }


    //生成菜单
    public List<MenuList> generateMenu(HashMap<Integer, Menu> menuView) {
        //将List索引和ParentMenuId建立hash映射
        HashMap<Integer, Integer> index = new HashMap<>();
        List<MenuList> menuListArrayUtil = new ArrayList<>();
        for (Menu menu : menuView.values()) {
            //父菜单的id，规定父菜单id为0，则为根菜单
            Integer parentMenuId = menu.getParentMenuId();
            MenuList current_menu = new MenuList(menu);
            //根菜单如果没有加入list中，则将其加入，
            // 并把根菜单的id与list中的index形成hash映射，方便取值
            if (parentMenuId == 0 && index.get(menu.getMenuId()) == null) {
                menuListArrayUtil.add(current_menu);
                index.put(menu.getMenuId(), menuListArrayUtil.indexOf(current_menu));
            } else {
                //将父菜单的index从hash映射中取出
                Integer getIndex = index.get(parentMenuId);
                MenuList parentMenu = getIndex == null ? null : menuListArrayUtil.get(getIndex);
                if (parentMenu != null) {
                    //如果父菜单已经加入到list中，只需将当前菜单添加至父菜单的chirldrenMenu的list属性中
                    parentMenu.getMenuChildren().add(current_menu);
                    menuListArrayUtil.set(getIndex, parentMenu);
                } else {
                    //如果父菜单还未加入到list中，则将需要将父菜单从菜单列表中取出，并将当前菜单加入父菜单的chirldrenMenu属性中
                    // 然后将父菜单加入list中，并建立将根菜单id与index建立hash映射
                    parentMenu = new MenuList(menuView.get(parentMenuId));
                    parentMenu.getMenuChildren().add(current_menu);
                    menuListArrayUtil.add(parentMenu);
                    index.put(parentMenuId, menuListArrayUtil.indexOf(parentMenu));
                }
            }
        }
        return menuListArrayUtil;
    }
}
