package com.plx.admin_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.dto.InfoDto;
import com.plx.admin_system.entity.dto.PasswordForm;
import com.plx.admin_system.entity.views.SelectedCourse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author plx
 * @since 2024-03-13
 */
public interface IStudentService extends IService<Student> {
    /**
     * get course list 获取 已选课程 和 可选课程 列表
     *
     * @param pageNum
     * @return
     */
    HashMap<String, Object> getCourseList(Integer pageNum);

    /**
     * cancel course 取消选择的课程
     *
     * @param id
     * @return
     */
    Boolean cancelCourse(Integer id);

    /**
     * @param course
     * @return
     */
    Boolean selectCourse(SelectedCourse course);

    /**
     * get course table 获取课程列表
     *
     * @return
     */
    List<Map> getCourseTable(Integer currentWeek);

    /**
     * get info 获取个人信息
     *
     * @return
     */
    InfoDto getInfo();

    /**
     * save info 保存信息
     *
     * @param info
     * @return
     */
    Boolean saveInfo(InfoDto info);

    /**
     * modify password 修改密码
     *
     * @param passwordForm
     * @return
     */
    Boolean modifyPassword(PasswordForm passwordForm);
}
