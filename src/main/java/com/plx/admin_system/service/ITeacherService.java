package com.plx.admin_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.plx.admin_system.entity.ApprovalingCourse;
import com.plx.admin_system.entity.Teacher;
import com.plx.admin_system.entity.dto.InfoDto;

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
public interface ITeacherService extends IService<Teacher> {
    /**
     * get course list 获取当前审批的课程
     *
     * @param queryParams
     * @param pageSize
     * @param pageNum
     * @return HashMap
     */
    HashMap<String, Object> getCourseList(ApprovalingCourse queryParams, Integer pageSize, Integer pageNum);

    /**
     * declare one course 申报一个课程
     *
     * @param course
     * @return
     */
    Boolean declareOneCourse(ApprovalingCourse course);

    /**
     * cancel course
     *
     * @param id
     * @return
     */
    Boolean cancelCourse(Integer id);

    /**
     * edit form 编辑课程信息
     *
     * @param course
     * @return
     */
    Boolean editCourse(ApprovalingCourse course);

    /**
     * delete course info 删除**未在申报中**的申报课程的信息
     *
     * @param id
     * @return
     */
    Boolean deleteCourseInfo(List<Integer> id);

    /**
     * get category list 获取课程门类列表
     *
     * @return
     */
    List<String> getCategoryList();

    /**
     * get course table 获取老师课表
     *
     * @param currentWeek
     * @return
     */
    List<Map> getCourseTable(Integer currentWeek);

    /**
     * get info 获取教师信息
     *
     * @return
     */
    HashMap getInfo();

    /**
     * save info 保存教师信息
     *
     * @param info
     * @return
     */
    Boolean saveInfo(InfoDto info);
}
