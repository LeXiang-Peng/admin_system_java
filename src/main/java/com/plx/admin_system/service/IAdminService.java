package com.plx.admin_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.Teacher;
import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.entity.views.AdminView;
import com.plx.admin_system.entity.views.StudentView;
import com.plx.admin_system.entity.views.TeacherView;
import com.plx.admin_system.utils.pojo.selectedOptions.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

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
     * @param queryParams StudentView 查询条件
     * @param pageSize    Integer  当前一页有多少个数据
     * @param pageNum     Integer 当前页码的第一个数据位置
     * @return HashMap
     */
    HashMap<String, Object> getStudentList(StudentView queryParams, Integer pageSize, Integer pageNum);

    /**
     * new one student 插入一条学生信息
     *
     * @param student
     * @return Boolean
     */
    Boolean newOneStudent(Student student);

    /**
     * 获取多级选项菜单
     *
     * @return Options
     */
    List<Options> getOptions();

    /**
     * update one student 更新一条学生信息
     *
     * @param id
     * @param student
     * @return
     */
    Boolean updateOneStudent(Integer id, Student student);

    /**
     * delete student 逻辑删除
     *
     * @param id
     * @return Boolean
     */
    Boolean deleteStudents(List<Integer> id);

    /**
     * reset student password 重置学生密码
     *
     * @param id
     * @return
     */
    Boolean resetStudentPassword(Integer id);

    /**
     * verify identity 身份验证
     *
     * @param password
     * @return Boolean
     */
    Boolean verifyIdentity(String password);

    /**
     * export all students 导出所有的学生信息
     *
     * @param response
     */
    void exportAllStudents(HttpServletResponse response);

    /**
     * export empty student excel 导出空白的 **学生信息表**
     *
     * @param response
     */
    void exportEmptyStudentExcel(HttpServletResponse response);

    /**
     * import students 导入学生信息
     *
     * @param studentsFile
     * @return Boolean
     */
    ResponseResult importStudents(MultipartFile studentsFile);

    /**
     * get teacher list 获取教师列表 并返回列表的数据条数
     *
     * @param queryParams TeacherView 查询条件
     * @param pageSize    Integer  当前一页有多少个数据
     * @param pageNum     Integer 当前页码的第一个数据位置
     * @return HashMap
     */
    HashMap<String, Object> getTeacherList(TeacherView queryParams, Integer pageSize, Integer pageNum);

    /**
     * get department list 给前端返回菜单列表
     *
     * @return
     */
    List<String> getDepartmentList();

    /**
     * reset teacher password 重置老师密码
     *
     * @param id
     * @return Boolean
     */
    Boolean resetTeacherPassword(Integer id);

    /**
     * new teacher 创建一个新的教师
     *
     * @param teacher
     * @return Boolean
     */
    Boolean newOneTeacher(Teacher teacher);

    /**
     * export all teacher 导出所有的教师信息
     *
     * @param response
     */
    void exportAllTeachers(HttpServletResponse response);

    /**
     * delete teachers 逻辑删除
     *
     * @param id
     * @return Boolean
     */
    Boolean deleteTeachers(List<Integer> id);

    /**
     * export empty teacher excel 导出空白的 **教师信息表**
     *
     * @param response
     */
    void exportEmptyTeacherExcel(HttpServletResponse response);

    /**
     * import teachers 导入教师信息
     *
     * @param teachersFile
     * @return Boolean
     */
    ResponseResult importTeachers(MultipartFile teachersFile);

    /**
     * grant 授权
     *
     * @param id
     * @return Boolean
     */
    Boolean grant(Integer id);

    /**
     * get admin list 获取管理员列表 并返回列表的数据条数
     *
     * @param queryParams
     * @param pageSize
     * @param pageNum
     * @return HashMap
     */
    HashMap<String, Object> getAdminList(AdminView queryParams, Integer pageSize, Integer pageNum);

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
    Boolean deleteAdmins(List<Integer> id);

    /**
     * reset admin password 重置管理员密码
     *
     * @param id
     * @return Boolean
     */
    Boolean resetAdminPassword(@Param("id") Integer id);
}
