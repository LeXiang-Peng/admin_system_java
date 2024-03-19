package com.plx.admin_system.service.impl;

import com.plx.admin_system.entity.Student;
import com.plx.admin_system.mapper.StudentMapper;
import com.plx.admin_system.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author plx
 * @since 2024-03-13
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

}
