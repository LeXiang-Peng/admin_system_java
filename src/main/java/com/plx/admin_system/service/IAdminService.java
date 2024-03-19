package com.plx.admin_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.views.StudentList;
import com.plx.admin_system.utils.pojo.Options;

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
public interface IAdminService extends IService<Admin> {
    /**
     * get student list 获取学生列表 并返回列表的数据条数
     *
     * @param queryParams StudentList 查询条件
     * @param pageSize    Integer  当前一页有多少个数据
     * @param pageNum     Integer 当前页码的第一个数据位置
     * @return Map
     */
    HashMap<String, Object> getStudentList(StudentList queryParams, Integer pageSize, Integer pageNum);

    /**
     * new one student 插入一条学生信息
     *
     * @param student
     * @return Boolean
     */
    Boolean newOneStudent(Student student);

    /**
     * 获取多级选项菜单
     * @return Options
     */
    List<Options> getOptions();

    /**
     * update one student 更新一条学生信息
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
}
