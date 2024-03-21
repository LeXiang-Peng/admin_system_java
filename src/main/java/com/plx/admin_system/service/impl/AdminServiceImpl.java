package com.plx.admin_system.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.dto.MyUserDetails;
import com.plx.admin_system.entity.views.StudentList;
import com.plx.admin_system.mapper.AdminMapper;
import com.plx.admin_system.security.password.UserAuthenticationToken;
import com.plx.admin_system.service.IAdminService;
import com.plx.admin_system.utils.CommonUtils;
import com.plx.admin_system.utils.pojo.selectedOptions.Options;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
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
    public HashMap<String, Object> getStudentList(StudentList queryParams, Integer pageSize, Integer pageNum) {
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
        return adminMapper.newOneStudent(student);
    }

    @Override
    public List<Options> getOptions() {
        return CommonUtils.generateOptions(adminMapper.getOptionsView());
    }

    @Override
    public Boolean updateOneStudent(Student student) {
        return adminMapper.updateOneStudent(student);
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
        List<StudentList> students = adminMapper.getAllStudents();
        CommonUtils.exportData(StudentList.class, students, "学生信息表.xlsx", "学生列表", "学生信息表", response);
    }
}

