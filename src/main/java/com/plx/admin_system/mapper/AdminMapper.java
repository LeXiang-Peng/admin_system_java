package com.plx.admin_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.ScheduledCourseTable;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.Teacher;
import com.plx.admin_system.entity.dto.InfoDto;
import com.plx.admin_system.entity.views.*;
import com.plx.admin_system.utils.pojo.schduledCourse.ClassroomInfo;
import com.plx.admin_system.utils.pojo.schduledCourse.CourseTask;
import com.plx.admin_system.utils.pojo.schduledCourse.SchedulingCourse;
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
    List<List<?>> getStudentList(@Param("queryStudentParams") StudentView queryParams,
                                 @Param("pageSize") Integer pageSize, @Param("pageNum") Integer pageNum);

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
    List<List<?>> getTeacherList(@Param("queryTeacherParams") TeacherView queryParams,
                                 @Param("pageSize") Integer pageSize, @Param("pageNum") Integer pageNum);

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
     * delete non_admin students 删除非管理员教师 逻辑删除
     *
     * @param id
     * @return Boolean
     */
    Boolean deleteNonAdminTeachers(@Param("id") List<Integer> id);

    /**
     * delete non_super_admin students 删除非超管教师 逻辑删除
     *
     * @param id
     * @return Boolean
     */
    Boolean deleteNonSuperAdminTeachers(@Param("id") List<Integer> id);

    /**
     * new teachers 批量插入教师
     *
     * @param teacher
     * @return Integer
     */
    Integer newTeachers(TeacherView teacher);

    /**
     * grant teacher 教师授权
     *
     * @param id
     * @return Boolean
     */
    Boolean grantTeacher(@Param("id") Integer id);

    /**
     * get student list 获取学生列表 并返回列表的数据条数 分页查询
     *
     * @param queryParams StudentList 查询条件
     * @param pageSize    Integer 每一页展示的数据条数
     * @param pageNum     Integer 从数据库的第pageNum开始显示
     * @return List
     */
    List<List<?>> getAdminList(@Param("queryAdminParams") AdminView queryParams,
                               @Param("pageSize") Integer pageSize, @Param("pageNum") Integer pageNum);

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

    /**
     * grant admin 恢复管理员的权限
     *
     * @param id
     * @return Boolean
     */
    Boolean grantAdmin(@Param("id") Integer id);

    /**
     * revoke admin a day 一天之后自动解除禁权
     *
     * @param id
     * @return
     */
    Boolean revokeAdminADay(@Param("id") Integer id);

    /**
     * revoke admin 永久禁权 只有永久超管才能解除禁权
     *
     * @param id
     * @return
     */
    Boolean revokeAdmin(@Param("id") Integer id);

    /**
     * privilege escalation a day 权限提升一天
     *
     * @param id
     * @return
     */
    Boolean privilegeEscalationADay(@Param("id") Integer id);

    /**
     * demotion rights 对超管进行降权
     *
     * @param id
     * @return
     */
    Boolean demotionRights(@Param("id") Integer id);

    /**
     * get granted teacher list 获取已授权的教师
     *
     * @param queryParams
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<List<?>> getGrantedTeacherList(@Param("queryTeacherParams") TeacherView queryParams,
                                        @Param("pageSize") Integer pageSize, @Param("pageNum") Integer pageNum);

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
     * @return
     */
    List<List<?>> getPendingCourses(@Param("queryPendingCoursesParams") PendingCourse queryParams,
                                    @Param("pageSize") Integer pageSize, @Param("pageNum") Integer pageNum);

    /**
     * reject course request 否决课程申报
     *
     * @param id
     * @return
     */
    Boolean rejectCourseRequest(@Param("id") Integer id);

    /**
     * pass course 通过课程申报
     *
     * @param course
     * @return
     */
    Boolean passCourseRequest(PendingCourse course);

    /**
     * get to be scheduled courses 获得 待排课程列表
     *
     * @param queryToBeScheduledParams ToBeScheduledCourses 查询条件
     * @param pageSize                 Integer 每一页展示的数据条数
     * @param pageNum                  Integer 从数据库的第pageNum开始显示
     * @return
     */
    List<List<?>> getToBeScheduledCourses(@Param("queryToBeScheduledParams")
                                                  ToBeScheduledCourses queryToBeScheduledParams,
                                          @Param("pageSize")
                                                  Integer pageSize,
                                          @Param("pageNum")
                                                  Integer pageNum);

    /**
     * get scheduled course 获得 已排课程列表
     *
     * @param queryScheduledParams
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<List<?>> getScheduledCourse(@Param("queryScheduledParams")
                                             ScheduledCourseTable queryScheduledParams,
                                     @Param("pageSize")
                                             Integer pageSize,
                                     @Param("pageNum")
                                             Integer pageNum);

    /**
     * get clazzs 获取选课的人群的专业
     *
     * @param id
     * @return
     */
    List<String> getClazzs(@Param("id") Integer id);

    /**
     * insert tasks
     * 将遗传学算法生成的课程表批量插入至数据库中
     *
     * @param task
     * @param course
     * @param courseTime
     * @param weekDay
     * @param roomInfo
     * @return
     */
    Integer insertTasks(@Param("task") CourseTask task, @Param("course") SchedulingCourse course,
                        @Param("courseTime") String courseTime, @Param("weekDay") String weekDay,
                        @Param("roomInfo") ClassroomInfo roomInfo);

    /**
     * update course info 排课之后批量更新课程is_scheduled 字段
     *
     * @param id
     * @return
     */
    Boolean updateCourseInfo(@Param("id") Integer id);

    /**
     * get info 获取管理员id
     *
     * @param id
     * @return
     */
    InfoDto getInfo(@Param("id") Integer id);

    /**
     * save info 保存管理员信息
     *
     * @param id
     * @return
     */
    Boolean saveInfo(@Param("info") InfoDto info, @Param("id") Integer id);
}
