package com.plx.admin_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plx.admin_system.entity.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author plx
 * @since 2024-03-13
 */
public interface StudentMapper extends BaseMapper<Student> {
    /**
     * get all course list 获取 已选课程 和 可选课程
     * @param id
     * @return
     */
    List<List<?>> getAllCourseList(@Param("id") Integer id);
}
