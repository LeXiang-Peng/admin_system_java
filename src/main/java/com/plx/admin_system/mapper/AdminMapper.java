package com.plx.admin_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.views.OptionsView;
import com.plx.admin_system.entity.views.StudentList;
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
public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * get student list 获取学生列表 并返回列表的数据条数
     *
     * @param queryParams StudentList 查询条件
     * @param pageSize Integer 每一页展示的数据条数
     * @param pageNum   Integer 从数据库的第pageNum开始显示
     * @return
     */
    List<List<?>> getStudentList(@Param("queryParams")StudentList queryParams, @Param("pageSize") Integer pageSize, @Param("pageNum") Integer pageNum);

    /**
     * new student 创建一个新的学生
     *
     * @param student
     * @return Boolean
     */
    Boolean newOneStudent(Student student);

    /**
     * update Student 更新学生信息
     *
     * @param student
     * @return
     */
    Boolean updateOneStudent(Student student);

    /**
     * delete student 逻辑删除
     *
     * @param id
     * @return
     */
    Boolean deleteOneStudent(Integer id);

    /**
     * reset student password 重置学生密码
     *
     * @param id
     * @return
     */
    Boolean resetStudentPassword(Integer id);

    /**
     *get options view 获得多级下拉框菜单
     * @return
     */
    List<OptionsView> getOptionsView();
}
