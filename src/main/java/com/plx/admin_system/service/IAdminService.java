package com.plx.admin_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.ScheduledCourseTable;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.Teacher;
import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.entity.views.*;
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
     * get student list 获取**学生列表** 并返回列表的数据条数
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
     * get permission
     *
     * @return String 返回最高权限
     */
    String getPermission();

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
     * get teacher list 获取**教师列表** 并返回列表的数据条数
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
     * delete non_admin students 姗迟非管理员教师 逻辑删除
     *
     * @param id
     * @return Boolean
     */
    Boolean deleteNonAdminTeachers(List<Integer> id);

    /**
     * delete non_super_admin students 删除非超管教师 逻辑删除
     *
     * @param id
     * @return Boolean
     */
    Boolean deleteNonSuperAdminTeachers(@Param("id") List<Integer> id);

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
     * grant teacher 授权教师
     *
     * @param id
     * @return Boolean
     */
    Boolean grantTeacher(Integer id);

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
    Boolean resetAdminPassword(Integer id);

    /**
     * grant admin 恢复管理员的权限
     *
     * @param id
     * @return Boolean
     */
    Boolean grantAdmin(Integer id);

    /**
     * revoke admin a day 一天之后自动解除禁权
     *
     * @param id
     * @return
     */
    Boolean revokeAdminADay(Integer id);

    /**
     * revoke admin 永久禁权
     *
     * @param id
     * @return
     */
    Boolean revokeAdmin(Integer id);

    /**
     * privilege escalation a day 权限提升一天
     *
     * @param id
     * @return
     */
    Boolean privilegeEscalationADay(Integer id);

    /**
     * demotion rights 对超管进行降权
     *
     * @param id
     * @return
     */
    Boolean demotionRights(Integer id);

    /**
     * get teacher list 获取**已授权教师列表** 并返回列表的数据条数
     *
     * @param queryParams TeacherView 查询条件
     * @param pageSize    Integer  当前一页有多少个数据
     * @param pageNum     Integer 当前页码的第一个数据位置
     * @return HashMap
     */
    HashMap<String, Object> getGrantedTeacherList(TeacherView queryParams, Integer pageSize, Integer pageNum);

    /**
     * revoke teacher 回收教师管理员权限
     *
     * @param id
     * @return
     */
    Boolean revokeTeacher(@Param("id") Integer id);

    /**
     * ban teacher a day 禁权一天
     *
     * @param id
     * @return
     */
    Boolean banTeacherADay(@Param("id") Integer id);

    /**
     * ban teacher  永久禁权
     *
     * @param id
     * @return
     */
    Boolean banTeacher(@Param("id") Integer id);

    /**
     * privilege escalation a day 2 对老师的权限进行提升，**时限一天**
     *
     * @param id
     * @return
     */
    Boolean privilegeEscalationADay2(@Param("id") Integer id);

    /**
     * get pending courses 获得待审核的课程
     *
     * @param queryParams
     * @param pageSize
     * @param pageNum
     * @return HashMap
     */
    HashMap<String, Object> getPendingCourses(PendingCourse queryParams, Integer pageSize, Integer pageNum);

    /**
     * reject course request 否决课程设备
     *
     * @param id
     * @return
     */
    Boolean rejectCourseRequest(Integer id);

    /**
     * pass course 通过课程申报
     *
     * @param course
     * @return
     */
    ResponseResult passCourseRequest(PendingCourse course);

    /**
     * get to be scheduled courses 获得 待排课程列表
     *
     * @param queryParams ToBeScheduledCourses 查询条件
     * @param pageSize    Integer 每一页展示的数据条数
     * @param pageNum     Integer 从数据库的第pageNum开始显示
     * @return
     */
    HashMap<String, Object> getToBeScheduledCourses(ToBeScheduledCourses queryParams,
                                                    Integer pageSize,
                                                    Integer pageNum);

    /**
     * get scheduled course 获取已编排的列表
     * @param queryParams
     * @param pageSize
     * @param pageNum
     * @return
     */
    HashMap<String, Object> getScheduledCourse(ScheduledCourseTable queryParams,
                                               Integer pageSize,
                                               Integer pageNum);

    /**
     * get clazzs 获取选课的人群的专业
     * @param id
     * @return
     */
    List<String> getClazzs(Integer id);

    /**
     * arrange course table by genetic algorithm 通过遗传学算法排课
     * @return
     */
    ResponseResult arrangeCourseTableByGA();
}
