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
    @MapKey("menuId")
    HashMap<Integer,Menu> getAdminMenuView();
    @MapKey("menuId")
    HashMap<Integer,Menu> getSuperAdminMenuView();
    @MapKey("menuId")
    HashMap<Integer,Menu> getStudentMenuView();
    @MapKey("menuId")
    HashMap<Integer,Menu> getTeacherMenuView();
    //交由SpringSecurity框架校验
    Student getOneStudentById(@Param("id") Integer id);
    Admin getOneAdminById(@Param("id") Integer id);
    Teacher getOneTeacherById(@Param("id") Integer id);
}
