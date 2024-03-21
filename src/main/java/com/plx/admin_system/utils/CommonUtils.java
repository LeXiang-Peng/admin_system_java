package com.plx.admin_system.utils;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.plx.admin_system.entity.views.Menu;
import com.plx.admin_system.entity.views.OptionsView;
import com.plx.admin_system.entity.views.StudentList;
import com.plx.admin_system.utils.pojo.MenuList;
import com.plx.admin_system.utils.pojo.selectedOptions.Clazz;
import com.plx.admin_system.utils.pojo.selectedOptions.Options;
import com.plx.admin_system.utils.pojo.selectedOptions.Profession;
import io.jsonwebtoken.Claims;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author plx
 */

@Component
public class CommonUtils {
    private static final String PREFIX = "login:";
    public static final String HEADER_TOKEN_KEY = "token";
    public static final String IDENTITY_STUDENT = "student";
    public static final String IDENTITY_TEACHER = "teacher";
    public static final String IDENTITY_ADMIN = "admin";
    public static final String IDENTITY_SUPER_ADMIN = "admin+";

    public static String getRedisUserKey(String userId) {
        return PREFIX + userId;
    }

    public static String parseJWT(String token) {
        try {
            Claims claims = JwtUtil.parseJWT(token);
            return getRedisUserKey(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException("token非法");
        }
    }

    /**
     * generate menu 生成多级菜单
     *
     * @param menuView
     * @return
     */
    public static List<MenuList> generateMenu(HashMap<Integer, Menu> menuView) {
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

    /**
     * generate options 生成多级选项菜单
     *
     * @param optionsViewList
     * @return
     */
    public static List<Options> generateOptions(List<OptionsView> optionsViewList) {
        HashMap<String, Options> optionsHashMap = new HashMap<>();
        HashMap<String, Profession> professionHashMap = new HashMap<>();
        for (OptionsView item : optionsViewList) {
            Profession profession = professionHashMap.get(item.getProfession());
            if (Objects.isNull(profession)) {
                profession = new Profession(item.getDepartment(), item.getProfession(), item.getClazz());
            } else {
                profession.getClazzList().add(new Clazz(item.getClazz()));
            }
            professionHashMap.put(profession.getProfession(), profession);
        }
        for (Profession item : professionHashMap.values()) {
            Options options = optionsHashMap.get(item.getDepartment());
            if (Objects.isNull(options)) {
                options = new Options(item);
                optionsHashMap.put(options.getDepartment(), options);
            } else {
                options.getProfessionList().add(item);
            }
            optionsHashMap.put(options.getDepartment(), options);
        }
        return optionsHashMap.values().stream().collect(Collectors.toList());
    }

    public static void exportData(Class<?> pojoClass, Collection<?> dataSet, String fileName,
                                  String sheetName, String title, HttpServletResponse response) {
        try {
            //设置信息头，告诉浏览器内容为excel类型
            response.setHeader("content-Type", "application/vnd.ms-excel");
            fileName = new String(fileName.getBytes(), "ISO-8859-1");

            //设置下载名称
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            //字节流输出
            ServletOutputStream out = response.getOutputStream();
            //设置excel参数
            ExportParams params = new ExportParams();
            //设置sheet名
            params.setSheetName(sheetName);
            //设置标题
            params.setTitle(title);
            //导入excel
            Workbook workbook = ExcelExportUtil.exportExcel(params, pojoClass, dataSet);
            //写入
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
