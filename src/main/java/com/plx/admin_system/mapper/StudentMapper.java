package com.plx.admin_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plx.admin_system.entity.ScheduledCourseTable;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.dto.InfoDto;
import com.plx.admin_system.entity.views.SelectedCourse;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author plx
 * @since 2024-03-13
 */
public interface StudentMapper extends BaseMapper<Student> {
    /**
     * get all course list 获取 已选课程 和 可选课程
     *
     * @param id
     * @param pageNum
     * @return
     */
    List<List<?>> getAllCourseList(@Param("id") Integer id, @Param("pageNum") Integer pageNum);

    /**
     * cancel course 取消选择的课程
     *
     * @param id
     * @return
     */
    Boolean cancelCourse(@Param("id") Integer id);

    /**
     * @param course
     * @return
     */
    Boolean selectCourse(SelectedCourse course);

    /**
     * get course table 获取课程列表
     *
     * @param id
     * @return
     */
    List<ScheduledCourseTable> getCourseTable(@Param("id") Integer id);

    /**
     * get info 获取个人信息
     *
     * @param id
     * @return
     */
    HashMap getInfo(@Param("id") Integer id);

    /**
     * save info 保存信息
     *
     * @param info
     * @param id
     * @return
     */
    Boolean saveInfo(@Param("info") InfoDto info, @Param("id") Integer id);
}
