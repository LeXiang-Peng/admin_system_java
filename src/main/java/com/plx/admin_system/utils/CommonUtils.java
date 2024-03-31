package com.plx.admin_system.utils;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.entity.views.Menu;
import com.plx.admin_system.entity.views.OptionsView;
import com.plx.admin_system.utils.pojo.CourseTask;
import com.plx.admin_system.utils.pojo.MenuList;
import com.plx.admin_system.utils.pojo.selectedOptions.Clazz;
import com.plx.admin_system.utils.pojo.selectedOptions.Options;
import com.plx.admin_system.utils.pojo.selectedOptions.Profession;
import io.jsonwebtoken.Claims;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
    public static final String IDENTITY_PERMANENT_ADMIN = "adminPlus";
    private static final List<String> weekDays = Arrays.asList("周一", "周二", "周三", "周四", "周五");
    /**
     * expected ending week 期待20周内结束所有课程
     */
    private static final Float EXPECTED_ENDING_WEEK = 20.0F;

    public static String getRedisUserKey(String userId, String userName) {
        return PREFIX + userId + userName;
    }

    public static String parseJWT(String token) {
        try {
            Claims claims = JwtUtil.parseJWT(token);
            return getRedisUserKey(claims.getSubject(), "");
        } catch (Exception e) {
            throw new RuntimeException("token非法");
        }
    }

    /**
     * generate menu 生成多级菜单
     * TODO 这里可以通过重写MyBatis xml文件自动映射成树形结构
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
     * TODO 这里可以通过重写MyBatis xml文件自动映射成树形结构
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

    /**
     * export data 导出数据的工具类
     *
     * @param pojoClass
     * @param dataSet
     * @param fileName
     * @param sheetName
     * @param title
     * @param response
     */
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

    /**
     * import data 导入数据
     *
     * @param pojoClass
     * @param file
     * @param <T>
     * @return
     */
    public static <T> List<T> importData(Class<?> pojoClass, MultipartFile file) {
        ImportParams importParams = new ImportParams();
        //标题行设置为1行，默认是0，可以不设置；依实际情况设置。
        importParams.setTitleRows(1);
        // 表头设置为1行
        importParams.setHeadRows(1);
        //设置检查 数据不能为空
        ClassExcelVerifyHandler verifyHandler = new ClassExcelVerifyHandler();
        importParams.setVerifyHandler(verifyHandler);
        try {
            return ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, importParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * commit 向数据库提交数据
     *
     * @param sqlSession
     * @return
     */
    public static ResponseResult commit(SqlSession sqlSession) {
        try {
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            sqlSession.clearCache();
            sqlSession.close();
            return new ResponseResult(HttpStatus.FORBIDDEN.value(), "文件内容有误，请重新上传");
        }
        sqlSession.clearCache();
        sqlSession.close();
        return new ResponseResult(HttpStatus.OK.value(), "导入成功");
    }

    /**
     * init tasks 初始化课表任务队列
     *
     * @param tasks
     * @param classroomListSize
     * @return
     */
    public static List<CourseTask> initTasks(List<CourseTask> tasks) {
        Integer length = tasks.size();
        for (int i = 0; i < length; i++) {
            CourseTask task = tasks.get(i);
            Integer hours = tasks.get(i).getCourse().getHours();
            double res = hours / EXPECTED_ENDING_WEEK;
            Integer times = res >= 1 ? (int) Math.round(res) : (int) Math.ceil(res);
            Integer total = hours / times / 2;
            task.setTimesOnceAWeek(times);
            task.setWeeksTotal(total);
            task.setCurrentTime(1);
            tasks.set(i, task);
            for (int j = 1; j < times; j++) {
                task.setCurrentTime(j + 1);
                tasks.add(new CourseTask(task));
            }
        }
        return tasks;
    }

}
