package com.plx.admin_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plx.admin_system.entity.ApprovalingCourse;
import com.plx.admin_system.entity.Teacher;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author plx
 * @since 2024-03-13
 */
public interface TeacherMapper extends BaseMapper<Teacher> {
    /**
     * get course list 获取申报的课程列表
     *
     * @param queryParams
     * @param pageSize
     * @param pageNum
     * @param id
     * @return
     */
    List<List<?>> getCourseList(@Param("queryCourseParams") ApprovalingCourse queryParams,
                                @Param("pageSize") Integer pageSize, @Param("pageNum") Integer pageNum,
                                @Param("id") Integer id);

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
    Boolean cancelCourse(@Param("id") Integer id);

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
    Boolean deleteCourseInfo(@Param("id") List<Integer> id);

    /**
     * get category list 获取课程门类列表
     *
     * @return
     */
    List<String> getCategoryList();
}
