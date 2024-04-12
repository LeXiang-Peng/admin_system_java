package com.plx.admin_system.utils;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.plx.admin_system.entity.ScheduledCourseTable;
import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.entity.views.Menu;
import com.plx.admin_system.entity.views.OptionsView;
import com.plx.admin_system.entity.views.ScheduledCourseInfo;
import com.plx.admin_system.utils.pojo.MenuList;
import com.plx.admin_system.utils.pojo.schduledCourse.ClassroomInfo;
import com.plx.admin_system.utils.pojo.schduledCourse.CourseTask;
import com.plx.admin_system.utils.pojo.schduledCourse.SchedulingCourse;
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
    public static final List<String> WEEKDAYS = Arrays.asList("周一", "周二", "周三", "周四", "周五");
    public static final List<String> COURSE_TIME = Arrays.asList("第一大节", "第二大节", "第三大节", "第四大节");
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
    public static ResponseResult commit(SqlSession sqlSession, String successMessage, String failMessage) {
        try {
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            sqlSession.clearCache();
            sqlSession.close();
            return new ResponseResult(HttpStatus.FORBIDDEN.value(), failMessage);
        }
        sqlSession.clearCache();
        sqlSession.close();
        return new ResponseResult(HttpStatus.OK.value(), successMessage);
    }

    /**
     * init tasks 初始化课表任务队列
     *
     * @param tasks
     * @return
     */
    public static List<CourseTask> initTasks(List<CourseTask> tasks) {
        Integer length = tasks.size();
        List<CourseTask> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list = initOneTask(list, tasks.get(i));
        }
        return list;
    }

    /**
     * init one task 初始化一个任务
     *
     * @param list
     * @param task
     * @return
     */
    public static List<CourseTask> initOneTask(List<CourseTask> list, CourseTask task) {
        Integer hours = task.getCourse().getHours();
        double res = hours / EXPECTED_ENDING_WEEK;
        Integer times = res >= 1 ? (int) Math.round(res - 0.2) : (int) Math.ceil(res);
        Integer total_times = (int) Math.ceil(hours / 2.0F);
        Integer weeks_total = (int) Math.ceil(hours / 2.0F / times);
        task.setTotalTimes(total_times);
        task.setTimesOnceAWeek(times);
        task.setWeeksTotal(weeks_total);
        task.setCurrentTimes(1);
        list.add(task);
        for (int j = 1; j < times; j++) {
            CourseTask temp = new CourseTask(task);
            temp.setCurrentTimes(j + 1);
            list.add(temp);
        }
        return list;
    }

    public static CourseTask initOneCourseInfo(SchedulingCourse info) {
        CourseTask task = new CourseTask();
        task.setId(info.getCourseId());
        task.setCourse(info);
        return task;
    }

    public static List<CourseTask> initCourseList(List<ScheduledCourseInfo> infos, List<ClassroomInfo> classrooms) {
        List<CourseTask> courseList = new ArrayList<>();
        for (ScheduledCourseInfo info : infos) {
            CourseTask task = new CourseTask();
            SchedulingCourse course = new SchedulingCourse();
            task.setId(info.getCourseId());
            task.setWeekDay(getWeekDay(info.getWeekday()));
            task.setCourseTime(getCourseTime(info.getCourseTime()));
            for (int i = 0; i < classrooms.size(); i++) {
                if (Objects.equals(classrooms.get(i).getClassroomName(), info.getClassroom()) &&
                        Objects.equals(classrooms.get(i).getBuildingName(), info.getBuilding())) {
                    task.setClassroom(i);
                }
            }
            course.setCourseId(info.getCourseId());
            course.setCourse(info.getCourseName());
            course.setLecturer(info.getLecturer());
            course.setLecturerId(info.getLecturerId());
            course.setClazzList(info.getClazzList());
            task.setCourse(course);
            courseList.add(task);
        }
        return courseList;
    }

    private static Integer getCourseTime(String courseTime) {
        switch (courseTime) {
            case "第一大节":
                return 0;
            case "第二大节":
                return 1;
            case "第三大节":
                return 2;
            case "第四大节":
                return 3;
            default:
                return null;
        }
    }

    private static Integer getWeekDay(String weekDay) {
        switch (weekDay) {
            case "周一":
                return 0;
            case "周二":
                return 1;
            case "周三":
                return 2;
            case "周四":
                return 3;
            case "周五":
                return 4;
            default:
                return null;
        }
    }

    public static List<Map> generateJsonCourse(List<ScheduledCourseTable> courseTableList) {
        List<Map> res = initMap();
        List list;
        for (ScheduledCourseTable courseTable : courseTableList) {
            switch (courseTable.getCourseTime()) {
                case "第一大节":
                    list = getJson(courseTable);
                    res.get(0).put(list.get(0), list.get(1));
                    break;
                case "第二大节":
                    list = getJson(courseTable);
                    res.get(1).put(list.get(0), list.get(1));
                    break;
                case "第三大节":
                    list = getJson(courseTable);
                    res.get(2).put(list.get(0), list.get(1));
                    break;
                case "第四大节":
                    list = getJson(courseTable);
                    res.get(3).put(list.get(0), list.get(1));
                    break;
                case "第五大节":
                    list = getJson(courseTable);
                    res.get(4).put(list.get(0), list.get(1));
                    break;
                default:
                    break;
            }
        }
        return res;
    }

    private static List<Map> initMap() {
        List<Map> list = new ArrayList<>();

        Map map = new HashMap();
        map.put("head", "第一小节");
        map.put("head_time", "8:00 —— 8:45");
        map.put("tail", "第二小节");
        map.put("tail_time", "8:55 —— 9:40");

        list.add(map);
        map = new HashMap();
        map.put("head", "第三小节");
        map.put("head_time", "9:50 —— 10:45");
        map.put("tail", "第四小节");
        map.put("tail_time", "11:00 —— 11:45");
        list.add(map);
        map = new HashMap();
        map.put("head", "第五小节");
        map.put("head_time", "14:00  —— 14:45");
        map.put("tail", "第六小节");
        map.put("tail_time", "15:00 —— 15:45");
        list.add(map);
        map = new HashMap();
        map.put("head", "第七小节");
        map.put("head_time", "16:00  —— 16:45");
        map.put("tail", "第八小节");
        map.put("tail_time", "17:00 —— 17:45");
        list.add(map);
        map = new HashMap();
        map.put("head_time", "18:50 - 20:30");
        list.add(map);
        return list;
    }

    private static List getJson(ScheduledCourseTable courseTable) {
        List res = new ArrayList();
        Map json = new HashMap();
        json.put("course", courseTable.getCourseName());
        json.put("classroom", courseTable.getBuildingName() + courseTable.getClassroomName());
        json.put("lecturer", courseTable.getLecturer());
        switch (courseTable.getWeekDay()) {
            case "周一":
                res.add("monday");
                res.add(json);
                return res;
            case "周二":
                res.add("tuesday");
                res.add(json);
                return res;
            case "周三":
                res.add("wednesday");
                res.add(json);
                return res;
            case "周四":
                res.add("thursday");
                res.add(json);
                return res;
            case "周五":
                res.add("friday");
                res.add(json);
                return res;
            case "周六":
                res.add("saturday");
                res.add(json);
                return res;
            case "周日":
                res.add("sunday");
                res.add(json);
                return res;
            default:
                return null;
        }
    }
}
