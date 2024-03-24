package com.plx.admin_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.Teacher;
import com.plx.admin_system.entity.dto.MyUserDetails;
import com.plx.admin_system.entity.dto.ResponseResult;
import com.plx.admin_system.entity.views.AdminView;
import com.plx.admin_system.entity.views.StudentView;
import com.plx.admin_system.entity.views.TeacherView;
import com.plx.admin_system.mapper.AdminMapper;
import com.plx.admin_system.security.password.UserAuthenticationToken;
import com.plx.admin_system.service.IAdminService;
import com.plx.admin_system.utils.CommonUtils;
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
        System.out.println(password);
        if (passwordEncoder.matches(password, loginUser.getPassword())) {
            return true;
        }
        return false;
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
        return CommonUtils.commit(sqlSession);
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
        return CommonUtils.commit(sqlSession);
    }

    @Override
    public Boolean grant(Integer id) {
        return adminMapper.grant(id);
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
}

