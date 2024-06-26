package com.plx.admin_system.mapper;

import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.Teacher;
import com.plx.admin_system.entity.views.Menu;
import com.plx.admin_system.entity.views.ScheduledCourseInfo;
import com.plx.admin_system.utils.pojo.schduledCourse.CourseTask;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author plx
 */
public interface CommonMapper {
    /**
     * get one student by id 通过id查询一条学生信息
     *
     * @param id
     * @return Student
     */
    Student getOneStudentById(@Param("id") Integer id);

    /**
     * get one admin by id 通过id查询一条管理员信息
     *
     * @param id
     * @return Admin
     */
    Admin getOneAdminById(@Param("id") Integer id);

    /**
     * get one teacher by id 通过id查询一条老师信息
     *
     * @param id
     * @return
     */
    Teacher getOneTeacherById(@Param("id") Integer id);
    //以上交由SpringSecurity框架校验

    /**
     * get student menu 获取学生菜单
     *
     * @return HashMap
     */
    @MapKey("menuId")
    HashMap<Integer, Menu> getStudentMenu();

    /**
     * get teacher and admin menu 获取管理员和老师权限菜单
     *
     * @return HashMap
     */
    @MapKey("menuId")
    HashMap<Integer, Menu> getTeacherAdminMenu();

    /**
     * get teacher menu 获取老师菜单
     *
     * @return HashMap
     */
    @MapKey("menuId")
    HashMap<Integer, Menu> getTeacherMenu();

    /**
     * get super admin menu 获取超级管理员菜单
     *
     * @return HashMap
     */
    @MapKey("menuId")
    HashMap<Integer, Menu> getSuperAdminMenu();

    /**
     * get admin menu 获取管理员菜单
     *
     * @return HashMap
     */
    @MapKey("menuId")
    HashMap<Integer, Menu> getAdminMenu();

    /**
     * get revoked admin menu 获取被禁权管理员菜单
     *
     * @return HashMap
     */
    @MapKey("menuId")
    HashMap<Integer, Menu> getRevokedAdminMenu();

    /**
     * get revoked admin menu 获取被禁权管理员菜单
     *
     * @return HashMap
     */
    @MapKey("menuId")
    HashMap<Integer, Menu> getTeacherSuperAdminMenu();

    /**
     * get to_be_scheduled course 获取全部待排课程
     *
     * @return
     */
    List<CourseTask> getToBeScheduledCourse();

    /**
     * get all classroom 获取所有教室信息
     *
     * @return
     */
    List<List<?>> getAllClassroom();

    /**
     * reset course table
     * 重置课表，便于之后再排课
     *
     * @return
     */
    Boolean resetCourseTable();

    /**
     * get scheduled course info 获取已经编排课程信息
     *
     * @return
     */
    List<ScheduledCourseInfo> getScheduledCourseInfo();

    /**
     * get avatar url 获取头像url
     *
     * @param url
     * @return
     */
    List<String> getAvatarUrl(@Param("url") String url);

    /**
     * upload admin avatar 上传管理员头像
     *
     * @param url
     * @param id
     * @return
     */
    Boolean uploadAdminAvatar(@Param("url") String url, @Param("id") Integer id);

    /**
     * upload student avatar 上传学生头像
     *
     * @param url
     * @param id
     * @return
     */
    Boolean uploadStudentAvatar(@Param("url") String url, @Param("id") Integer id);

    /**
     * upload teacher avatar 上传教师头像
     *
     * @param url
     * @param id
     * @return
     */
    Boolean uploadTeacherAvatar(@Param("url") String url, @Param("id") Integer id);
}
