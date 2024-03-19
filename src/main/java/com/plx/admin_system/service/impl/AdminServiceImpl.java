package com.plx.admin_system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.entity.Student;
import com.plx.admin_system.entity.views.StudentList;
import com.plx.admin_system.mapper.AdminMapper;
import com.plx.admin_system.service.IAdminService;
import com.plx.admin_system.utils.CommonUtils;
import com.plx.admin_system.utils.pojo.Options;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Boolean deleteOneStudent(Integer id) {
        return adminMapper.deleteOneStudent(id);
    }

    @Override
    public Boolean resetStudentPassword(Integer id) {
        return adminMapper.resetStudentPassword(id);
    }
}
