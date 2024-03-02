package com.plx.admin_system.service.impl;

import com.plx.admin_system.entity.Admin;
import com.plx.admin_system.mapper.AdminMapper;
import com.plx.admin_system.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author plx
 * @since 2024-03-05
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

}
