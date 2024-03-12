package com.plx.admin_system.mapper;

import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.Teacher;
import com.plx.admin_system.entity.views.Menu;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;

/**
 * @author plx
 */
public interface CommonMapper {

    //交由SpringSecurity框架校验
    Student getOneStudentById(@Param("id") Integer id);
    Admin getOneAdminById(@Param("id") Integer id);
    Teacher getOneTeacherById(@Param("id") Integer id);
    /**
     * get student menu 从数据库拿到学生菜单
     * @return HashMap
     */
    @MapKey("menuId")
    HashMap<Integer, Menu> getStudentMenu();
    @MapKey("menuId")
    HashMap<Integer, Menu> getTeacherAdminMenu();
    @MapKey("menuId")
    HashMap<Integer, Menu> getTeacherMenu();
    @MapKey("menuId")
    HashMap<Integer, Menu> getSuperAdminMenu();
    @MapKey("menuId")
    HashMap<Integer, Menu> getAdminMenu();
}
