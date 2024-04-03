package com.plx.admin_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.ScheduledCourseTable;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.Teacher;
import com.plx.admin_system.entity.dto.InfoDto;
import com.plx.admin_system.entity.dto.MyUserDetails;
import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.entity.views.*;
import com.plx.admin_system.mapper.AdminMapper;
import com.plx.admin_system.mapper.CommonMapper;
import com.plx.admin_system.security.password.UserAuthenticationToken;
import com.plx.admin_system.service.IAdminService;
import com.plx.admin_system.utils.CommonUtils;
import com.plx.admin_system.utils.GeneticAlgorithm;
import com.plx.admin_system.utils.pojo.schduledCourse.ClassroomInfo;
import com.plx.admin_system.utils.pojo.schduledCourse.CourseTask;
import com.plx.admin_system.utils.pojo.selectedOptions.Options;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author plx
 * @since 2024-03-13
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Resource
    AdminMapper adminMapper;
    @Resource
    CommonMapper commonMapper;

    @Override
    public HashMap<String, Object> getStudentList(StudentView queryParams, Integer pageSize, Integer pageNum) {
        List<List<?>> list = adminMapper.getStudentList(queryParams, pageSize, pageNum);
        HashMap<String, Object> result = new HashMap<>();
        //学生列表
        result.put("list", list.get(0));
        //数据条数
        result.put("total", list.get(1).get(0));
        return result;
    }

    @Override
    public Boolean newOneStudent(Student student) {
        try {
            return adminMapper.newOneStudent(student);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Options> getOptions() {
        return CommonUtils.generateOptions(adminMapper.getOptionsView());
    }

    @Override
    public Boolean updateOneStudent(Integer id, Student student) {
        try {
            return adminMapper.updateOneStudent(id, student);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean deleteStudents(List<Integer> id) {
        return adminMapper.deleteStudents(id);
    }

    @Override
    public Boolean resetStudentPassword(Integer id) {
        return adminMapper.resetStudentPassword(id);
    }

    @Override
    public Boolean verifyIdentity(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserAuthenticationToken token = (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        if (passwordEncoder.matches(password, loginUser.getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public String getPermission() {
        UserAuthenticationToken token = (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        if (Objects.nonNull(loginUser)) {
            //默认权限最高的字符串在最后
            Integer size = loginUser.getPermission().size();
            return loginUser.getPermission().get(size - 1);
        }
        return null;
    }

    @Override
    public void exportAllStudents(HttpServletResponse response) {
        //从数据库查询到数据
        List<StudentView> students = adminMapper.getAllStudents();
        CommonUtils.exportData(StudentView.class, students, "学生信息表.xlsx", "学生列表",
                "学生信息表", response);
    }

    @Override
    public void exportEmptyStudentExcel(HttpServletResponse response) {
        List<StudentView> students = new ArrayList<StudentView>();
        StudentView student = new StudentView();
        student.setId(88888888);
        student.setName("样例");
        student.setClazz("xx级计算机科学与技术x班(必填！！！)");
        student.setProfession("计算科学与技术(可以不填，系统会自动识别)");
        student.setDepartment("信息与工程学院(可以不填，系统会自动识别)");
        student.setGrade("第四学年下学期(可以不填，系统会自动识别)");
        student.setGender("男/女");
        students.add(student);
        CommonUtils.exportData(StudentView.class, students, "学生信息表.xlsx", "学生列表",
                "学生信息表", response);
    }

    @Override
    public ResponseResult importStudents(MultipartFile studentsFile) {
        List<StudentView> students = CommonUtils.importData(StudentView.class, studentsFile);
        if (students.size() == 0) {
            return new ResponseResult(HttpStatus.FORBIDDEN.value(), "上传文件为空，请重新上传");
        }

        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        AdminMapper mapper = sqlSession.getMapper(AdminMapper.class);
        //批处理
        students.stream().forEach(student -> mapper.newStudents(student));
        return CommonUtils.commit(sqlSession, "导入成功", "文件信息有误，请重新上传");
    }

    @Override
    public HashMap<String, Object> getTeacherList(TeacherView queryParams, Integer pageSize, Integer pageNum) {
        List<List<?>> list = adminMapper.getTeacherList(queryParams, pageSize, pageNum);
        HashMap<String, Object> result = new HashMap<>();
        //教师列表
        result.put("list", list.get(0));
        //数据条数
        result.put("total", list.get(1).get(0));
        return result;
    }

    @Override
    public List<String> getDepartmentList() {
        return adminMapper.getDepartmentList();
    }

    @Override
    public Boolean resetTeacherPassword(Integer id) {
        return adminMapper.resetTeacherPassword(id);
    }

    @Override
    public Boolean newOneTeacher(Teacher teacher) {
        try {
            return adminMapper.newOneTeacher(teacher);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void exportAllTeachers(HttpServletResponse response) {
        //从数据库查询到数据
        List<TeacherView> teachers = adminMapper.getAllTeachers();
        CommonUtils.exportData(TeacherView.class, teachers, "教师信息表.xlsx", "教师列表",
                "教师信息表", response);
    }

    @Override
    public Boolean deleteTeachers(List<Integer> id) {
        return adminMapper.deleteTeachers(id);
    }

    @Override
    public Boolean deleteNonAdminTeachers(List<Integer> id) {
        return adminMapper.deleteNonAdminTeachers(id);
    }

    @Override
    public Boolean deleteNonSuperAdminTeachers(List<Integer> id) {
        return adminMapper.deleteNonSuperAdminTeachers(id);
    }

    @Override
    public void exportEmptyTeacherExcel(HttpServletResponse response) {
        List<TeacherView> teachers = new ArrayList<TeacherView>();
        TeacherView teacher = new TeacherView();
        teacher.setId(88888888);
        teacher.setName("样例");
        teacher.setDepartment("信息与工程学院(必填!!!)");
        teacher.setGender("男/女");
        teachers.add(teacher);
        CommonUtils.exportData(TeacherView.class, teachers, "教师信息表.xlsx", "教师列表",
                "教师信息表", response);
    }

    @Override
    public ResponseResult importTeachers(MultipartFile teachersFile) {
        List<TeacherView> teachers = CommonUtils.importData(TeacherView.class, teachersFile);
        if (teachers.size() == 0) {
            return new ResponseResult(HttpStatus.FORBIDDEN.value(), "上传文件为空，请重新上传");
        }

        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        AdminMapper mapper = sqlSession.getMapper(AdminMapper.class);
        //批处理
        teachers.stream().forEach(teacher -> mapper.newTeachers(teacher));
        return CommonUtils.commit(sqlSession, "导入成功", "文件信息有误，请重新上传");
    }

    @Override
    public Boolean grantTeacher(Integer id) {
        return adminMapper.grantTeacher(id);
    }

    @Override
    public HashMap<String, Object> getAdminList(AdminView queryParams, Integer pageSize, Integer pageNum) {
        List<List<?>> list = adminMapper.getAdminList(queryParams, pageSize, pageNum);
        HashMap<String, Object> result = new HashMap<>();
        //管理员列表
        result.put("list", list.get(0));
        //数据条数
        result.put("total", list.get(1).get(0));
        return result;
    }

    @Override
    public Boolean newOneAdmin(Admin admin) {
        return adminMapper.newOneAdmin(admin);
    }

    @Override
    public Boolean deleteAdmins(List<Integer> id) {
        return adminMapper.deleteAdmins(id);
    }

    @Override
    public Boolean resetAdminPassword(Integer id) {
        return adminMapper.resetAdminPassword(id);
    }

    @Override
    public Boolean grantAdmin(Integer id) {
        return adminMapper.grantAdmin(id);
    }

    @Override
    public Boolean revokeAdminADay(Integer id) {
        return adminMapper.revokeAdminADay(id);
    }

    @Override
    public Boolean revokeAdmin(Integer id) {
        return adminMapper.revokeAdmin(id);
    }

    @Override
    public Boolean privilegeEscalationADay(Integer id) {
        return adminMapper.privilegeEscalationADay(id);
    }

    @Override
    public Boolean demotionRights(Integer id) {
        return adminMapper.demotionRights(id);
    }

    @Override
    public HashMap<String, Object> getGrantedTeacherList(TeacherView queryParams, Integer pageSize, Integer pageNum) {
        List<List<?>> list = adminMapper.getGrantedTeacherList(queryParams, pageSize, pageNum);
        HashMap<String, Object> result = new HashMap<>();
        //已授权教师列表
        result.put("list", list.get(0));
        //数据条数
        result.put("total", list.get(1).get(0));
        return result;
    }

    @Override
    public Boolean revokeTeacher(Integer id) {
        return adminMapper.revokeTeacher(id);
    }

    @Override
    public Boolean banTeacherADay(Integer id) {
        return adminMapper.banTeacherADay(id);
    }

    @Override
    public Boolean banTeacher(Integer id) {
        return adminMapper.banTeacher(id);
    }

    @Override
    public Boolean privilegeEscalationADay2(Integer id) {
        return adminMapper.privilegeEscalationADay2(id);
    }

    @Override
    public HashMap<String, Object> getPendingCourses(PendingCourse queryParams, Integer pageSize, Integer pageNum) {
        List<List<?>> list = adminMapper.getPendingCourses(queryParams, pageSize, pageNum);
        HashMap<String, Object> result = new HashMap<>();
        //待审核课程列表
        result.put("list", list.get(0));
        //数据条数
        result.put("total", list.get(1).get(0));
        return result;
    }

    @Override
    public Boolean rejectCourseRequest(Integer id) {
        return adminMapper.rejectCourseRequest(id);
    }

    @Override
    public ResponseResult passCourseRequest(PendingCourse course) {
        try {
            return adminMapper.passCourseRequest(course) ? new ResponseResult(HttpStatus.OK.value(), "通过成功") :
                    new ResponseResult(HttpStatus.FORBIDDEN.value(), "通过失败，请联系管理人员");
        } catch (Exception e) {
            adminMapper.rejectCourseRequest(course.getId());
            return new ResponseResult(HttpStatus.FORBIDDEN.value(), "通过失败，该班级已经开设该课程，已自动否决");
        }
    }

    @Override
    public HashMap<String, Object> getToBeScheduledCourses(ToBeScheduledCourses queryParams, Integer pageSize, Integer pageNum) {
        List<List<?>> list = adminMapper.getToBeScheduledCourses(queryParams, pageSize, pageNum);
        HashMap<String, Object> result = new HashMap<>();
        //待排课程列表
        result.put("list", list.get(0));
        //数据条数
        result.put("total", list.get(1).get(0));
        return result;
    }

    @Override
    public HashMap<String, Object> getScheduledCourse(ScheduledCourseTable queryParams, Integer pageSize, Integer pageNum) {
        List<List<?>> list = adminMapper.getScheduledCourse(queryParams, pageSize, pageNum);
        HashMap<String, Object> result = new HashMap<>();
        //待排课程列表
        result.put("list", list.get(0));
        //数据条数
        result.put("total", list.get(1).get(0));
        return result;
    }

    @Override
    public List<String> getClazzs(Integer id) {
        return adminMapper.getClazzs(id);
    }

    @Override
    public ResponseResult arrangeCourseTableByGA() {
        //重置之前排好的课程
        commonMapper.resetCourseTable();
        GeneticAlgorithm algorithm = new GeneticAlgorithm();
        List<List<?>> list = commonMapper.getAllClassroom();
        List<CourseTask> tasks = CommonUtils.initTasks(commonMapper.getToBeScheduledCourse());
        List<ClassroomInfo> classroomInfoList = (List<ClassroomInfo>) list.get(0);
        Integer classroomListSize = (Integer) list.get(1).get(0);
        tasks = algorithm.evolute(tasks, classroomListSize);
        if (tasks.size() == 0) {
            return new ResponseResult(HttpStatus.NOT_IMPLEMENTED.value(), "生成失败，请重新生成");
        }
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        AdminMapper mapper = sqlSession.getMapper(AdminMapper.class);
        //批处理
        tasks.stream().forEach(item -> {
            String weekDay = CommonUtils.WEEKDAYS.get(item.getWeekDay());
            String courseTime = CommonUtils.COURSE_TIME.get(item.getCourseTime());
            ClassroomInfo roomInfo = classroomInfoList.get(item.getClassroom());
            mapper.insertTasks(item, item.getCourse(), courseTime, weekDay, roomInfo);
            mapper.updateCourseInfo(item.getId());
        });
        return CommonUtils.commit(sqlSession, "生成成功", "生成失败，请重新生成");
    }

    @Override
    public InfoDto getInfo() {
        UserAuthenticationToken token = (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        return adminMapper.getInfo(loginUser.getUserId());
    }

    @Override
    public Boolean saveInfo(InfoDto info) {
        UserAuthenticationToken token = (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails loginUser = (MyUserDetails) token.getPrincipal();
        return adminMapper.saveInfo(info, loginUser.getUserId());
    }
}

