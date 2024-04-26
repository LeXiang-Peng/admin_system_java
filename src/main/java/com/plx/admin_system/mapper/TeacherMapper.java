package com.plx.admin_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plx.admin_system.entity.ApprovalingCourse;
import com.plx.admin_system.entity.ScheduledCourseTable;
import com.plx.admin_system.entity.Teacher;
import com.plx.admin_system.entity.dto.ChangeCourseDto;
import com.plx.admin_system.entity.dto.InfoDto;
import io.swagger.models.auth.In;
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

    /**
     * get course table 获取老师课表
     *
     * @param id
     * @return
     */
    List<ScheduledCourseTable> getCourseTable(@Param("id") Integer id);

    /**
     * get info 获取教师信息
     *
     * @param id
     * @return
     */
    InfoDto getInfo(@Param("id") Integer id);

    /**
     * save info 保存教师信息
     *
     * @param info
     * @param id
     * @return
     */
    Boolean saveInfo(@Param("info") InfoDto info, @Param("id") Integer id);

    /**
     * modify password 修改密码
     *
     * @param id
     * @param newPassword
     * @return
     */
    Boolean modifyPassword(@Param("id") Integer id, @Param("newPassword") String newPassword);

    /**
     * change course time 调课
     *
     * @param form
     * @return
     */
    Boolean changeCourrseTime(@Param("form") ChangeCourseDto form);

    /**
     * get change course table 获取课表
     *
     * @param id
     * @param currWeek
     * @return
     */
    List<List<ChangeCourseDto>> getChangeCourseTable(@Param("id") Integer id, @Param("week") Integer currWeek);

    /**
     * get records 获取调课记录
     *
     * @param form
     * @return
     */
    List<ChangeCourseDto> getRecords(@Param("id") Integer id,
                                     @Param("form") ChangeCourseDto form,
                                     @Param("pageSize") Integer pageSize, @Param("pageNum") Integer pageNum);

    /**
     * delete record 删除调课记录
     *
     * @param id
     * @return
     */
    Boolean deleteRecord(@Param("id") Integer id);

    /**
     * edit record 编辑调课记录
     *
     * @param form
     * @return
     */
    Boolean editRecord(@Param("form") ChangeCourseDto form);
}
