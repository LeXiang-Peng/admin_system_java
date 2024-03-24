package com.plx.admin_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.Teacher;
import com.plx.admin_system.entity.views.AdminView;
import com.plx.admin_system.entity.views.OptionsView;
import com.plx.admin_system.entity.views.StudentView;
import com.plx.admin_system.entity.views.TeacherView;
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
     * get student list 获取学生列表 并返回列表的数据条数 分页查询
     *
     * @param queryParams StudentList 查询条件
     * @param pageSize    Integer 每一页展示的数据条数
     * @param pageNum     Integer 从数据库的第pageNum开始显示
     * @return List
     */
    List<List<?>> getStudentList(@Param("queryStudentParams") StudentView queryParams, @Param("pageSize") Integer pageSize, @Param("pageNum") Integer pageNum);

    /**
     * new student 创建一个新的学生
     *
     * @param student
     * @return Boolean
     */
    Boolean newOneStudent(Student student);

    /**
     * update one student 更新一条学生信息
     *
     * @param id
     * @param student
     * @return Boolean
     */
    Boolean updateOneStudent(@Param("id") Integer id, Student student);

    /**
     * delete students 逻辑删除
     *
     * @param id
     * @return Boolean
     */
    Boolean deleteStudents(@Param("id") List<Integer> id);

    /**
     * reset student password 重置学生密码
     *
     * @param id
     * @return Boolean
     */
    Boolean resetStudentPassword(Integer id);

    /**
     * get options view 获得多级下拉框菜单
     *
     * @return OptionsView
     */
    List<OptionsView> getOptionsView();

    /**
     * get all students 导出所有学生的信息
     *
     * @return
     */
    List<StudentView> getAllStudents();

    /**
     * new students 批量插入学生
     *
     * @param student
     * @return Integer
     */
    Integer newStudents(StudentView student);

    /**
     * get all teachers 导出所有教师的信息
     *
     * @return TeacherView
     */
    List<TeacherView> getAllTeachers();

    /**
     * get teacher list 获取教师列表 并返回列表的数据条数 分页查询
     *
     * @param queryParams StudentList 查询条件
     * @param pageSize    Integer 每一页展示的数据条数
     * @param pageNum     Integer 从数据库的第pageNum开始显示
     * @return List
     */
    List<List<?>> getTeacherList(@Param("queryTeacherParams") TeacherView queryParams, @Param("pageSize") Integer pageSize, @Param("pageNum") Integer pageNum);

    /**
     * get department list 给前端返回下拉菜单列表
     *
     * @return String
     */
    List<String> getDepartmentList();

    /**
     * reset teacher password 重置教师密码
     *
     * @param id
     * @return Boolean
     */
    Boolean resetTeacherPassword(Integer id);

    /**
     * update one teacher 更新一条学生信息
     * TODO 编辑功能
     *
     * @param id
     * @param teacher
     * @return Boolean
     */
    Boolean updateOneTeacher(@Param("id") Integer id, Teacher teacher);

    /**
     * new teacher 创建一个新的教师
     *
     * @param teacher
     * @return Boolean
     */
    Boolean newOneTeacher(Teacher teacher);

    /**
     * delete students 逻辑删除
     *
     * @param id
     * @return Boolean
     */
    Boolean deleteTeachers(@Param("id") List<Integer> id);

    /**
     * new teachers 批量插入教师
     *
     * @param teacher
     * @return Integer
     */
    Integer newTeachers(TeacherView teacher);

    /**
     * grant 授权
     *
     * @param id
     * @return Boolean
     */
    Boolean grant(@Param("id") Integer id);

    /**
     * get student list 获取学生列表 并返回列表的数据条数 分页查询
     *
     * @param queryParams StudentList 查询条件
     * @param pageSize    Integer 每一页展示的数据条数
     * @param pageNum     Integer 从数据库的第pageNum开始显示
     * @return List
     */
    List<List<?>> getAdminList(@Param("queryAdminParams") AdminView queryParams, @Param("pageSize") Integer pageSize, @Param("pageNum") Integer pageNum);

    /**
     * new student 创建一个新的学生
     *
     * @param admin
     * @return Boolean
     */
    Boolean newOneAdmin(Admin admin);

    /**
     * delete admins 逻辑删除
     *
     * @param id
     * @return Boolean
     */
    Boolean deleteAdmins(@Param("id") List<Integer> id);

    /**
     * reset admin password 重置管理员密码
     *
     * @param id
     * @return Boolean
     */
    Boolean resetAdminPassword(@Param("id") Integer id);
}
